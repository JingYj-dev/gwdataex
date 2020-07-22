package com.hnjz.apps.oa.dataexsys.service.task;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTrans;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransAccount;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.cas.QueueUtil;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.apps.oa.dataexsys.pkg.model.PkgData;
import com.hnjz.apps.oa.dataexsys.pkg.model.PkgOaAndEx;
import com.hnjz.apps.oa.dataexsys.service.impl.dataex.DataexProcessor;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.apps.oa.dataexsys.util.DataexsysSafeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;

public final class DataExDBTask implements Callable<String> {
	private static final Log _logger = LogFactory.getLog(DataExDBTask.class);
	
		private PkgData data;
		private DataexProcessor processor;
		private DataexSysTrans original;
		public DataExDBTask(PkgData data, DataexProcessor processor, DataexSysTrans original) {
			this.data = data;
			this.processor = processor;
			this.original = original;
		}
		/**
		 * 1、该线程具备添加任务和消费任务的功能
		 * 2、当该线程执行消费任务时，如果生产者线程执行添加任务，会造成某一任务被重复执行
		 * 3、通过采用枚举类(单例模式)实现两个线程的互斥，一个线程执行、另一个线程等待
		 */
		@Override
		public String call() throws Exception {
			String ret = Constants.FAILURE;
			Identity sender = processor.getSender(data);
			//String senderId = sender.getIdentityFlag();
			//开启事务
			TransactionCache tx = null;
			//枚举类实现线程安全
			DataexsysSafeUtil safeEnum = DataexsysSafeUtil.INSTANCE;
			//考虑多线程执行
			safeEnum.setConsumerThread(safeEnum.getConsumerThread()+1);
			try {
				tx = new TransactionCache();
				while(safeEnum.getProducerThread() == 1){
					Thread.sleep(1000);
				}
				//List<List<DataexSysTransTask>> taskLst = new ArrayList<List<DataexSysTransTask>>();
				List<DataexSysTransTask> taskLst = processor.saveTransData(tx, original, data);
				//taskLst.add(processor.saveTransData(tx, original, data));
				DataexSysTransAccount account = new DataexSysTransAccount();
				TaskHelper.setOriginalAndAccount(original, sender, account, Constants.ACCOUNT_RECV);
				tx.update(original);
				tx.save(account);
				tx.commit();
				if (taskLst != null && taskLst.size() > 0) {
					//String resType = QueueUtil.QUEUE_DATAEXSYS;
					//不用每次循环都初始化一次任务队列(容易造成资源浪费)
					/*if (queue == null) {
						if (QueueUtil.initDataexSysQueue()) {
							queue = (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(QueueUtil.PROCESS_QUEUE, resType);
						}
					}*/
					LinkedHashMap<String, String> queue = QueueUtil.getTaskQueue();
					for (DataexSysTransTask dataexSysTransTask : taskLst) {
						if (queue==null || queue.size() == 0 ) {
							TaskHelper.immediateProcess(dataexSysTransTask);
						} else {
							//这里有可能放不进去，假如放不进去，由补救工作者来完成放入操作
							boolean flag = QueueUtil.putDataToQueue(QueueUtil.QUEUE_DATAEXSYS, dataexSysTransTask);
							if (!flag) {
								if (_logger.isInfoEnabled()) {
									_logger.info("任务放入队列失败");					
								}
							}
						}
					}
				}
				ret = Constants.SUCCESS;
			} catch (Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				if (_logger.isErrorEnabled()) {
					_logger.error(e.getMessage() == null ? "[save trans data error. ]":e.getMessage(), e);
				}
				PkgOaAndEx pkg = (PkgOaAndEx)data;
				String msg = "[save DB exception. ]\n" + pkg.asXML();
				//开启子线程记录失败数据
				TaskHelper.startFileWorkerThread(msg);
			}finally {
				//重置默认值
				safeEnum.setConsumerThread(safeEnum.getConsumerThread()-1);
			}
			return ret;
		}
		
	}