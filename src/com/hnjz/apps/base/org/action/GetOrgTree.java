/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetOrgTree.java
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.org.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** 
 * <p>获得组织机构树</p>
 * 
 */
public class GetOrgTree extends AdminAction {
	private static Log log = LogFactory.getLog(GetOrgTree.class);
	private String id;
	public GetOrgTree(){
	}
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
      try {
			result = OrgItem.getSubOrgan(id).toString();
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

}
