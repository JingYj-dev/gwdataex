package com.hnjz.apps.base.log.service;

import com.hnjz.apps.base.log.model.SLog;
import com.hnjz.db.query.TransactionCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddLogThread extends Thread {
	private static Log log = LogFactory.getLog(AddLogThread.class);
	private SLog sLog = null;
	public AddLogThread(SLog sLog) {
		this.sLog = sLog;
	}
	
	public void run() {
 		addLog(sLog);
	}
	
	/**
	 * 添加日志
	 * @author liuzhb on Jan 6, 2014 4:59:48 PM
	 * @param sLog
	 * @return
	 */
	public static boolean addLog(SLog sLog) {
		TransactionCache tx = null;
		try {
			//采用sql语句进行插入，便于分表
			//			String table = getTable();
			//			tx = QueryLog.getTransaction();
			//			Query q=tx.getSession().createSQLQuery( "INSERT INTO  " + table + " (uuid, operId, operName, operIp, operType, content, logLevel, logDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			//			q.setString(0, sLog.getUuid());
			//			q.setString(1, sLog.getOperId());
			//			q.setString(2, sLog.getOperName());
			//			q.setString(3, sLog.getOperIp());
			//			q.setInteger(4, sLog.getOperType());
			//			q.setString(5, sLog.getContent());
			//			q.setInteger(6, sLog.getLogLevel());
			//			q.setDate(7, sLog.getLogDate());
			//			q.executeUpdate();
			//			tx.commit();
			
			//采用hql插入
			tx = new QueryLog().getTransaction();
			tx.getSession().save(sLog);
			tx.commit();
			return true;
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			log.error(ex.getMessage(), ex);
			return false;
		} 
	}
}
