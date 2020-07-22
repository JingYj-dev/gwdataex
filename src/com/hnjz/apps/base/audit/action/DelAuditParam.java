package com.hnjz.apps.base.audit.action;

import com.hnjz.apps.base.audit.model.AuditParam;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelAuditParam extends AdminAction {
	private static Log log = LogFactory.getLog(DelAuditParam.class);
	private String ids;
	public DelAuditParam(){
	}
	@Override
	protected String adminGo(){
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List listIds = StringHelper.strToList(ids);
			List<AuditParam> postList = QueryCache.idToObj(AuditParam.class, listIds);
			tx = new TransactionCache();
			tx.delete(postList);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
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
