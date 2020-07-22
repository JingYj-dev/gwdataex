/*
 * Created on 2005-7-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hnjz.apps.base.user.action.login;

import com.hnjz.apps.base.common.Constants;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.security.model.SecurityParam;
import com.hnjz.apps.base.security.service.SecurityService;
import com.hnjz.apps.base.user.action.UserItem;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.core.configuration.Environment;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.util.*;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.model.SUserEnvironment;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class Login extends LoginAction {
	private static Log log = LogFactory.getLog(Login.class);
	private String checkCode = null;
	public Login() {
	}
	
	private String getLoginData(){
		//return "{checkCode:'"+checkCode+"',loginName:'"+sUser.getLoginName()+"',password:'"+sUser.getPassword()+"'}"; 
		return "{checkCode:'"+checkCode+"',loginName:'"+sUser.getLoginName()+"',password:'"+sUser.getPassword()+"'}"; 
	}
	public String loginGo() {
		//验证码判断
		/*if (!checkCode.equals((String) ServletActionContext.getRequest().getSession().getAttribute(Environment.System_SessionID))) {
			setMessage(Messages.getString("systemMsg.checkCodeError"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.checkCodeError"));
			logpart(LogConstant.LOG_LEVEL_IMPORTANT,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.checkCodeError"),getLoginData(),"","");
			return Action.ERROR;
		}*/
		if (StringHelper.isEmpty(sUser.getLoginName()) || StringHelper.isEmpty(sUser.getPassword())) {
			setMessage(Messages.getString("systemMsg.fieldEmpty"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			logpart(LogConstant.LOG_LEVEL_IMPORTANT,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.fieldEmpty"),getLoginData(),"","");
			return Action.ERROR;
		}
		return login();
	}
	public String login() {
		mod = 2;
		try {
			SUser user = UserItem.getFailedLoginCount(sUser.getLoginName());
			if(user == null){
				setMessage(Messages.getString("systemMsg.loginFaild"));
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginFaild"));
				logpart(LogConstant.LOG_LEVEL_IMPORTANT,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.loginFaild"),getLoginData(),"","");
				return Action.ERROR;
			}
			set(Environment.SESSION_LOGIN_KEY,user);
			if (!user.getPassword().equals(Md5Util.MD5Encode(sUser.getPassword()))) {
				UserItem.updateFailedLoginCount(user);
//				logpart(LogConstant.LOG_LEVEL_COMMON,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.loginFaild"),Json.object2json(user),user.getUuid());
				String strMaxLogin = null;
				if(!SecurityService.getParamValue(SecurityParam.MAX_LOGIN_FAIL).equals("")){
					strMaxLogin = SecurityService.getParamValue(SecurityParam.MAX_LOGIN_FAIL);
				}else{
					strMaxLogin = SUserEnvironment.STR_MAX_LOGIN;
				}
				int maxLogin = Integer.parseInt(strMaxLogin);
				if(user.getFailedLoginCount() > maxLogin){//登录超过五次后，改变安全状态,不能登录
					UserItem.updateSafeStatus(user);
					setMessage(Messages.getString("systemMsg.loginFaild"));
					logpart(LogConstant.LOG_LEVEL_SEVERE,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.loginFailCount",new String[]{strMaxLogin}),Json.object2json(user),user.getUuid(),user.getLoginName());
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginFailCount",new String[]{strMaxLogin}));
					return Action.ERROR;
				}else{
					setMessage(Messages.getString("systemMsg.loginFaild"));
					logpart(LogConstant.LOG_LEVEL_IMPORTANT,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.loginFaild"),Json.object2json(user),user.getUuid(),user.getLoginName());
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginFaild"));
					return Action.ERROR;
				}
			}
			userId = user.getUuid();
			//一个userId绑定一个ip
			String ip = ServletActionContext.getRequest().getRemoteHost();
			String sessionIp = (String) MemCachedFactory.get(Constants.ACCOUNT_LOGON+userId);
			if(ConfigurationManager.getConfigurationManager().getSysConfigure("app.account.logon", "").equals("true")){
				if(StringHelper.isNotEmpty(sessionIp) && !ip.equals(sessionIp)){
					setMessage(Messages.getString("systemMsg.accountLogon"));
					logpart(LogConstant.LOG_LEVEL_IMPORTANT,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.accountLogon"),Json.object2json(user),user.getUuid(),user.getLoginName());
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.accountLogon"));
					return Action.ERROR;
				}
			}
			MemCachedFactory.set(Constants.ACCOUNT_LOGON+userId,ip);
			if(!user.getOpenFlag().equals(SUserEnvironment.OPEN_FLAG)){//用户账号开启状态才可以登录
				setMessage(Messages.getString("systemMsg.userOpenflagActive"));
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.userOpenflagActive"));
				logpart(LogConstant.LOG_LEVEL_SEVERE,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.userOpenflagActive"),Json.object2json(user),user.getUuid(),user.getLoginName());
				return Action.ERROR;
			}
			if(user.getActiveStatus().equals(SUserEnvironment.ACTIVE_STATUS_CREATE)){//用户刚被创建，未进入待激活状态
				setMessage(Messages.getString("systemMsg.safeAdminActive"));
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.safeAdminActive"),user.getLoginName());
				logpart(LogConstant.LOG_LEVEL_COMMON,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.safeAdminActive"),Json.object2json(user),user.getUuid(),user.getLoginName());
				return Action.ERROR;
			}
			if(user.getActiveStatus().equals(SUserEnvironment.ACTIVE_STATUS_SUB)){//进入待激活，必须在规定的时间内修改密码
				if(user.getActiveDeadLine() == null){
					setMessage(Messages.getString("systemMsg.safeAdminActive"));
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.safeAdminActive"));
					logpart(LogConstant.LOG_LEVEL_COMMON,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.safeAdminActive"),Json.object2json(user),user.getUuid(),user.getLoginName());
					return Action.ERROR;
				}
				long minute = (user.getActiveDeadLine().getTime() - new Date().getTime())/86400;
				if(minute >= 0){
					setMessage(Messages.getString("systemMsg.updPwdActive"));
					result = Ajax.JSONResult( SUserEnvironment.RESULT_CODE_UPDPWD, Messages.getString("systemMsg.updPwdActive"),user.getLoginName());
					logpart(LogConstant.LOG_LEVEL_COMMON,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.updPwdActive"),Json.object2json(user),user.getUuid(),user.getLoginName());
					set(SUserEnvironment.ACTION_UPDPWD, user);
					return SUserEnvironment.ACTION_UPDPWD;
				}else{
					setMessage(Messages.getString("systemMsg.safeAdminActive"));
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.safeAdminActive"));
					logpart(LogConstant.LOG_LEVEL_SEVERE,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.safeAdminActive"),Json.object2json(user),user.getUuid(),user.getLoginName());
					return Action.ERROR;
				}
			}
			
			if(user.getEditPwdTime() == null){//如果修改时间为空，必须进入修改密码页面
				setMessage(Messages.getString("systemMsg.updPassword"));
				result = Ajax.JSONResult( SUserEnvironment.RESULT_CODE_UPDPWD, Messages.getString("systemMsg.updPassword"),user.getLoginName());
				logpart(LogConstant.LOG_LEVEL_IMPORTANT,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.updPassword"),Json.object2json(user),user.getUuid(),user.getLoginName());
				set(SUserEnvironment.ACTION_UPDPWD, user);
				return SUserEnvironment.ACTION_UPDPWD;
			}
			String strOutDays = null;
			//口令过期周期
			if(!SecurityService.getParamValue(SecurityParam.OUTDATE_PASSWORD).equals("")){
				strOutDays = SecurityService.getParamValue(SecurityParam.OUTDATE_PASSWORD);
			}else{
				strOutDays = SUserEnvironment.STR_OUT_DATE;
			}
			int outDate = Integer.parseInt(strOutDays);
			long n = DateUtil.daysInterval(user.getEditPwdTime(),new Date());
			if(n > outDate){//如果修改时间超过一定天数，进入修改密码页面
				setMessage(Messages.getString("systemMsg.updPwd"));
				result = Ajax.JSONResult( SUserEnvironment.RESULT_CODE_UPDPWD, Messages.getString("systemMsg.updPwd",new String[]{strOutDays}));
				logpart(LogConstant.LOG_LEVEL_IMPORTANT,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.updPwd",new String[]{strOutDays}),Json.object2json(user),user.getUuid(),user.getLoginName());
				set(SUserEnvironment.ACTION_UPDPWD, user);
				return SUserEnvironment.ACTION_UPDPWD;
			}
			setMessage(Messages.getString("systemMsg.success"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			
			logpart(LogConstant.LOG_LEVEL_COMMON,LogConstant.RESULT_SUCCESS,Messages.getString("systemMsg.success"),Json.object2json(user),user.getUuid(),user.getLoginName());
			return Action.SUCCESS;
		} catch (Exception ex) {
			setMessage(Messages.getString("systemMsg.exception"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			logpart(LogConstant.LOG_LEVEL_IMPORTANT,LogConstant.RESULT_ERROR,Messages.getString("systemMsg.exception"),getLoginData(),"","");
			return Action.ERROR;
		}
	}
	
	public static void logpart(Integer logLevel,String result,String memo,String logData,String id,String name){
		new LogPart()
		.setLogLevel(logLevel)
		.setOpType(LogConstant.LOG_TYPE_LOGON)
		.setResult(result)
		.setMemo(memo)
		.setLogData(logData)
		.setOpObjType(SUser.class.getName())
		.setOpObjId(id)
		.setRelObjType(SUser.class.getName())
		.setRelObjId(id)
		.setOperId(id)
		.setOperName(name)
		.save();
	}
	
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
}
