package com.hnjz.apps.base.menu.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMenuTree extends AdminAction{

	private static Log log = LogFactory.getLog(GetMenuTree.class);
	private String id;
	
	public GetMenuTree() {
		super();
	}

	@Override
	protected String adminGo() {
		try {
			result = MenuItem.getSubMenu(id).toString();
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
