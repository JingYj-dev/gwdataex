package com.hnjz.apps.base.user.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetUserRoleTree extends AdminAction{
	private static Log log = LogFactory.getLog(GetUserRoleTree.class);
	private String userId;
	
	public GetUserRoleTree() {
	}

	@Override
	protected String adminGo() {
		try {
			if(StringHelper.isNotEmpty(userId)){
				result = UserItem.getUserRoleTree(userId).toString();
			}
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	
}
