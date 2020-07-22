/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetFuncTree.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.func.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>获得树</p>
 * 
 */
public class GetFuncTree extends AdminAction {
	private static Log log = LogFactory.getLog(GetFuncTree.class);
	private String id;
	private String funcId;
	public GetFuncTree(){
	}
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
		try {
			result = FuncItem.getSubFuncMenu(id,funcId).toString().toString();
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

}
