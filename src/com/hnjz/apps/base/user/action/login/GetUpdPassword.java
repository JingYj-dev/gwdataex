package com.hnjz.apps.base.user.action.login;

import com.hnjz.apps.base.security.model.SecurityParam;
import com.hnjz.apps.base.security.service.SecurityService;
import com.hnjz.webbase.webwork.action.AbstractAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.model.SUserEnvironment;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetUpdPassword extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetUpdPassword.class);
	private SUser item;
	private String pwdLevel;

	public GetUpdPassword() {
		this.item = new SUser();
	}

	@Override
	protected String go() {
		if(get(SUserEnvironment.ACTION_UPDPWD) == null) return Action.LOGIN;
		try{
			if(SecurityService.getParamValue(SecurityParam.PASSWORD_LEVEAL).isEmpty()){
				pwdLevel = "3";
			}else{
				pwdLevel=SecurityService.getParamValue(SecurityParam.PASSWORD_LEVEAL);
			}
			return Action.SUCCESS;
		}catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			setMessage(msg);
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
