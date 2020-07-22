/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirRoleAction.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.role.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>查询角色分页列表</p>
 * 
 */
public class DirRole extends AdminAction {
	private static Log log = LogFactory.getLog(DirRole.class);
	private String roleName;
	private String sysId;
	private String openFlag;
	private String roleType;
	private Page page;
	public DirRole(){
		page = new Page();
		page.setCountField("a.uuid");
	}
	
	@Override
	protected String adminGo() {
		try{
			QueryCache qc = new QueryCache("select a.uuid from SRole a  "+ getWhere() +getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(SRole.class, page.getResults()));
			return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
			
		}
	}
	
	public String getWhere() {
		StringBuffer sb = new StringBuffer("where 1 = 1 ");
		//run模式下只能显示业务类型角色
		if(!ConfigurationManager.isAdminMode()){
			sb.append(" and a.roleType = '" +BaseEnvironment.ROLETYPE_NORMAL+ "' ");
		}else if(StringHelper.isNotEmpty(roleType)){
			sb.append(" and a.roleType = '" + roleType +"' ");
		}
		if (StringHelper.isNotEmpty(roleName))
			sb.append(" and a.name like :roleName ");
		if(StringHelper.isNotEmpty(sysId))
			sb.append(" and a.sysId=:sysId ");
		if(StringHelper.isNotEmpty(openFlag))
			sb.append(" and a.openFlag=:openFlag ");
		return sb.toString();
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.openFlag,a.name,a.sysId";
	}
	public void setWhere(QueryCache qc) {
		if (StringHelper.isNotEmpty(roleName))
			qc.setParameter("roleName", "%" + roleName.trim() + "%");
		if(StringHelper.isNotEmpty(sysId))
			qc.setParameter("sysId", sysId);
		if(StringHelper.isNotEmpty(openFlag))
			qc.setParameter("openFlag", openFlag);
		
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
}
