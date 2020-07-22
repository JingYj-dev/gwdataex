package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 在构建异步模型中：任务消费者
 * [抽象封装：消费者数据结构]
 * @author liyzh
 *
 */
public class Consumer {
	
	private static final Log _logger = LogFactory.getLog(Consumer.class);
	
	private ExecutorService threadPool;
	
	private Consumer() {
		initialize();
	}
	
	private void initialize() {
		//获取线程池
		threadPool = Executors.newFixedThreadPool(Constants.getThreadPoolSize());
		if (_logger.isInfoEnabled()) {
			_logger.info("threadPool initialized... ");
		}
	}
	
	ExecutorService getThreadPool() {
		return threadPool;
	}
	
	public void putTask(AbstractTask task) {
		Future<String> futureTask = threadPool.submit(getReadyTask(task));
		//备用代码[必要时可以打开]
		/*try {
			String ret = futureTask.get();
		} catch (InterruptedException e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[method:putTask(Map<String, Object> task) InterruptedException. ]":e.getMessage(), e);
			}
		} catch (ExecutionException e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[method:putTask(Map<String, Object> task) ExecutionException. ]":e.getMessage(), e);
			}
		} catch (Exception e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[method:putTask(Map<String, Object> task) RuntimeException. ]":e.getMessage(), e);
			}
		}*/
	}
	
	/**
	 * 抽象封装：待运行任务
	 * 
	 */
	private final class ReadyTask implements Callable<String> {
		private AbstractTask task;
		public ReadyTask(AbstractTask task) {
			this.task = task;
		}
		@Override
		public String call() throws Exception {
			return task.execute();
		}
	}
	
	public Callable<String> getReadyTask(AbstractTask task) {
		return new ReadyTask(task);
	}
	
	public static Consumer getConsumer() {
		return new Consumer();
	}

}
