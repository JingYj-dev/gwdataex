package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransParam;
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

public class AddDataexSysTransParam extends AdminAction implements ModelDriven {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AddDataexSysTransParam.class);
	private DataexSysTransParam item;
	
	
	public AddDataexSysTransParam() { 
		this.item = new DataexSysTransParam();
	}
	
	protected String adminGo() {	
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getParamName()) || StringHelper.isEmpty(item.getParamValue()) || item.getParamId() == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
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
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));		
			return Action.ERROR;
		}
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return item;
	}
	
	public Boolean check(DataexSysTransParam item) {
		DataexSysTransParam transParam = QueryCache.get(DataexSysTransParam.class, item.getParamId());
		if(transParam == null){
			return true;
		}
		return false;
	}
}
