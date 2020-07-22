package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.model.SAlertSetting;
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

public class UpdSAlertSetting extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdSAlertSetting.class);
	private SAlertSetting item = null;
	
	public UpdSAlertSetting(){
		item = new SAlertSetting();
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getAlertType())||StringHelper.isEmpty(item.getEventType()) || StringHelper.isEmpty(item.getSeverLevel())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}	
			SAlertSetting oldItem = QueryCache.get(SAlertSetting.class, item.getSetId());
			if(oldItem == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.recordnotfound"));				
				return Action.ERROR;
			}
			oldItem.setAlertType(item.getAlertType());
			oldItem.setEventType(item.getEventType());
			oldItem.setSeverLevel(item.getSeverLevel());
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
