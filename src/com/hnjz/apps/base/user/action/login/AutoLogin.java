/*
 * Created on 2006-6-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hnjz.apps.base.user.action.login;

import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.DesUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.util.CookieUtil;
import com.hnjz.util.Messages;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class AutoLogin extends LoginAction {
	private static Log log = LogFactory.getLog(AutoLogin.class);
	public AutoLogin() {
	}
	public String loginGo() {
		try{
			mod = 1;
			String cookieUserId = CookieUtil.GetCookies(Environment.Cookie_UserID, ServletActionContext.getRequest());
			if (StringHelper.isEmpty(cookieUserId) || cookieUserId.equals("0")) {
				setMessage(Messages.getString("systemMsg.loginFaild"));
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginFaild"));
				return Action.ERROR;
			}
			userId = DesUtil.decrypt(cookieUserId);
			setMessage(Messages.getString("systemMsg.success"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			setMessage(Messages.getString("systemMsg.exception"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
}
