package com.hnjz.apps.oa.dataexsys.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SerialNum {
	
	private static AtomicInteger counter = new AtomicInteger(0);
	
	private static ThreadLocal<Integer> serialNum = new ThreadLocal<Integer>(){
		@Override
		protected Integer initialValue() {
			return counter.getAndIncrement();
		}
	};
	
	public static Integer get() {
		return serialNum.get();
	}

}
