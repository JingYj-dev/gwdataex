/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirSysAction.java creation date: [2014-1-8 14:30:20] by Xingzhc
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.sys.action;

import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>查询系统分页列表</p>
 * 
 */
public class DirSys extends AdminAction {
	private static Log log = LogFactory.getLog(DirSys.class);
	private String sysName;
	private String openFlag;
	private String sysId;
	private String name;
	private Page page;
	
	public DirSys(){
		page = new Page();
		page.setCountField("a.uuid");
	}
	@Override
	protected String adminGo() {
		try{
			QueryCache qc = new QueryCache(" select a.uuid from SSys a  " + getWhere()+getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(SSys.class, page.getResults()));
			return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public String getWhere() {
		StringBuffer sb = new StringBuffer("where 1 = 1");
//		if (StringHelper.isNotEmpty(sysId))
//			sb.append("and a.sysId like :sysId ");
//		if (StringHelper.isNotEmpty(sysName))
//			sb.append("and a.name like :sysName ");
		if(StringHelper.isNotEmpty(name))
			sb.append(" and (a.name like :name or a.sysId like :name) ");
		if(StringHelper.isNotEmpty(openFlag))
			sb.append("and a.openFlag=:openFlag ");
		return sb.toString();
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.openFlag,a.sysId";
	}
	
	public void setWhere(QueryCache qc) {
//		if (StringHelper.isNotEmpty(sysId))
//			qc.setParameter("sysId", "%" + sysId.trim() + "%");
//		if (StringHelper.isNotEmpty(sysName))
//			qc.setParameter("sysName", "%" + sysName.trim() + "%");
		if(StringHelper.isNotEmpty(name))
			qc.setParameter("name", "%" + name.trim() + "%");
		if(StringHelper.isNotEmpty(openFlag))
			qc.setParameter("openFlag", openFlag);
	}
	
	public String getSysName() {
		return sysName;
	}
	
	public void setSysName(String sysName) {
		this.sysName = sysName;
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
	
	public String getSysId() {
		return sysId;
	}
	
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}