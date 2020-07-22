package com.hnjz.apps.base.user.action;

import com.hnjz.core.model.IServiceResult;
import com.hnjz.util.Ajax;
import com.hnjz.apps.base.user.service.UpdUserSettingsService;
import com.hnjz.apps.base.user.model.SUser;

/**
 * 保存设置
 * 
 * @author mazhh
 * @version 1.0
 */
public class UpdUserSettings extends UserAction {
	private static final long serialVersionUID = 1L;
	private SUser item;
	private String newPassword; //新登录密码
	private String repeatNewPassword; //重复登录密码
	private String newReviewPwd; //新签章密码
	private String repeatNewReviewPwd; //重复签章密码
	private String pwdLevel;
	
	private String reviewPwdHidden; //原有签章密码(用于判断是否是新设置签章密码)
	
	public UpdUserSettings() {
		item = new SUser();
	}

	public String userGo() {
		IServiceResult<String> res = UpdUserSettingsService.saveUpdate(item, newPassword, newReviewPwd, repeatNewPassword, repeatNewReviewPwd, reviewPwdHidden,pwdLevel);
		result = Ajax.JSONResult(res.getResultCode(), res.getResultDesc());
		return res.toActionResult();
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	public String getNewReviewPwd() {
		return newReviewPwd;
	}

	public void setNewReviewPwd(String newReviewPwd) {
		this.newReviewPwd = newReviewPwd;
	}

	public String getRepeatNewPassword() {
		return repeatNewPassword;
	}

	public void setRepeatNewPassword(String repeatNewPassword) {
		this.repeatNewPassword = repeatNewPassword;
	}

	public String getRepeatNewReviewPwd() {
		return repeatNewReviewPwd;
	}

	public void setRepeatNewReviewPwd(String repeatNewReviewPwd) {
		this.repeatNewReviewPwd = repeatNewReviewPwd;
	}

	public SUser getItem() {
		return item;
	}

	public void setItem(SUser item) {
		this.item = item;
	}

	public String getReviewPwdHidden() {
		return reviewPwdHidden;
	}

	public void setReviewPwdHidden(String reviewPwdHidden) {
		this.reviewPwdHidden = reviewPwdHidden;
	}

	public String getPwdLevel() {
		return pwdLevel;
	}

	public void setPwdLevel(String pwdLevel) {
		this.pwdLevel = pwdLevel;
	}
}