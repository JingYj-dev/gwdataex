package com.hnjz.apps.base.user.action.login;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.sys.action.SysItem;
import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.apps.base.user.action.UserItem;
import com.hnjz.common.PluginBus;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.webwork.action.AbstractAction;
import com.hnjz.apps.base.user.spi.IUserListener;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public class SSO extends AbstractAction {
	private static Log log = LogFactory.getLog(SSO.class);
	public static final String CONST_SSO_LOGOUT = "_sso_logout_";
	
	private String toAction;
	private String logoutAction;
	public SUser sUser = new SUser();
	public SSO() {}
	public String go() {
		
		sUser =  (SUser) WebBaseUtil.getCurrentUser();
		if(sUser==null)
			sUser = QueryCache.get(SUser.class, sUser.getUuid());
		/*if (sUser == null) {
			setMessage(Messages.getString("systemMsg.loginFaild"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginFaild"));
			return Action.ERROR;
		}
		*/
		//登录成功后，总登录次数加1，失败次数清零
		UserItem.updateTotalLoginCount(sUser);
		String systemId = com.hnjz.core.configuration.ConfigurationManager.getConfigurationManager().getSysConfigure("app.system.id", Environment.CURRENT_SYSTEM_ID);
		if("2".equals(QueryCache.get(SSys.class, systemId).getOpenFlag())){
			if(sUser.getUserType().equals(BaseEnvironment.USERTYPE_NORMAL)){
				String msg= Messages.getString("systemMsg.systemUnavailable");
				setMessage(msg);
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,msg);
				return Action.ERROR;
			}
		}
		
		IUserListener lis=PluginBus.getPlugin(IUserListener.class);
		if(lis!=null){
			lis.afterLogin(sUser);
		}		
		List<SSys> sysList = SysItem.getSystems();
		HashMap sysmap = new HashMap();
		for(SSys sys : sysList){
			sysmap.put(sys.getSysId(), sys);
		}
		set("sysMap", sysmap); 
		//将sUser设置在session中
		set(Environment.SESSION_LOGIN_KEY, sUser);
		//一个userId绑定一个ip
		//set(sUser.getUuid(), ServletActionContext.getRequest().getRemoteHost());		 
		/*if (mod == 2) {
			if (autoLogin != null && autoLogin.intValue() == 1)
				CookieUtil.SetCookies(Environment.Cookie_UserID, DesUtil.encrypt(sUser.getUuid().toString()),
						24 * 60 * 60 * 30, ServletActionContext.getResponse());
			else
				UserItem.clearCookies();
		}*/
		result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, String.valueOf(sUser.getUserType()));
		setMessage(Messages.getString("systemMsg.success"));
		
		
		if(sUser !=null){
			toAction = "explorer.jsp";
		}
		return Action.SUCCESS;
		
	}
	public String getLogoutUrl(){
		String server = this.getSession().getServletContext().getInitParameter("sso.server.service");
		String client = this.getSession().getServletContext().getInitParameter("sso.client.serverName");
//		String server = ConfigurationManager.getConfigurationManager().getSysConfigsso.server.serviceure("sso.server.service", "");
//		String client = ConfigurationManager.getConfigurationManager().getSysConfigure("sso.client.serverName", "");
		if(StringHelper.isNotEmpty(server)){
			server = server.replaceAll("\\\\", "");
			server = server + "/logout";
			HttpServletRequest request = ServletActionContext.getRequest();
			server = server + "?service=" 
				+ client + request.getContextPath(); 
		}
		return server;
	}
	public String getToAction() {
		return toAction;
	}
	public void setToAction(String toAction) {
		this.toAction = toAction;
	}
	public String getLogoutAction() {
		return logoutAction;
	}
	public void setLogoutAction(String logoutAction) {
		this.logoutAction = logoutAction;
	}
}
