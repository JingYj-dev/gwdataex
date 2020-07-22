package com.hnjz.apps.base.backup.action;

import com.hnjz.apps.base.backup.model.SDataBackup;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelDataBackup extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DelDataBackup.class);
	private String ids;

	@SuppressWarnings("unchecked")
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;
			}
			List<String> listIds = StringHelper.strToList(ids);
			List<SDataBackup> bakList = QueryCache.idToObj(SDataBackup.class, listIds);
			tx = new TransactionCache();
			for(SDataBackup bak : bakList){
				tx.delete(bak);
			}
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			for(SDataBackup bak : bakList){
				LogPart lp=new LogPart();
				lp.setOpObjType(SDataBackup.class.getName());
				lp.setOpType(LogConstant.LOG_TYPE_DELETE);
				lp.setRelObjId("");
				lp.setRelObjType("");
				lp.setLogData(Json.object2json(bak));
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.setOpObjId(bak.getBackupId());
				lp.save();
			}
			return Action.SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	

}
