package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.model.SAlertSetting;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetSAlertSetting extends AdminAction implements ModelDriven{

	private static Log log = LogFactory.getLog(GetSAlertSetting.class);
	private SAlertSetting item;
	
	public GetSAlertSetting() {
		this.item = new SAlertSetting();
	}

	@Override
	protected String adminGo() {
		try{
			if(StringHelper.isNotEmpty(item.getSetId())){
				item = QueryCache.get(SAlertSetting.class, item.getSetId());
			}
			return Action.SUCCESS;
		}catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			setMessage(msg);
			return Action.ERROR;
		}
	}

	@Override
	public Object getModel() {
		return item;
	}

	public SAlertSetting getItem() {
		return item;
	}

	public void setItem(SAlertSetting item) {
		this.item = item;
	}
}
