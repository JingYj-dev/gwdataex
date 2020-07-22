package com.hnjz.apps.base.user.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetUserRole extends AdminAction{
	private static Log log = LogFactory.getLog(GetUserRole.class);
	private String userId;
	private SUser item;
	private boolean Flag;
	public GetUserRole() {
	}
	@Override
	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(userId)) {
				item = QueryCache.get(SUser.class, userId);
			}
			if(Flag == false){
				setMessage(Messages.getString("systemMsg.NoAddUserRole"));
				return Action.ERROR;	
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
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
	public SUser getItem() {
		return item;
	}
	public void setItem(SUser item) {
		this.item = item;
	}
	public boolean isFlag() {
		return Flag;
	}
	public void setFlag(boolean flag) {
		Flag = flag;
	}
	
}
