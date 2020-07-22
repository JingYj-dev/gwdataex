package com.hnjz.apps.base.user.service;

import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.core.model.ModeFactory;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Md5Util;
import com.hnjz.util.StringHelper;
import com.hnjz.util.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdUserSettingsService {
	private static Log log = LogFactory.getLog(UpdUserSettingsService.class);
	
	public static IServiceResult<String> saveUpdate(SUser item, String newPassword, String newReviewPwd, 
			String repeatNewPassword, String repeatNewReviewPwd, String reviewPwdHidden, String pwdLevel){
		
		IServiceResult<String> res = null;
		String result="";
		SUser updObj = null;
		TransactionCache tx = null;
		
		try {
			
			newPassword = newPassword == null ? "" : newPassword.trim();
			repeatNewPassword = repeatNewPassword == null ? "" : repeatNewPassword.trim();
			newReviewPwd = newReviewPwd == null ? "" : newReviewPwd.trim();
			repeatNewReviewPwd = repeatNewReviewPwd == null ? "" : repeatNewReviewPwd.trim();
			
			updObj = UserProvider.currentUser(); //获取当前用户;
			updObj = QueryCache.get(SUser.class, updObj.getUserId());
			
			String inputPassword = item.getPassword() == null ? "" : item.getPassword().trim(); //从页面获取的密码
			String inputReviewPwd = item.getReviewPwd() == null ? "" : item.getReviewPwd().trim(); //从页面获取的签章密码
			String inputMobile = item.getMobile() == null ? "" : item.getMobile().trim(); //从页面获取的手机号
			String inputPhone = item.getPhone() == null ? "" : item.getPhone().trim(); //从页面获取的座机号
			String inputEmail = item.getEmail() == null ? "" : item.getEmail().trim(); //从页面获取的邮箱
			
			String password = updObj.getPassword();
			String reviewPwd = updObj.getReviewPwd();
			String mobile = updObj.getMobile();
			String phone = updObj.getPhone();
			String email = updObj.getEmail();
			
			inputReviewPwd = StringHelper.isNotEmpty(inputReviewPwd) ? inputReviewPwd.split(",")[0] : "";
			newReviewPwd = StringHelper.isNotEmpty(newReviewPwd) ? newReviewPwd.split(",")[0] : "";
			repeatNewReviewPwd = StringHelper.isNotEmpty(repeatNewReviewPwd) ? repeatNewReviewPwd.split(",")[0] : "";
			
			if (StringHelper.isEmpty(inputPassword) && 
					StringHelper.isEmpty(newPassword) && 
					StringHelper.isEmpty(repeatNewPassword) && 
					StringHelper.isEmpty(reviewPwd) &&
					StringHelper.isEmpty(inputReviewPwd) && 
					StringHelper.isEmpty(newReviewPwd) && 
					StringHelper.isEmpty(repeatNewReviewPwd) &&
					StringHelper.isEmpty(inputMobile) && 
					StringHelper.isEmpty(inputPhone) && 
					StringHelper.isEmpty(inputEmail)) {
				
				res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
						Messages.getString("systemMsg.fieldEmpty"), result);
				return res;
			}
			
			if (StringHelper.isNotEmpty(inputPassword) && StringHelper.isNotEmpty(newPassword) && StringHelper.isNotEmpty(repeatNewPassword)) {
				if (!Md5Util.MD5Encode(inputPassword).equals(password)) {
					res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
							Messages.getString("systemMsg.passwordCheckFail"), result);
					return res;
				}
				if (inputPassword.equals(newPassword)) {
					res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
							Messages.getString("systemMsg.newPasswordAndPasswordCheckFail"), result);
					return res;
				}
				if (!newPassword.equals(repeatNewPassword)) {
					res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
							Messages.getString("systemMsg.repeatNewPasswordAndNewPasswordCheckFail"), result);
					return res;
				}
				if(StringHelper.isNotEmpty(pwdLevel)){
					if(Integer.parseInt(pwdLevel) == 1){
						String regex1 = "^[A-Za-z0-9]+$";
						Pattern pattern1 = Pattern.compile(regex1);
				        Matcher  matcher1 = pattern1.matcher(newPassword);
				        if (!matcher1.matches()) {//密码格式为字母数字的组合
				        	res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
									Messages.getString("systemMsg.pwdCheckFaillevel1"), result);
							return res;
				        }
					}
				}
				if(StringHelper.isNotEmpty(pwdLevel)){
					if(Integer.parseInt(pwdLevel) >= 2){
						String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{10,}$";
						Pattern pattern = Pattern.compile(regex);
				        Matcher  matcher = pattern.matcher(newPassword);
				        if (!matcher.matches()) {//密码格式为10以上母数字的组合
				        	res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
									Messages.getString("systemMsg.pwdCheckFail"), result);
							return res;
				        }
					}
				}
				updObj.setEditPwdTime(new Date());
				updObj.setPassword(Md5Util.MD5Encode(newPassword));
			} else if (StringHelper.isNotEmpty(inputPassword) || StringHelper.isNotEmpty(newPassword) || StringHelper.isNotEmpty(repeatNewPassword)) {
				res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
						Messages.getString("systemMsg.infoIsNotWhole"), result);
				return res;
			}
			
			if (StringHelper.isNotEmpty(reviewPwdHidden)) { //修改签章密码
				if (StringHelper.isNotEmpty(inputReviewPwd) && StringHelper.isNotEmpty(newReviewPwd) && StringHelper.isNotEmpty(repeatNewReviewPwd)) {
					if (!Md5Util.MD5Encode(inputReviewPwd).equals(reviewPwd)) {
						res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
								Messages.getString("systemMsg.reviewPwdCheckFail"), result);
						return res;
					}
					if (inputReviewPwd.equals(newReviewPwd)) {
						res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
								Messages.getString("systemMsg.newReviewPwdAndReviewPwdCheckFail"), result);
						return res;
					}
					if (!newReviewPwd.equals(repeatNewReviewPwd)) {
						res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
								Messages.getString("systemMsg.repeatNewRepeatPwdAndNewRepeatPwdCheckFail"), result);
						return res;
					}
					updObj.setReviewPwd(Md5Util.MD5Encode(newReviewPwd));
				} else if (StringHelper.isNotEmpty(inputReviewPwd) || StringHelper.isNotEmpty(newReviewPwd) || StringHelper.isNotEmpty(repeatNewReviewPwd)) {
					res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
							Messages.getString("systemMsg.infoIsNotWhole"), result);
					return res;
				}
			} else { //新设置签章密码
				if (StringHelper.isNotEmpty(newReviewPwd) && StringHelper.isNotEmpty(repeatNewReviewPwd)) {
					
					if (!newReviewPwd.equals(repeatNewReviewPwd)) {
						res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
								Messages.getString("systemMsg.repeatNewRepeatPwdAndNewRepeatPwdCheckFail"), result);
						return res;
					}
					updObj.setReviewPwd(Md5Util.MD5Encode(newReviewPwd));
				} else if (StringHelper.isNotEmpty(newReviewPwd) || StringHelper.isNotEmpty(repeatNewReviewPwd)) {
					res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
							Messages.getString("systemMsg.infoIsNotWhole"), result);
					return res;
				}
			}
			
			if (StringHelper.isNotEmpty(inputMobile) && !inputMobile.equals(mobile)) {
				String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$" ;
				Pattern pattern = Pattern.compile(regex);
		        Matcher  matcher = pattern.matcher(inputMobile);
		        if (!matcher.matches()) {
		        	res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
		        			Messages.getString("systemMsg.mobileCheckFail"), result);
					return res;
		        }
		        updObj.setMobile(inputMobile);
			} else if (inputMobile.equals("")) {
				updObj.setMobile(inputMobile);
			}
			if (!inputPhone.equals(phone)) {
				updObj.setPhone(inputPhone);
			}
			if (StringHelper.isNotEmpty(inputEmail) && !inputEmail.equals(email)) {
				String regex = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
				Pattern pattern = Pattern.compile(regex);
		        Matcher  matcher = pattern.matcher(inputEmail);
		        if (!matcher.matches()) {
		        	res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
		        			Messages.getString("systemMsg.emailCheckFail"), result);
					return res;
		        }
		        updObj.setEmail(inputEmail);
			} else if (inputEmail.equals("")) {
				updObj.setEmail(inputEmail);
			}
			
			tx = new TransactionCache();
			tx.update(updObj);
			tx.commit();
			
			res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_OK, 
					Messages.getString("systemMsg.success"), result);
			return res;
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, 
					Messages.getString("systemMsg.exception"), result);
			log.error(ex.getMessage(), ex);
			return res;
		}
	}
}
