/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetRoleFuncTree.java
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.role.action;

import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>获得角色功能</p>
 * 
 */
public class GetRoleFuncTree extends AdminAction {
	private static Log log = LogFactory.getLog(GetRoleFuncTree.class);
	private String roleId;
	
	public GetRoleFuncTree() {
	
	}
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
		try {
			if(StringHelper.isNotEmpty(roleId)){
				SRole role = (SRole) QueryCache.get(SRole.class, roleId);
				if(StringHelper.isNotEmpty(role.getSysId())){
					String sysId = role.getSysId();
					JSONArray ja = RoleItem.getFunc(role);
//					JSONArray ja = RoleItem.getSubFunc(sysId, roleId);
					if(ja != null && ja.size()!=0){
						result = ja.toString();
					}else{
						setMessage(Messages.getString("systemMsg.NoAddRoleFunc"));
						return Action.ERROR;	
					}
				}
			}
			if(StringHelper.isEmpty(result))
				result = "[]";
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
