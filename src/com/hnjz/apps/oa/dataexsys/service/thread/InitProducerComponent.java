package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.common.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 在构建异步模型中：设计的初衷是为了管理生产者线程池
 * @author liyzh
 *
 */
public class InitProducerComponent {
	
	//声明线程池
	public static ExecutorService _threadPool;
	
	public InitProducerComponent() {
	}

	/**
	 * @Title: init
	 * @Description: TODO(描述方法功能:初始化线程池)
	 */
	public static void init() {
		_threadPool = Executors.newFixedThreadPool(Constants.getThreadPoolSize());
	}
	
	
	/**
	 * @Title: dispose
	 * @Description: TODO(描述方法功能:销毁线程池)
	 */
	public static void dispose() {
		if (_threadPool != null) {
			_threadPool.shutdownNow();			
		}
	}

}
