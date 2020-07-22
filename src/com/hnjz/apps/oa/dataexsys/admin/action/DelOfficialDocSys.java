package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysAttachment;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

public class DelOfficialDocSys extends AdminAction {
	
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(DelOfficialDocSys.class);
	
	private String uuid;
	private String filePath;
	public DelOfficialDocSys() {
	}
	@Override
	protected String adminGo() {
		if (StringHelper.isEmpty(uuid)) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		DataexSysAttachment attachment = QueryCache.get(DataexSysAttachment.class, uuid);
		if (attachment == null) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.recordnotfound"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			tx = new TransactionCache();
			tx.delete(attachment);
			tx.commit();
			File f = new File(filePath);
			if (f.exists()) {
				f.delete();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage());
			return Action.ERROR;
		} 
		
	}
	
	public String getAttachPath(Integer serverId){
		//String dir = System.getProperty("user.dir");
		if(null == serverId){
			serverId = Integer.parseInt(Constants.RECV_SYS_DIR_ID);
		}
		return AttachItem.filePath(serverId);
	}
	
	public void delAttachFile(String file) throws Exception{
		File f = new File(file);
		if (f.exists())
			f.delete();
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}


