package com.hnjz.apps.base.backup.service;


import com.hnjz.apps.base.backup.model.SDataBackup;
import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.db.query.TransactionCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.MessageFormat;
import java.util.Date;

public class AddBackupThread extends Thread {

	private static Log log = LogFactory.getLog(AddBackupThread.class);

	private SDataBackup sdb;

	public AddBackupThread(SDataBackup sdb) {
		this.sdb = sdb;
	}

	public void run() {
		addBackup(sdb);
	}

	public boolean addBackup(SDataBackup sdb) {
		TransactionCache tx = null;
		try {
			String sql="backup database btree to {0} bakfile {1} ";
			SDict sqlTmp= DictMan.getDictType("d_para_g","19");
			if(sqlTmp!=null){
				sql=sqlTmp.getName();
			}
			sql=MessageFormat.format(sql, sdb.getBackupName(),"'"+sdb.getDataBakPath()+"'");
			sql+=("2".equals(sdb.getZipMark())?";":" COMPRESSED;");		 
			tx = new TransactionCache();
			tx.executeUpdateSQL(sql);
			tx.commit();
			sdb.setBackupStatus("2");
			sdb.setBackupEndTime(new Date());
			return true;
		} catch (Exception e) {
			if(tx!=null)
				tx.rollback();
			sdb.setBackupStatus("3");
			sdb.setBackupEndTime(new Date());
			log.error(e.getMessage(), e);
			return false;
		} finally {
			new UpdateBackupService().updateBackup(sdb);
		}
	}
}
