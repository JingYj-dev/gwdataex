package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.StringHelper;
import com.hnjz.util.Ajax;
import com.hnjz.util.Md5Util;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class UpdPasswordSetting extends UserAction {
	private static Log log = LogFactory.getLog(UpdPasswordSetting.class);
	private String passwordTr;
	private String newPasswordTr; //新登录密码
	private String repeatNewPasswordTr; //重复登录密码
	private String pwdLevel;
	
	public UpdPasswordSetting() {
	}

	@Override
	protected String userGo() {
		SUser sUser = UserProvider.currentUser(); //获取当前用户;
		//SUser sUser = QueryCache.get(SUser.class, currentSUser.getUserId());
		if(sUser == null){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginWebOut"));
			return Action.ERROR;
		}
		if(StringHelper.isEmpty(passwordTr) || StringHelper.isEmpty(newPasswordTr) || StringHelper.isEmpty(repeatNewPasswordTr)){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			if (!sUser.getPassword().equals(Md5Util.MD5Encode(passwordTr))) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginFaild"));
				return Action.ERROR;
			}
			/*if(StringHelper.isNotEmpty(pwdLevel)){
				if(Integer.parseInt(pwdLevel) == 1){
					String regex1 = "^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$";
					Pattern pattern1 = Pattern.compile(regex1);
			        Matcher  matcher1 = pattern1.matcher(newPasswordTr);
			        if (!matcher1.matches()) {//密码登记为1的校验
			        	result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.pwdCheckFaillevel"));
						return Action.ERROR;
			        }
				}
				if(Integer.parseInt(pwdLevel) == 2){
					String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]{10,16}$";
					Pattern pattern = Pattern.compile(regex);
			        Matcher  matcher = pattern.matcher(newPasswordTr);
			        if (!matcher.matches()) {//密码格式为10以上母数字的组合
			        	result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.pwdCheckFail"));
						return Action.ERROR;
			        }
				}
			}*/
			if(passwordTr.equals(newPasswordTr)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.newPasswordAndPasswordCheckFail"));
				return Action.ERROR;
			}
			if(!newPasswordTr.equals(repeatNewPasswordTr)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.repeatNewPasswordAndNewPasswordCheckFail"));
				return Action.ERROR;
			}
			sUser.setPassword(Md5Util.MD5Encode(newPasswordTr));
			sUser.setEditPwdTime(new Date());
			tx = new TransactionCache();
			tx.update(sUser);
			tx.commit();
			set(Environment.SESSION_LOGIN_KEY, sUser);
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public String getPasswordTr() {
		return passwordTr;
	}

	public void setPasswordTr(String passwordTr) {
		this.passwordTr = passwordTr;
	}

	public String getNewPasswordTr() {
		return newPasswordTr;
	}

	public void setNewPasswordTr(String newPasswordTr) {
		this.newPasswordTr = newPasswordTr;
	}

	public String getRepeatNewPasswordTr() {
		return repeatNewPasswordTr;
	}

	public void setRepeatNewPasswordTr(String repeatNewPasswordTr) {
		this.repeatNewPasswordTr = repeatNewPasswordTr;
	}

	public String getPwdLevel() {
		return pwdLevel;
	}

	public void setPwdLevel(String pwdLevel) {
		this.pwdLevel = pwdLevel;
	}
}
