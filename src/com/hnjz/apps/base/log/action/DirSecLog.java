package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.model.SLog;
import com.hnjz.apps.base.log.service.QueryLog;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.Date;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirLogAction.java creation date: [July 13, 2014 1:54:27 PM] by Shiwl
 * http://www.css.com.cn
 */

public class DirSecLog extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DirSecLog.class);
	private String operName;
	private Date secBeginDate;
	private Date secEndDate;
	private Integer operType;
	private String opObjType;
	private Integer eventType;
	private Page page;
	
	public DirSecLog(){
		page = new Page();
		page.setCountField("a.logId");
	}

	@Override
	protected String adminGo() {
		try{
			if(secEndDate != null){
				secEndDate = DateUtil.addDate(secEndDate, 1);
			}
			StringBuffer sb = new StringBuffer(" select a.logId from SLog a " + getWhere() + getOrder());
			QueryCache qc = new QueryLog(sb.toString());
			setWhere(qc);
			page = qc.pageCache(page);
			page.setResults(QueryLog.idToObj(SLog.class, page.getResults()));
        	return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getWhere(){
		StringBuffer sb = new StringBuffer(" where a.operatorType in (1,4) ");
		if(StringHelper.isNotEmpty(operName))
			sb.append(" and a.opName like :operName ");
		
		if(secBeginDate != null)
			sb.append(" and a.opTime >=:secBeginDate");
		
		if(secEndDate != null)
			sb.append(" and a.opTime <:secEndDate");
		
		if(operType != null)
			sb.append(" and a.opType=:operType ");
		
		if(StringHelper.isNotEmpty(opObjType))
			sb.append(" and a.opObjType=:opObjType ");
		
		if(eventType != null)
			sb.append(" and a.eventType=:eventType ");
		
		return sb.toString();
	}
	
	public void setWhere(QueryCache qc) throws ParseException {
		if(StringHelper.isNotEmpty(operName))
			qc.setParameter("operName", "%" + operName.trim() + "%");
		
		if(secBeginDate != null)
			qc.setParameter("secBeginDate",secBeginDate);
		
		if(secEndDate!= null)
			qc.setParameter("secEndDate",secEndDate);
		
		if(operType != null)
			qc.setParameter("operType", operType);
		
		if(StringHelper.isNotEmpty(opObjType))
			qc.setParameter("opObjType", opObjType);
		
		if(eventType != null)
			qc.setParameter("eventType", eventType);
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by opTime desc ";
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public Date getSecBeginDate() {
		return secBeginDate;
	}

	public void setSecBeginDate(Date secBeginDate) {
		this.secBeginDate = secBeginDate;
	}

	public Date getSecEndDate() {
		if(secEndDate != null)
			secEndDate = DateUtil.addDate(secEndDate, -1);
		return secEndDate;
	}

	public void setSecEndDate(Date secEndDate) {
		this.secEndDate = secEndDate;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public String getOpObjType() {
		return opObjType;
	}

	public void setOpObjType(String opObjType) {
		this.opObjType = opObjType;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	
}
