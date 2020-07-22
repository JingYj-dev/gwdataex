package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.core.configuration.Environment;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.db.util.StringHelper;
import com.hnjz.util.Md5Util;
import com.schooner.MemCached.MemcachedItem;

import java.util.HashMap;
import java.util.Map;

public class StateMapUtil {
	private static Map<String, Object> stateMap = null;
	private static StateMapUtil instance;
	public static synchronized StateMapUtil getInstance() {
		if (instance == null) {
			stateMap = new HashMap<String, Object>();
			instance = new StateMapUtil();
		}
		return instance;
	}
	public String get(String key) {
		return (String) stateMap.get(key);
	}
	public void set(String key, String beforeJobState) {
		stateMap.put(key, beforeJobState);
	}
	
	private static final int casTimes = 100;
	public static String getMd5Key(String uuid) {
		return Md5Util.MD5Encode(Environment.SYSTEM_NAME + "UuidKey:" + uuid);
	}
	/**
	 * 检查任务在当前的触发周期内初始工作是否已经执行
	 * 
	 * @return boolean <br/>
	 *         true: 已初始化 <br/>
	 *         false: 未初始化<br/>
	 */
	public static String getState(String uuid) {
		String key = getMd5Key(uuid);
		/**
		 * lState为任务执行信号量，每个集群节点服务器本机保存一份lState，memcached服务器保存一份mState <br/>
		 * 当定时触发器工作时，判断bRet=lState.equals(mState) <br/>
		 * bRet=false ：意味着任务的初始化工作“没有被”集群中的其他节点执行，本机可执行beforeJob() <br/>
		 * bRet=true：意味着任务的初始化工作“已经被”集群中的其他节点执行，本机不执行beforeJob()，赋值lState=mState<br/>
		 */
		String lState = StateMapUtil.getInstance().get(key);
		if (lState == null)
			lState = "";
		boolean bRet = false;
		boolean flag = false;
		for (int i = 1; i <= casTimes; i++) {
			MemcachedItem mi = MemCachedFactory.buildClient().gets(key);
			if (mi != null) {
				String mState = (String) mi.getValue();
				if (StringHelper.isEmpty(mState) || mState.equals(lState)) {
					StateMapUtil.getInstance().set(key, lState);
					mi.value = lState;
					flag = MemCachedFactory.buildClient().cas(key, mi.value, mi.getCasUnique());
					if (flag) {
						bRet = false;
						break;
					}
				} else {
					lState = mState;
					StateMapUtil.getInstance().set(key, lState);
					bRet = true;
					break;
				}
			} else {
				StateMapUtil.getInstance().set(key, lState);
				MemCachedFactory.buildClient().set(key, lState);
				continue;
			}
		}
		return StateMapUtil.getInstance().get(key);
	}
}
