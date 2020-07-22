package com.hnjz.apps.oa.dataexsys.cas;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.components.cache.impl.CacheUtil;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class QueueUtil {
	private static Log log = LogFactory.getLog(QueueUtil.class);
	
	//任务加工队列CacheName
	public static final String QUEUE_DATAEXSYS = "QUEUE_DATAEXSYS";
	
	public static final String PROCESS_QUEUE = "PROCESS_QUEUE";
	
	public static final String ResProcessObj = "ResProcessObj";
	
	/*
	 * 初始化队列
	 */
	private static boolean initQueue(String resType) {
		boolean flag = false;
		try {
			LinkedHashMap<String, String> queue = (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(PROCESS_QUEUE, resType);
			if (queue == null) {
				queue = new LinkedHashMap<String, String>();
			}
			//从临时表中获取未加工的资源
			List<DataexSysTransTask> needProcessRes = getPreResProcessList();
			//将资源放入队列中
			if (needProcessRes != null && needProcessRes.size() > 0) {// && needProcessRes.size() > 0
				for (DataexSysTransTask res : needProcessRes) {
					//加一层保护
					if (res != null && res.getUuid() != null) {
						queue.put(res.getUuid(), res.getUuid());
						putResProcessToMem(res);
					}
				}
			}
			flag = CacheCAS.asynSendMsg(PROCESS_QUEUE, resType, queue);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return flag;
	}
	
	/**
	 * @Title: getPreResProcessList
	 * @Description: TODO(描述方法功能:获取未加工任务列表)
	 * @return
	 */
	public static List<DataexSysTransTask> getPreResProcessList() {
		QueryCache qc = new QueryCache("select a.uuid from DataexSysTransTask a  " + getWhere());
		setWhere(qc);
		List lst = qc.list();
		//此处需要清空缓存，否则获取的task对象为缓存中的旧对象；此处不知道原因，或许有其他解决办法(2018/11/16)
		if(lst != null && lst.size()>0){
			MemCachedFactory.buildClient().flushAll();
		}
		List<DataexSysTransTask> timeoutTasks = QueryCache.idToObj(DataexSysTransTask.class,lst);
		return timeoutTasks;
	}
	
	private static String getWhere() {
		StringBuffer sb = new StringBuffer("where (a.sendStatus=:received) or (a.sendStatus=:failure and a.resendTimes <:times) order by a.startTime");
		return sb.toString();
	}

	private static void setWhere(QueryCache qc) {
		qc.setParameter("received", Constants.kqueue_received).setParameter("failure", Constants.kqueue_failure).setParameter("times", Constants.getSendTimes());
	}
	
	/*
	 * 初始化消息加工队列
	 */
	public static boolean initDataexSysQueue() {
		return initQueue(QueueUtil.QUEUE_DATAEXSYS);
	}
	
	public static LinkedHashMap<String, String> getTaskQueue() {
		String resType = QueueUtil.QUEUE_DATAEXSYS;
		return (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(PROCESS_QUEUE, resType);
	}
	/*
	 * write 写
	 * 写操作
	 * 
	 * 将待加工的资源放入队列
	 * resType取值范围:[QueueUtil.QUEUE_MSG(消息类型数据)]
	 * res: 待加工资源的临时表对象
	 * [res == null的情况不在这里判断]
	 */
	public static boolean putDataToQueue(String resType, DataexSysTransTask task) {
		boolean flag = false;
		try {
			LinkedHashMap<String, String> queue = (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(PROCESS_QUEUE, resType);
			if (queue == null || queue.size() == 0) {
				if (initQueue(resType)) {
					queue = (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(PROCESS_QUEUE, resType);
				}
				else {//由于我们用了nNum = 500循环，queue == null情况微乎其微[nNum值可以通过参数功能动态调整]
					if (log.isInfoEnabled()) {
						log.info("方法initQueue(resType)初始化队列失败");					
					}
				}
			}
			if (queue != null) {
				queue.put(task.getUuid(), task.getUuid());
				putResProcessToMem(task);
				flag = CacheCAS.asynSendMsg(PROCESS_QUEUE, resType, queue);
				if (!flag) {
					if (log.isErrorEnabled()) {
						log.error("[当前失败值为:" + task.getUuid() + "  当前队列大小:" + queue.size() + "]");
					}
				}				
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return flag;
	}
	/*
	 * read 读
	 * 读操作
	 * 
	 * 从队列取待加工的资源(实现FIFO算法)
	 * resType取值范围:[QueueUtil.QUEUE_DOC(文档类数据), QueueUtil.QUEUE_VEDIO(视频类数据)]
	 */
	public static DataexSysTransTask getDataFromQueue(String resType) {
		boolean flag = false;
		DataexSysTransTask res = null;
		try {
			LinkedHashMap<String, String> queue = (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(PROCESS_QUEUE, resType);
			if (queue == null || queue.size() == 0) {
				return null;
			}
			if (queue != null) {//从缓存中取数据，并清理缓存
				Iterator<Entry<String, String>> iterator = queue.entrySet().iterator();
				if (iterator.hasNext()) {
					//查找key
					String key = iterator.next().getKey();
					//获取res任务
					res = getResProcessFromMem(key);
					//从队列中清除res任务ID
					queue.remove(key);
					//从mem中清除res任务对象
					removeResFromMem(key);
					//向mem中回写queue
					flag = CacheCAS.asynSendMsg(PROCESS_QUEUE, resType, queue);
					if (!flag) {
						if (log.isErrorEnabled()) {
							log.error("[当前失败值为:" + res.getUuid() + "  当前队列大小:" + queue.size() + "]");
						}
					}
				}
				//修改数据库状态和资源开始加工的时间
				if (flag) {	//缓存成功清理
					if(res != null && res.getSendStatus().equals(Constants.kqueue_received)){//待加工的，则将状态置为加工中
						//更新res任务状态
						res.setSendStatus(Constants.kqueue_sended);
						res.setEndTime(Calendar.getInstance().getTime());
						//保证数据的一致性
						//保证事务的一致性
						if (!updateRes(res)) res = null;
					}
				}
				/*
				 * 备注：只有当[flag = CacheCAS.asynSendMsg(PROCESS_QUEUE, resType, queue);]和
				 *[updateRes(res)]都成功才能返回res任务，以保证res任务数据逻辑上的一致性
				*/
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return res;
	}
	public static DataexSysTransTask getDataFromQueueByDocId(String resType,String docId) {
		boolean flag = false;
		DataexSysTransTask res = null;
		try {
			LinkedHashMap<String, String> queue = (LinkedHashMap<String, String>) CacheCAS.asynAcceptMsg(PROCESS_QUEUE, resType);
			if (queue == null || queue.size() == 0) {
				return null;
			}
			if (queue != null) {//从缓存中取数据，并清理缓存
				Iterator<Entry<String, String>> iterator = queue.entrySet().iterator();
				if (iterator.hasNext()) {
					//查找key
					String key = iterator.next().getKey();
					//获取res任务
					res = getResProcessFromMem(key);
					if(docId.equals(res.getContent().getDocId())){
						//从队列中清除res任务ID
						queue.remove(key);
						//从mem中清除res任务对象
						removeResFromMem(key);
						//向mem中回写queue
						flag = CacheCAS.asynSendMsg(PROCESS_QUEUE, resType, queue);
						if (!flag) {
							if (log.isErrorEnabled()) {
								log.error("[当前失败值为:" + res.getUuid() + "  当前队列大小:" + queue.size() + "]");
							}
						}
						if(!flag || !delRes(res)){
							res = null;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return res;
	}
	
	private static boolean updateRes(DataexSysTransTask res) {
		boolean flag = false;
		TransactionCache tx = null;
		try {
			tx = new TransactionCache();
			tx.update(res);
			//提交
			tx.commit();
			flag = true;
		} catch (Exception e) {
			//事务回滚
			if (tx != null) {
				tx.rollback();
			}
			if (log.isErrorEnabled()) {
				log.error(e.getMessage() == null ? "[method:saveDataexSysTransTaskAndLog exception. ]":e.getMessage(), e);
			}
		}
		return flag;
	}
	public static boolean delRes(DataexSysTransTask res) {
		boolean flag = false;
		TransactionCache tx = null;
		try {
			tx = new TransactionCache();
			tx.delete(res);
			//提交
			tx.commit();
			flag = true;
		} catch (Exception e) {
			//事务回滚
			if (tx != null) {
				tx.rollback();
			}
			if (log.isErrorEnabled()) {
				log.error(e.getMessage() == null ? "[method:saveDataexSysTransTaskAndLog exception. ]":e.getMessage(), e);
			}
		}
		return flag;
	}
	
	public static Boolean putResProcessToMem(DataexSysTransTask resProcess) {
		boolean flag = false;
		try {
			String key = CacheUtil.getCacheName(ResProcessObj, resProcess.getUuid());
			MemCachedFactory.buildClient().set(key, resProcess);
			flag = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return flag;
	}
	
	public static boolean removeResFromMem(String id) {
		String key = CacheUtil.getCacheName(ResProcessObj, id);
		return MemCachedFactory.buildClient().delete(key);
	}
	public static DataexSysTransTask getResProcessFromMem(String id){
		DataexSysTransTask res = null;
		if(StringHelper.isEmptyByTrim(id)){
			return null;
		}
		try{
			String key = CacheUtil.getCacheName(ResProcessObj, id);
			res = (DataexSysTransTask) MemCachedFactory.buildClient().get(key);
			//这里要考虑memcached的LRU算法，考虑失效时间的情况
			//失效时从数据库读取任务
			//if (res == null) {
				res = getResProcessFromDBById(id);
			//}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return res;
	}
	

	public static DataexSysTransTask getResProcessFromDBById(String id){
		DataexSysTransTask res = null;
		try{
			res = QueryCache.get(DataexSysTransTask.class, id);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return res;
	}
	
	
}
