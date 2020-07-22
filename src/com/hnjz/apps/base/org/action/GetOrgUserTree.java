/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetOrgPostTree.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.org.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.user.action.GetUserPostTree;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * <p>获得岗位功能</p>
 * 
 */
public class GetOrgUserTree extends AdminAction {
	private static Log log = LogFactory.getLog(GetUserPostTree.class);
	private String no;
	public GetOrgUserTree(){
	}
	
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
		try {
			result = OrgItem.getOrgUserTree().toString();
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

}
