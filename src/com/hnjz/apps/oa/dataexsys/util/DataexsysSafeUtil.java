package com.hnjz.apps.oa.dataexsys.util;

/**
 * 实现类似单例模式功能，保证线程安全，解决多线程之间变量共享
 * @author 庞书彦
 *
 */
public enum DataexsysSafeUtil {
	INSTANCE;
	private int consumerThread = 0;//0:默认值,大于等于1:该线程正在执行
	private int producerThread = 0;//0:默认值,大于等于1:该线程正在执行
	public int getConsumerThread() {
		return consumerThread;
	}
	public void setConsumerThread(int consumerThread) {
		this.consumerThread = consumerThread;
	}
	public int getProducerThread() {
		return producerThread;
	}
	public void setProducerThread(int producerThread) {
		this.producerThread = producerThread;
	}

	
}
