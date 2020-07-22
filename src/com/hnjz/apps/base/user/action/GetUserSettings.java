package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.apps.base.security.model.SecurityParam;
import com.hnjz.apps.base.security.service.SecurityService;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 跳转到设置页面
 * 
 * @author mazhh
 * @version 1.0
 */
public class GetUserSettings extends UserAction {
	
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(GetUserSettings.class);
	private SUser item;
	private String pwdLevel;
	private String pwdLevelt;
	
	public GetUserSettings() {
	}

	public String userGo() {
		try {
			item = QueryCache.get(SUser.class, UserProvider.currentUser().getUserId());
			pwdLevelt=SecurityService.getParamValue(SecurityParam.PASSWORD_LEVEAL);
			if(!pwdLevelt.isEmpty()){
				pwdLevel = pwdLevelt;
			}else{
				pwdLevel="3";
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public SUser getItem() {
		return item;
	}

	public void setItem(SUser item) {
		this.item = item;
	}

	public String getPwdLevel() {
		return pwdLevel;
	}

	public void setPwdLevel(String pwdLevel) {
		this.pwdLevel = pwdLevel;
	}

}
