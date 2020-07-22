package com.hnjz.apps.base.backup.service;

import com.hnjz.apps.base.backup.model.SDataBackup;
import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBackupService {
	
	private static Log log = LogFactory.getLog(AddBackupService.class);
	private String backupName;
	private String zipMark;
	
	public AddBackupService(){
		
	}
	
	public AddBackupService(String backupName){
		this.backupName = backupName;
	}
	
	public AddBackupService(String backupName,String zipMark){
		this.backupName = backupName;
		this.zipMark = zipMark;
	}
	
	public String addBackup(){
		Date backupStartTime = new Date();
		if(StringHelper.isEmpty(backupName)){
			String mask="yyyyMMdd";
			SDict dict= DictMan.getDictType("d_para_g", "68");
			if(dict!=null){
				mask=dict.getName();
			}
			backupName = "BAK"
				+ new SimpleDateFormat(mask).format(backupStartTime);
		}
		if(StringHelper.isNotEmpty(backupName)){
			Object backupId = new QueryCache("select a.backupId from SDataBackup a where a.backupName = :backupName ")
				.setParameter("backupName", backupName).setMaxResults(1).uniqueResult();
			SDataBackup bak = null;
			if(backupId != null){
				bak = QueryCache.get(SDataBackup.class, backupId.toString());
			}
			if(bak != null && !"3".equals(bak.getBackupStatus())){
				return "systemMsg.bakNameExist";
			}
		}
		String dataBakPath = "\\opt\\dbbackup\\" + backupName + ".bak";
		SDict sDict = DictMan.getDictType("d_para_g", "18");
		if(sDict != null){
			dataBakPath = sDict.getName() + backupName + ".bak";
		}
		String backupStatus = "1";
		String backupCreator = "";
		String backupCreatorName = "";
		
		SDataBackup sdb = new SDataBackup();
		sdb.setBackupStartTime(backupStartTime);
		sdb.setBackupName(backupName);
		sdb.setDataBakPath(dataBakPath);
		sdb.setBackupStatus(backupStatus);
		sdb.setZipMark(zipMark);
		sdb.setBackupCreator(backupCreator);
		sdb.setBackupCreatorName(backupCreatorName);
		TransactionCache tx = null;
		try{
			tx = new TransactionCache();
			tx.save(sdb);
			tx.commit();
			LogPart lp=new LogPart();
			lp.setOpObjType(SDataBackup.class.getName());
			lp.setOpType(LogConstant.LOG_TYPE_ADD);
			lp.setRelObjId("");
			lp.setRelObjType("");
			lp.setLogData(Json.object2json(sdb));
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.setOpObjId(sdb.getBackupId());
			lp.save();
			AddBackupThread addBackupThread = new AddBackupThread(sdb);
			addBackupThread.start();
			return "success";
		}catch (Exception e) {
			if(tx != null)
				tx.rollback();
			log.error(e.getMessage(), e);
			return "systemMsg.exception";
		}
	}

}
