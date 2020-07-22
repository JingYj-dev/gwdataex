package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.StringHelper;
import com.hnjz.util.Ajax;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdPersonalSetting extends UserAction {

	private static Log log = LogFactory.getLog(UpdPersonalSetting.class);
	private String mobile;
	private String phone;
	private String email;
	
	public UpdPersonalSetting() {
	}
	
	@Override
	protected String userGo() {
		SUser sUser = UserProvider.currentUser(); //获取当前用户;
		//SUser sUser = QueryCache.get(SUser.class, currentSUser.getUserId());
		if(sUser == null){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginWebOut"));
			return Action.ERROR;
		}
		/*if(StringHelper.isEmpty(mobile) || StringHelper.isEmpty(phone) || StringHelper.isEmpty(email)){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}*/
		TransactionCache tx = null;
		try {
			if(StringHelper.isNotEmpty(mobile)){
				String regex = "^(((1[0-9]{2})|(15[0-9]{1}))+[0-9]{8})$" ;
				Pattern pattern = Pattern.compile(regex);
			    Matcher  matcher = pattern.matcher(mobile);
			       if (!matcher.matches()) {
			    	   result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.mobileCheckFail"));
			    	   return Action.ERROR;
			       }
			}
			if(StringHelper.isNotEmpty(email)){
				String regexEmail = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
			    Pattern patternEmail = Pattern.compile(regexEmail);
			    Matcher  matcherEmail = patternEmail.matcher(email);
			    	if (!matcherEmail.matches()) {
			    		result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.emailCheckFail"));
			    		return Action.ERROR;
			    }
			}
		    if(StringHelper.isNotEmpty(phone)){
		    	String regexPhone = "^[0-9]{3,4}-?[0-9]{7,9}$";
				Pattern patternPhone = Pattern.compile(regexPhone);
				Matcher  matcherPhone = patternPhone.matcher(phone);
				    if (!matcherPhone.matches()) {
				    	result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.phoneCheckFail"));
				    	return Action.ERROR;
				 }
		    }
		    sUser.setEmail(email);
		    sUser.setPhone(phone);
		    sUser.setMobile(mobile);
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
