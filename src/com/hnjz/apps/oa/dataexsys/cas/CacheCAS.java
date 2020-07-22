package com.hnjz.apps.oa.dataexsys.cas;

import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.components.cache.impl.CacheUtil;
import com.hnjz.base.memcached.MemCachedFactory;
import com.schooner.MemCached.MemcachedItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashMap;

public class CacheCAS {
	private static Log log = LogFactory.getLog(CacheCAS.class);
	//从缓存中获取数据，最底层
	public static MemcachedItem getCasObject(String cacheName, Object value){
		try {
			String key = CacheUtil.getCacheName(cacheName, value);
			return MemCachedFactory.buildClient().gets(key);
		} catch (Exception ex) {
			log.error(ex.getMessage(),ex);
			return null;
		}
	}
	
	//向缓存中存放数据，最底层
	public static boolean asynObject(String cacheName, Object value,MemcachedItem memCacheItem){
		try {
			String key = CacheUtil.getCacheName(cacheName, value);
			return MemCachedFactory.buildClient().cas(key, memCacheItem.getValue(),memCacheItem.getCasUnique());
		} catch (Exception ex) {
			return false;
		}
	}
	
	// 设置缓存对象（永久缓存），包括新增、修改功能；param1:缓存前缀，param2:对应的id，param3:要缓存的值
	public static Object setObject(String cacheName, Object value, Object obj) {
		try {
			String key = CacheUtil.getCacheName(cacheName, value);
			MemCachedFactory.buildClient().set(key, obj);
			return obj;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	/**将数据放入缓存*/
	public static boolean casIssueMsg(String cacheName, Object value, Object obj) {
		boolean flag = true;
		MemcachedItem memCacheItem = CacheCAS.getCasObject(cacheName, value);
		if (memCacheItem != null) {
			memCacheItem.value = obj;
			flag = CacheCAS.asynObject(cacheName, value, memCacheItem);
		} else {
			CacheCAS.setObject(cacheName, value, obj);
		}
		return flag;
	}

	public static boolean asynSendMsg(String cacheName, Object value, Object obj) {
		boolean flag = false;
		int nNum = Constants.getCasTimes();
		for (int i = 1; i <= nNum; i++) {
			flag = casIssueMsg(cacheName, value, obj);
			if (flag)
				break;
		}
		return flag;
	}
	/**从缓存中取出数据*/
	public static Object asynAcceptMsg(String cacheName, Object value) {
		Object msgList = null;
		LinkedHashMap<String, String> newMsgList = new LinkedHashMap<String, String>();
		boolean flag = false;
		int nNum = Constants.getCasTimes();
		for (int i = 1; i <= nNum; i++) {
			MemcachedItem memCacheItem = CacheCAS.getCasObject(cacheName, value);
			if (memCacheItem != null) {
				msgList = memCacheItem.getValue();
				memCacheItem.value = newMsgList;
				//作用：此刻让其他工作者等待有数据的queue队列[实际上是解决串行工作的问题]
				//用意：应用互斥锁模型
				flag = CacheCAS.asynObject(cacheName, value, memCacheItem);
			} else {
				flag = true;
			}
			if (flag)
				break;
		}
		return flag ? msgList : null;
	}
	
	/*
	public static boolean write(String cacheName, Object value, MsgIn res) {
		boolean flag = false;
		int nNum = Constants.getCasTimes();
		for (int i = 1; i <= nNum; i++) {
			MemcachedItem memCacheItem = CacheCAS.getCasObject(cacheName, value);
			if (memCacheItem != null) {
				LinkedHashMap<String, String> msgList = (LinkedHashMap<String, String>) memCacheItem.getValue();
				msgList.put(res.getMsgId(), res.getMsgId());
				memCacheItem.value = msgList;
				flag = CacheCAS.asynObject(cacheName, value, memCacheItem);
			} else {
				flag = true;
			}
			if (flag)
				break;
		}
		return flag;
	}
	*/
	public static void main(String[] args) {
		/*Object cacheName = "aa";
		Object value = "bb";
		String md5Value = (String)cacheName+value;
		System.out.println(md5Value);*/
		/*//放
		LinkedHashMap<String, String> queue = new LinkedHashMap<String, String>();
		queue.put("1", "helloworld");
		boolean ret = asynSendMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_MSG, queue);
		System.out.println(ret);
		//取
		queue = asynAcceptMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_MSG);
		Iterator<Entry<String, String>> iterator = queue.entrySet().iterator();
		for (; iterator.hasNext(); ) {
			Entry<String, String> item = iterator.next();
			System.out.println(item.getKey() + "\n" + item.getValue());// + "\n" + iterator.next().getValue()
		}*/
		/*LinkedHashMap<String, String> queue = new LinkedHashMap<String, String>();
		CacheCAS cas = new CacheCAS();
		Bench b1 = cas.new Bench("a", 2000, queue);
	    Bench b2 = cas.new Bench("b", 5000, queue);
	 
	    b1.start();
	    b2.start();*/
		LinkedHashMap<String, String> queue = new LinkedHashMap<String, String>();
		boolean ret = asynSendMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_DATAEXSYS, queue);
		System.out.println(ret);

		//取
		queue = (LinkedHashMap<String, String>) asynAcceptMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_DATAEXSYS);

		System.out.println(queue);
		ret = asynSendMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_DATAEXSYS, "aa");
		System.out.println(ret);

		//取
		Object obj = asynAcceptMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_DATAEXSYS);

		System.out.println(queue);

		for (int i = 0; i < 5; i++) {
			ret = asynSendMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_DATAEXSYS, queue);
			System.out.println(ret);
		}
		for (int i = 0; i < 5; i++) {
			ret = asynSendMsg(QueueUtil.PROCESS_QUEUE, QueueUtil.QUEUE_DATAEXSYS, null);
			System.out.println(ret);
		}
		obj = asynAcceptMsg(QueueUtil.PROCESS_QUEUE, "11");
		System.out.println(obj);
	}
	
}
