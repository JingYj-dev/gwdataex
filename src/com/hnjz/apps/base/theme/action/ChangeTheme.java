package com.hnjz.apps.base.theme.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.util.CookieUtil;
import com.hnjz.webbase.webwork.action.UserAction;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.util.Messages;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ChangeTheme extends UserAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ChangeTheme.class);
	private String code;
	public	ChangeTheme(){}
	@Override
	protected String userGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(code)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.ThemeCodeEmpty"));
				return Action.ERROR;
			}
			//SUser user = UserProvider.getUser(sUser.getUserId());
			SUser user = (SUser)get(Environment.SESSION_LOGIN_KEY);
			user.setSkinId(code);
			tx = new TransactionCache();
			tx.update(user);
			tx.commit();
			//SUser user = 
			//用户信息放入session
			set(Environment.SESSION_LOGIN_KEY, user);
			//用户登陆皮肤信息存入cookie
			CookieUtil.SetCookies("skin", code,60*60*24*365, ServletActionContext.getResponse());
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS,Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		}catch(Exception ex){
			if(tx != null)
				tx.rollback();
			log.error(ex.getMessage(),ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
