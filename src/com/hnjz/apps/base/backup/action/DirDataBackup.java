package com.hnjz.apps.base.backup.action;

import com.hnjz.apps.base.backup.model.SDataBackup;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.Date;

public class DirDataBackup extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DirDataBackup.class);
	
	private String backupName;
	private Date backupStartTime;
	private Date backupEndTime;
	
	private Page page;
	
	public DirDataBackup(){
		page = new Page();
		page.setCountField("a.backupId");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String adminGo() {
		try {
			if(backupEndTime != null){
				backupEndTime.setDate(backupEndTime.getDate()+1);
			}
			StringBuffer sb = new StringBuffer("select a.backupId from SDataBackup a " 
					+ getWhere() + getOrder());
			QueryCache qc = new QueryCache(sb.toString());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(SDataBackup.class, page.getResults()));
			return Action.SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			setMessage(Messages.getString("systemMsg.exception"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getWhere(){
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if(StringHelper.isNotEmpty(backupName))
			sb.append(" and a.backupName like :backupName ");
		if(backupStartTime != null)
			sb.append(" and a.backupStartTime>=:backupStartTime ");
		if(backupEndTime != null)
			sb.append(" and a.backupEndTime<:backupEndTime");
		return sb.toString();
	}
	
	public String getOrder(){
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by backupStartTime desc ";
	}

	public void setWhere(QueryCache qc) throws ParseException {
		if(StringHelper.isNotEmpty(backupName))
			qc.setParameter("backupName", "%" + backupName.trim() + "%");
		
		if(backupStartTime != null)
			qc.setParameter("backupStartTime", backupStartTime);
		
		if(backupEndTime != null)
			qc.setParameter("backupEndTime", backupEndTime);
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getBackupName() {
		return backupName;
	}

	public void setBackupName(String backupName) {
		this.backupName = backupName;
	}

	public Date getBackupStartTime() {
		return backupStartTime;
	}

	public void setBackupStartTime(Date backupStartTime) {
		this.backupStartTime = backupStartTime;
	}

	@SuppressWarnings("deprecation")
	public Date getBackupEndTime() {
		if(backupEndTime != null)
			backupEndTime.setDate(backupEndTime.getDate()-1);
		return backupEndTime;
	}

	public void setBackupEndTime(Date backupEndTime) {
		this.backupEndTime = backupEndTime;
	}

	
}
