package com.hnjz.apps.base.theme.action;

import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.theme.model.Theme;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetTheme extends AdminAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetTheme.class);
	private Theme item;
	private String uuid;
	public GetTheme(){
		item = new Theme();
	}
	@Override
	protected String adminGo() {
		try {
			if(StringHelper.isNotEmpty(uuid)){
				item = QueryCache.get(Theme.class, uuid);
			} 
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		} 
	}
	public Theme getItem() {
		return item;
	}
	public void setItem(Theme item) {
		this.item = item;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
