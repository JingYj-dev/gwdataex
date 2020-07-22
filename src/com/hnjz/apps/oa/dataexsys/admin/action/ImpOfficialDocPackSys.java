/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file AddSysAction.java creation date: [2014-1-8 09:30:20] by mazhh
 * http://www.css.com.cn
 */
package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.webbase.webwork.action.AdminAction;
import com.opensymphony.xwork.Action;

/**
 * <p>打开导入公文包页面</p>
 * @author CSS
 *
 */
public class ImpOfficialDocPackSys extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	private final String funcid = "OA_DATAEXSYS_SYSIMPOFFICIALDOCPACK";
	
	public ImpOfficialDocPackSys() { 
		setFuncid(funcid);
	}
	
	protected String adminGo() {
		return Action.SUCCESS;
	}

}
