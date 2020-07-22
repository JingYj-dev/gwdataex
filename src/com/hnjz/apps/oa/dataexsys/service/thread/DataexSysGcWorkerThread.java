package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.cas.CacheCAS;
import com.hnjz.apps.oa.dataexsys.cas.QueueUtil;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.util.DataexsysConfiguration;
import com.hnjz.apps.oa.dataexsys.util.DataexsysSafeUtil;
import com.hnjz.apps.oa.dataexsys.util.SerialNum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashMap;

/**
 * 在构建异步模型中：担任回收超时任务的领导角色
 * @author liyzh
 *
 */
public class DataexSysGcWorkerThread extends Thread {
	
	private static final Log _logger = LogFactory.getLog(DataexSysGcWorkerThread.class);
	
	/**
	 * 
	 */
	private volatile boolean flag = true;
	//任务回收工厂
	private IGcTask gcTaskFactory;
	
	/**
	 * the daemonworker name
	 */
	static String threadName = "DataexSysGcWorkerThread";
	
	public DataexSysGcWorkerThread() {
	}
	
	public void init() {
		//初始化任务回收工厂
		gcTaskFactory = getGcTaskFactory();
		if (gcTaskFactory == null) {
			if (_logger.isInfoEnabled()) {
				_logger.info("[初始化任务回收工厂失败]");
			}
		}
		this.setDaemon(true);
		this.setName(threadName);
		if (_logger.isInfoEnabled()) {
			_logger.info("[SerialNum:" + SerialNum.get() + "]" + this.getName() + ":is running... ");
		}
		//启动守护线程DaemonWorker
		start();
	}
	
	public Log getLog() {
        return LogFactory.getLog(DataexSysGcWorkerThread.class);
    }
	
	@Override
	public void run() {
		while (flag) {
			//设置间歇时间
			sleepTime(Constants.getFailureSleepTime());//目前配置为2min
			DataexsysSafeUtil safeEnum = DataexsysSafeUtil.INSTANCE;
			safeEnum.setProducerThread(1);
			try {
				//定时任务将加工状态为2的处理时间超过Tmax的记录，重新放到相应队列
//				IGcTask gcTask = getGcTask();
				LinkedHashMap<String, String> queue = (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_DATAEXSYS);
				if ((queue == null || queue.size() == 0) && safeEnum.getConsumerThread() == 0) {
					gcTaskFactory.gcTask();
				}
			} catch (Exception e) {
				if (_logger.isErrorEnabled()) {
					_logger.error(e.getMessage() == null ? "[DaemonThread Exception. ]":e.getMessage(), e);
				}
			}finally {
				safeEnum.setProducerThread(0);
			}
		}
	}
	
	public IGcTask getGcTaskFactory() {
		try {
			String taskClassName = DataexsysConfiguration.getGcTask();
			Class<?> taskClazz = Class.forName(taskClassName);
			return (IGcTask) taskClazz.newInstance();
		} catch (Exception e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[method:getGcTask() Exception. ]":e.getMessage(), e);
			}
		}
		return null;
	}
	
	private void sleepTime(long seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[thread InterruptedException. ]":e.getMessage(), e);
			}
		}
	}
	
	public void dispose() {
		flag = false;
	}

}
