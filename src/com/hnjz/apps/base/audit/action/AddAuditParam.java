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
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddAuditParam extends AdminAction implements ModelDriven {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AddAuditParam.class);
	private AuditParam item;
	
	
	public AddAuditParam() { 
		this.item = new AuditParam();
	}
	
	protected String adminGo() {
		try {
			if(StringHelper.isEmpty(item.getParamName())||StringHelper.isEmpty(item.getParamValue()) || item.getParamId().equals(null)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}	
			TransactionCache tx = null;
			if(!check(item)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.secParamAddField"));
				return Action.ERROR;
			}
			tx = new TransactionCache();
			tx.save(item);
			tx.commit();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.getParamId());
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));		
			return Action.ERROR;
		}
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return item;
	}
	
	public Boolean check(AuditParam item){
		AuditParam sParm = QueryCache.get(AuditParam.class, item.getParamId());
		if(sParm == null){
			return true;
		}
		return false;
	}
}
