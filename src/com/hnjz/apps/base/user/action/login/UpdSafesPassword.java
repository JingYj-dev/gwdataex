package com.hnjz.apps.base.user.action.login;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Md5Util;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AbstractAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.model.SUserEnvironment;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class UpdSafesPassword extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(UpdSafesPassword.class);
	private String loginName;
	private String password;
	private String newPassword;
	private String repeatNewPassword;
	private String pwdLevel;
	
	public UpdSafesPassword() { 
	}

	protected String go() {
		if(get(SUserEnvironment.ACTION_UPDPWD) == null) return Action.LOGIN;
		TransactionCache tx = null;
		try {
			if(StringHelper.isAnyEmpty(loginName,password,newPassword,repeatNewPassword)){//判空
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			
			/*if(StringHelper.isNotEmpty(pwdLevel)){
				if(Integer.parseInt(pwdLevel) >= 2){
					String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]{10,}$";
					Pattern pattern = Pattern.compile(regex);
			        Matcher  matcher = pattern.matcher(newPassword);
			        if (!matcher.matches()) {//密码格式为10以上母数字的组合
			        	result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.pwdCheckFail"));
						return Action.ERROR;
			        }
				}
				if(Integer.parseInt(pwdLevel) == 1){
					String regex1 = "^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$";
					Pattern pattern1 = Pattern.compile(regex1);
			        Matcher  matcher1 = pattern1.matcher(newPassword);
			        if (!matcher1.matches()) {//密码格式为字母数字的组合
			        	result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.pwdCheckFaillevel"));
						return Action.ERROR;
			        }
				}
			}*/
			String userId = (String) new QueryCache("select a.uuid from SUser a where a.loginName =:loginName and a.password=:password")
				.setParameter("loginName", loginName)
				.setParameter("password", Md5Util.MD5Encode(password)).uniqueResult();
			if (userId == null) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginFaild"));
				return Action.ERROR;
			}
			if(newPassword.trim().equals(password.trim())){//新密码不能与旧密码相同
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.newPasswordAndPasswordCheckFail"));
				return Action.ERROR;
			}
			if(!newPassword.trim().equals(repeatNewPassword.trim())){//新密码与确认密码相同
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.repeatNewPasswordAndNewPasswordCheckFail"));
				return Action.ERROR;
			}
			tx = new TransactionCache();
			SUser suser=QueryCache.get(SUser.class, userId);
			suser.setEditPwdTime(new Date());
			suser.setPassword(Md5Util.MD5Encode(newPassword));
			suser.setActiveStatus("3");
			tx.update(suser);
			tx.commit();
			//remove(SUserEnvironment.ACTION_UPDPWD);
			ActionContext.getContext().getSession().clear();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatNewPassword() {
		return repeatNewPassword;
	}

	public void setRepeatNewPassword(String repeatNewPassword) {
		this.repeatNewPassword = repeatNewPassword;
	}

	public String getPwdLevel() {
		return pwdLevel;
	}

	public void setPwdLevel(String pwdLevel) {
		this.pwdLevel = pwdLevel;
	}
}

