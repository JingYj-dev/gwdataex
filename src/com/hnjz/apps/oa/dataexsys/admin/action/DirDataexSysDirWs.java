package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDirWs;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.List;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirDataexInbox.java creation date: [Jun 30, 2014 10:00:27 PM] by mazhh
 * http://www.css.com.cn
 */

public class DirDataexSysDirWs extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DirDataexSysDirWs.class);
	
	private String methodName;
	private String wsType;
	private String uuid;
	private Page page;
	
	public DirDataexSysDirWs(){
		page = new Page();
		page.setCountField("a.wsId");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected String adminGo() {
		try{
			if(StringHelper.isEmpty(uuid)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.recordnotfound"));
				return Action.ERROR;
			}
			StringBuffer sb = new StringBuffer("select a.wsId from DataexSysDirWs a"+getWhere()+getOrder());
			QueryCache qc = new QueryCache(sb.toString());
			setWhere(qc);
			page = qc.page(page);
			List<DataexSysDirWs> listDataexSysDirWs = QueryCache.idToObj(DataexSysDirWs.class, page.getResults());
			page.setResults(listDataexSysDirWs);
        	return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1 = 1 ");
		sb.append(" and a.dirId=:dirId");
		
		if(StringHelper.isNotEmpty(methodName)){
			sb.append(" and a.methodName=:methodName ");
		}
		if(StringHelper.isNotEmpty(wsType)){
			sb.append(" and a.wsType=:wsType");
		}
		return sb.toString();
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by a.wsType desc";
	}
	public void setWhere(QueryCache qc) throws ParseException {
		qc.setParameter("dirId", uuid);
		if(StringHelper.isNotEmpty(methodName)){
			qc.setParameter("methodName", methodName);
		}
		if(StringHelper.isNotEmpty(wsType)){
			qc.setParameter("wsType", wsType);
		}
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getWsType() {
		return wsType;
	}

	public void setWsType(String wsType) {
		this.wsType = wsType;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
}
