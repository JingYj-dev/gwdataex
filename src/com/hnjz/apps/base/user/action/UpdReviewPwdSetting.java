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

public class UpdReviewPwdSetting extends UserAction {
	private static Log log = LogFactory.getLog(UpdReviewPwdSetting.class);
	private String reviewPwdTr;
	private String newReviewPwdTr;
	private String repeatNewReviewPwdTr;
	private String pwdLevel;
	private String reviewPwdHidden;
	public UpdReviewPwdSetting() {
		super();
	}

	@Override
	protected String userGo() {
		// TODO Auto-generated method stub
		SUser sUser = UserProvider.currentUser(); //获取当前用户;
		//SUser sUser = QueryCache.get(SUser.class, currentSUser.getUserId());
		if(sUser == null){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.loginWebOut"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			if(StringHelper.isNotEmpty(reviewPwdHidden)){
				if(StringHelper.isEmpty(reviewPwdTr) || StringHelper.isEmpty(newReviewPwdTr) || StringHelper.isEmpty(repeatNewReviewPwdTr)){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
					return Action.ERROR;
				}
				if (!sUser.getReviewPwd().equals(Md5Util.MD5Encode(reviewPwdTr))) {
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.reviewFaild"));
					return Action.ERROR;
				}
				if(reviewPwdTr.equals(newReviewPwdTr)){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.newPasswordAndPasswordCheckFail"));
					return Action.ERROR;
				}
			}else{
				if(StringHelper.isEmpty(newReviewPwdTr) || StringHelper.isEmpty(repeatNewReviewPwdTr)){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
					return Action.ERROR;
				}
			}
			
			/*if(StringHelper.isNotEmpty(pwdLevel)){
				if(Integer.parseInt(pwdLevel) == 1){
					String regex1 = "^[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]+$";
					Pattern pattern1 = Pattern.compile(regex1);
			        Matcher  matcher1 = pattern1.matcher(newReviewPwdTr);
			        if (!matcher1.matches()) {
			        	result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.pwdCheckFaillevel"));
						return Action.ERROR;
			        }
				}
				if(Integer.parseInt(pwdLevel) == 2){
					String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[A-Za-z0-9~`!@#$%^&*()_+\'{}<>?:,.;-=\"]{10,}$";
					Pattern pattern = Pattern.compile(regex);
			        Matcher  matcher = pattern.matcher(newReviewPwdTr);
			        if (!matcher.matches()) {
			        	result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.pwdCheckFail"));
						return Action.ERROR;
			        }
				}
			}*/
			
			if(!newReviewPwdTr.equals(repeatNewReviewPwdTr)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.repeatNewPasswordAndNewPasswordCheckFail"));
				return Action.ERROR;
			}
			sUser.setReviewPwd(Md5Util.MD5Encode(newReviewPwdTr));
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

	public String getReviewPwdTr() {
		return reviewPwdTr;
	}

	public void setReviewPwdTr(String reviewPwdTr) {
		this.reviewPwdTr = reviewPwdTr;
	}

	public String getNewReviewPwdTr() {
		return newReviewPwdTr;
	}

	public void setNewReviewPwdTr(String newReviewPwdTr) {
		this.newReviewPwdTr = newReviewPwdTr;
	}

	public String getRepeatNewReviewPwdTr() {
		return repeatNewReviewPwdTr;
	}

	public void setRepeatNewReviewPwdTr(String repeatNewReviewPwdTr) {
		this.repeatNewReviewPwdTr = repeatNewReviewPwdTr;
	}

	public String getPwdLevel() {
		return pwdLevel;
	}

	public void setPwdLevel(String pwdLevel) {
		this.pwdLevel = pwdLevel;
	}

	public String getReviewPwdHidden() {
		return reviewPwdHidden;
	}

	public void setReviewPwdHidden(String reviewPwdHidden) {
		this.reviewPwdHidden = reviewPwdHidden;
	}

}
