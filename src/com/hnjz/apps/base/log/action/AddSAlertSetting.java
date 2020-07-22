package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.model.SAlertSetting;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddSAlertSetting extends AdminAction implements ModelDriven {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AddSAlertSetting.class);
	private SAlertSetting item;
	
	public AddSAlertSetting() { 
		this.item = new SAlertSetting();
	}
	
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getAlertType())||StringHelper.isEmpty(item.getEventType()) || StringHelper.isEmpty(item.getSeverLevel())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}	
			tx = new TransactionCache();
			item.setSetId(com.hnjz.util.UuidUtil.getUuid());
			tx.save(item);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.getSetId());
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
}
