package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.db.util.LockUtil;
import com.hnjz.apps.oa.dataexsys.util.DataexsysConfiguration;
import com.hnjz.apps.oa.dataexsys.util.SerialNum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 在构建异步模型中：设计成任务的分配者
 * [充当总管的角色，分配任务]
 * @author liyzh
 *
 */
public class DaemonWorker extends Thread {
	
	private static final Log _logger = LogFactory.getLog(DaemonWorker.class);
	
	//初始化消费者线程
	private Consumer consumer;
	//锁的键值
	private String lockKey = Constants.getLockKey();//"wait_to_lock";
	//自旋锁和自适应自旋
	//这里不用考虑多线程情况，因为这里设计的只有一个线程[nCount是独占资源]
	private int nCount;//获取锁的次数，用以判断何时检查回收锁

	private TaskFactory taskFactory;

	/**
	 *
	 */
	private volatile boolean flag = true;

	/**
	 * the daemonworker name
	 */
	static String threadName = "DataexSysConsumeDaemonWorker";

	public DaemonWorker() {
	}

	public void init() {
		//初始化线程池[该线程池处理接收任务]
//		ThreadPoolFactory.getInstance().getExecutorService();
		//初始化任务工厂
		taskFactory = getTaskFactory();
		if (taskFactory == null) {
			if (_logger.isInfoEnabled()) {
				_logger.info("[初始化任务工厂失败]");
			}
		}
		//初始化consumer消费者
		consumer = Consumer.getConsumer();
		this.setDaemon(true);
		this.setName(threadName);
		if (_logger.isInfoEnabled()) {
			_logger.info("[SerialNum:" + SerialNum.get() + "]" + this.getName() + ":is running... ");
		}
		//启动守护线程DaemonWorker
		start();
	}

	public Log getLog() {
        return LogFactory.getLog(DaemonWorker.class);
    }

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
				//设置间歇时间
				/*sleepTime(Constants.getFailureSleepTime());*/
			}
			//设置间歇时间
			sleepTime(Constants.getSuccessSleepTime());
		}
	}
	private boolean checkStatus() {
		return LockUtil.getLock(lockKey);
	}
	//检查超时[强制获取锁]和宕机情况
	//考虑检查机制，可以周期性检查
	private boolean checkTimeout() {
		return diff();
	}

	private boolean diff() {
		Object lockObj = LockUtil.getLockObject(lockKey);
		//(1)[checkStatus() == false && lockObj == null]说明memcached节点Node已宕机
		//(2)[lockObj != null && (new Date().getTime() - ((Date) lockObj).getTime()) > Constants.timeout]说明交换节点Node已宕机
		//如出现以上两种情况，则说明需要强制释放锁，并接管资源和锁的控制权
		if (lockObj == null) {
			if (_logger.isInfoEnabled()) {
				_logger.info("[memcached error. ]");
			}
			//memcached节点Node宕掉情况下，容错机制
			//备用lockKey[这里可能存在备用lockKey还是hash到宕掉的memcached节点Node的情况，暂不考虑/稍后考虑]
			this.lockKey = Constants.getBackupKey();
			return true;
		}
		if (lockObj != null && (new Date().getTime() - ((Date) lockObj).getTime()) > Constants.getTimeOut()) {
			return true;
		}
		return false;
	}

	private void assignTask() {
		//加锁
		if (checkStatus()) {
			try {
//				TaskFactory taskFactory = getTaskFactory();
				if (taskFactory != null) {
					List<AbstractTask> lst = taskFactory.getTask();
					for (AbstractTask abstractTask : lst) {
						consumer.putTask(abstractTask);
					}
				}
			} finally {
				//释放锁
				LockUtil.releaseLock(lockKey);
			}
			//回收记数器归零
			nCount = 0;
			//设置间歇时间
			/*sleepTime(Constants.getSuccessSleepTime());*/
		} else {
			//判断是否回收锁[即判断回收的时机]
			//回收策略，回收时机到了
			if (nCount == Constants.getGcTimes()) {
				//回收同步锁[回收锁--互斥是方法，同步是目的]
				if (checkTimeout()) {
					LockUtil.releaseLock(lockKey);
				}
				//回收记数器归零
				nCount = 0;
			} else {
				nCount++;
			}
			//设置间歇时间
			//采取的策略是：自适应自旋
			/*sleepTime(Constants.getFailureSleepTime() + nCount * Constants.getFailureSleepTime());*/
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
		//
		/*ExecutorService threadRecvPool = ThreadPoolFactory.getInstance().getExecutorService();
		if (threadRecvPool != null && !threadRecvPool.isShutdown()) {
			threadRecvPool.shutdownNow();
		}*/
	}

}
