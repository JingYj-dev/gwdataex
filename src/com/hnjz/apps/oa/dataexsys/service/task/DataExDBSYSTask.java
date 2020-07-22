package com.hnjz.apps.oa.dataexsys.service.task;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.cas.CacheCAS;
import com.hnjz.apps.oa.dataexsys.cas.QueueUtil;
import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.pkg.model.PkgData;
import com.hnjz.apps.oa.dataexsys.pkg.model.PkgExAndEx;
import com.hnjz.apps.oa.dataexsys.service.impl.dataexsys.DataexsysProcessor;
import com.hnjz.db.query.TransactionCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashMap;
import java.util.concurrent.Callable;

public final class DataExDBSYSTask implements Callable<String> {
	private static final Log _logger = LogFactory.getLog(DataExDBSYSTask.class);
	
	private PkgData data;
	private DataexsysProcessor processor;
	private ClientInfo clientInfo;
	public DataExDBSYSTask(PkgData data, DataexsysProcessor processor, ClientInfo clientInfo) {
		this.data = data;
		this.processor = processor;
		this.clientInfo = clientInfo;
	}
	@Override
	public String call() throws Exception {
		String ret = Constants.FAILURE;
		//开启事务
		TransactionCache tx = null;
		try {
			tx = new TransactionCache();
			DataexSysTransTask task = processor.saveTransData(tx, data, clientInfo);
			tx.commit();
			if (task != null) {
				String resType = QueueUtil.QUEUE_DATAEXSYS;
				LinkedHashMap<String, String> queue = QueueUtil.getTaskQueue();
				if (queue == null) {
					if (QueueUtil.initDataexSysQueue()) {
						queue = (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(QueueUtil.PROCESS_QUEUE, resType);
					}
				}
				if (queue==null || queue.size() == 0) {
					TaskHelper.immediateProcess(task);
				} else {
					//这里有可能放不进去，假如放不进去，由补救工作者来完成放入操作
					boolean flag = QueueUtil.putDataToQueue(resType, task);
					if (!flag) {
						if (_logger.isInfoEnabled()) {
							_logger.info("任务放入队列失败");					
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
			PkgExAndEx pkg = (PkgExAndEx)data;
			String msg = "[save DB exception. ]\n" + pkg.asXML();
			//开启子线程记录失败数据
			TaskHelper.startFileWorkerThread(msg);
		}
		return ret;
	}
}
	