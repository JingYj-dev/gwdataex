package com.hnjz.apps.base.user.action.login;

import com.hnjz.apps.base.security.model.SecurityParam;
import com.hnjz.apps.base.security.service.SecurityService;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AbstractAction;
import com.opensymphony.xwork.Action;

public class GetLoginStyle extends AbstractAction{

	protected String go() {
		String loginStyle = SecurityService.getParamValue(SecurityParam.LOGIN_STYLE);
		if("1".equals(loginStyle)){
			result = Ajax.JSONResult(1, "");
			return Action.ERROR;
		}else{
			result = Ajax.JSONResult(0, "");
			return Action.SUCCESS;
		}
	}

}
