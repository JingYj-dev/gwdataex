/**
 * 
 */
package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.common.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liyzh
 *
 */
public class ThreadPoolFactory {
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(Constants.getThreadPoolSize());
	
	private static ThreadPoolFactory instance = new ThreadPoolFactory();
	
	private ThreadPoolFactory() {}
	
	public static ThreadPoolFactory getInstance() {
		return instance;
	}
	
	public ExecutorService getExecutorService() {
		return threadPool;
	}

	/**
	 * @Title: main
	 * @Description: TODO(描述方法功能:)
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
