/*
 * Created on 2005-7-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hnjz.apps.base.user.action.login;

import com.hnjz.apps.base.common.Constants;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.user.action.UserItem;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.IUser;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.util.Json;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.webwork.action.AbstractAction;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class Quit extends AbstractAction {
	private static Log log = LogFactory.getLog(Quit.class);
	private String toUrl = "";
	public String go() {
		try {
			IUser user = WebBaseUtil.getCurrentUser();
			if(user!=null){
				MemCachedFactory.delete(Constants.ACCOUNT_LOGON+user.getUserId());
			}
			ActionContext.getContext().getSession().clear();
			ServletActionContext.getRequest().getSession().removeAttribute(Environment.SESSION_LOGIN_KEY);
			UserItem.clearCookies();
			if(user!=null){
			new LogPart()
			.setLogLevel(LogConstant.LOG_LEVEL_COMMON)
			.setOpType(LogConstant.LOG_TYPE_LOGOFF)
			.setResult(LogConstant.RESULT_SUCCESS)
			.setMemo(null)
			.setLogData(Json.object2json(user))
			.setOpObjType(SUser.class.getName())
			.setOpObjId(user.getUserId())
			.setRelObjType(SUser.class.getName())
			.setRelObjId(user.getUserId())
			.setOperId(user.getUserId())
			.setOperName(user.getLoginName())
			.save();
			}
			return SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	public String getToUrl() {
		return toUrl;
	}
	public void setToUrl(String toUrl) {
		this.toUrl = toUrl;
	}
}
