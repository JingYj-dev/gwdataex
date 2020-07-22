package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.cas.QueueUtil;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.util.DataexsysConfiguration;
import com.hnjz.apps.oa.dataexsys.util.SerialNum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 在构建异步模型中：设计成任务的分配者
 * [充当总管的角色，分配任务]
 * @author liyzh
 *
 */
public class DataexSysDaemonWorker extends Thread {
	
	private static final Log _logger = LogFactory.getLog(DataexSysDaemonWorker.class);
	
	//任务工厂
	private TaskFactory taskFactory;

	//初始化消费者线程
	private Consumer consumer;

	/**
	 *
	 */
	private volatile boolean flag = true;

	/**
	 * the daemonworker name
	 */
	static String threadName = "DataexSysDaemonWorker";

	public DataexSysDaemonWorker() {
	}

	public void init() {
		//初始化任务工厂
		taskFactory = getTaskFactory();
		if (taskFactory == null) {
			if (_logger.isInfoEnabled()) {
				_logger.info("[初始化任务工厂失败]");
			}
		}
		//初始化consumer消费者
		consumer = Consumer.getConsumer();
		//初始化队列
		QueueUtil.initDataexSysQueue();
		this.setDaemon(true);
		this.setName(threadName);
		if (_logger.isInfoEnabled()) {
			_logger.info("[SerialNum:" + SerialNum.get() + "]" + this.getName() + ":is running... ");
		}
		//启动守护线程DaemonWorker
		start();
	}

//	public Log getLog() {
//        return LogFactory.getLog(DataexSysDaemonWorker.class);
//    }

	@Override
	public void run() {
		while (flag) {
			try {
				//采用轮询策略
				assignTask();
			} catch (Exception e) {
				if (_logger.isErrorEnabled()) {
					_logger.error(e.getMessage() == null ? "[DaemonThread Exception. ]":e.getMessage(), e);
				}
			}
			//设置间歇时间
			sleepTime(Constants.getSuccessSleepTime());
		}
	}

	private void assignTask() {
//		TaskFactory taskFactory = getTaskFactory();
		if (taskFactory != null) {
			/*List<AbstractTask> lst = taskFactory.getTask();
			for (AbstractTask abstractTask : lst) {
				consumer.putTask(abstractTask);
			}*/
			//这里要加一个做任务的循环
			//这里加一个做任务的控制循环
			//b)	取出Q1，弹出最上面对象
			//d)	处理完成后，重复b)操作
			while (true) {
				List<AbstractTask> lst = taskFactory.getTask();
				if (lst.size() == 0) {
					break;
				}
				for (AbstractTask abstractTask : lst) {
					consumer.putTask(abstractTask);
				}
			}
		}
	}

	public TaskFactory getTaskFactory() {
		try {
			String taskClassName = DataexsysConfiguration.getTaskFactory();
			Class<?> taskClazz = Class.forName(taskClassName);
			return (TaskFactory) taskClazz.newInstance();
		} catch (Exception e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[method:getTaskFactory() Exception. ]":e.getMessage(), e);
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
		ExecutorService threadPool = consumer.getThreadPool();
		if (threadPool != null) {
			threadPool.shutdownNow();			
		}
	}

}
