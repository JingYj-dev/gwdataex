package com.hnjz.apps.base.backup.action;


import com.hnjz.apps.base.backup.service.AddBackupService;
import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddDataBackup extends AdminAction {
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(AddDataBackup.class);
	
	private String backupName;
	private String zipMark;

	@Override
	protected String adminGo() {
		try{
			String strResult = new AddBackupService(backupName, zipMark).addBackup();
			if("success".equals(strResult)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
				return Action.SUCCESS;
			} else {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString(strResult));
				return Action.ERROR;
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			setMessage(Messages.getString("systemMsg.exception"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public String getBackupName() {
		return backupName;
	}

	public void setBackupName(String backupName) {
		this.backupName = backupName;
	}

	public String getZipMark() {
		return zipMark;
	}

	public void setZipMark(String zipMark) {
		this.zipMark = zipMark;
	}
	
	

}
