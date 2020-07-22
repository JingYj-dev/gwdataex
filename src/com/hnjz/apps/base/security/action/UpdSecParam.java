package com.hnjz.apps.base.security.action;

import com.hnjz.apps.base.security.model.SecParam;
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

public class UpdSecParam extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdSecParam.class);
	private SecParam item = null;
	
	public UpdSecParam(){
		item = new SecParam();
	}
	@Override
	protected String adminGo() {
		if (StringHelper.isEmpty(item.getParamValue())||StringHelper.isEmpty(item.getParamName())|| item.getParamId() == null) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			SecParam oldItem = QueryCache.get(SecParam.class, item.getParamId());
			if(oldItem == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.recordnotfound"));				
				return Action.ERROR;
			}
			oldItem.setParamName(item.getParamName());
			oldItem.setParamValue(item.getParamValue());
			oldItem.setParamMemo(item.getParamMemo());
			tx = new TransactionCache();
			tx.update(oldItem);
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
	
	public Object getModel() {
		return item;
	}

}
