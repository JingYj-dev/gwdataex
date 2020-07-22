package com.hnjz.apps.base.user.action.login;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.func.action.FuncItem;
import com.hnjz.apps.base.sys.action.SysItem;
import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.apps.base.user.action.UserItem;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AbstractAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;

public abstract class LoginAction extends AbstractAction {
	private static Log log = LogFactory.getLog(LoginAction.class);
	public String result;
	public SUser sUser = new SUser();
	public String userId = null;
	public Integer autoLogin = null;
	public int mod = 0;
	public String funcid = "ACL_LOGIN"; // 功能编码
	public LoginAction() {}
	public String go() {
		String sRet = loginGo();
		if (sRet != Action.SUCCESS)
			return sRet;
		sUser = QueryCache.get(SUser.class, userId);
		/*if (sUser == null) {
			setMessage(Messages.getString("systemMsg.loginFaild"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginFaild"));
			return Action.ERROR;
		}
		
		if ("2".equals(sUser.getOpenFlag())) {
			setMessage(Messages.getString("systemMsg.accountCloseError"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.accountCloseError"));
			return Action.ERROR;
		}
		String password = Md5Util.MD5Encode(Environment.DEFAULT_PASSWORD);
		if (password.equalsIgnoreCase(sUser.getPassword())) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.pwdError"));
			setMessage(Messages.getString("systemMsg.pwdError"));
			return Action.ERROR;
		}*/
		//登录成功后，总登录次数加1，失败次数清零
		UserItem.updateTotalLoginCount(userId);
		String systemId = ConfigurationManager.getConfigurationManager().getSysConfigure("app.system.id", Environment.CURRENT_SYSTEM_ID);
		if("2".equals(QueryCache.get(SSys.class, systemId).getOpenFlag())){
			if(sUser.getUserType().equals(BaseEnvironment.USERTYPE_NORMAL)){
				setMessage(Messages.getString(""));
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString(""));
				return Action.ERROR;
			}
		}
		
		//获取功能ID列表
		List<String> funcIdList = FuncItem.getSFuncJoinList(userId);
		//设置用户（userId）在系统（sysId）中所拥有的功能
		sUser.setFunctions(FuncItem.getSFuncIdSet(funcIdList));
		//设置用户（userId）在系统（sysId）中所拥有的功能点
		sUser.setFuncActions(FuncItem.getSFuncActionJoinList(funcIdList));
		
		List<SSys> sysList = SysItem.getSystems();
		HashMap sysmap = new HashMap();
		for(SSys sys : sysList){
			sysmap.put(sys.getSysId(), sys);
		}
		set("sysMap", sysmap);
		
//		if(StringHelper.isEmptyByTrim(sUser.getOrgId())){
//			//XXX 设置用户所属机构ID:暂定用户仅属于某一个机构
//			java.util.List<String> orgs= UserProvider.getOrgs(userId);
//			if(orgs!=null && !orgs.isEmpty()){
//				sUser.setOrgId(orgs.get(0));
//			}
//		}
		//将sUser设置在session中
		set(Environment.SESSION_LOGIN_KEY, sUser);
		//一个userId绑定一个ip
		set(sUser.getUuid(), ServletActionContext.getRequest().getRemoteHost());
		 
		/*if (mod == 2) {
			if (autoLogin != null && autoLogin.intValue() == 1)
				CookieUtil.SetCookies(Environment.Cookie_UserID, DesUtil.encrypt(sUser.getUuid().toString()),
						24 * 60 * 60 * 30, ServletActionContext.getResponse());
			else
				UserItem.clearCookies();
		}*/
		
		result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, String.valueOf(sUser.getUserType()));
		setMessage(Messages.getString("systemMsg.success"));
		return Action.SUCCESS;
	}
	protected abstract String loginGo();

 
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public SUser getSUser() {
		return sUser;
	}
	public void setSUser(SUser user) {
		sUser = user;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getAutoLogin() {
		return autoLogin;
	}
	public void setAutoLogin(Integer autoLogin) {
		this.autoLogin = autoLogin;
	}
	public int getMod() {
		return mod;
	}
	public void setMod(int mod) {
		this.mod = mod;
	}
}
