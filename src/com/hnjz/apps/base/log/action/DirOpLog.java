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
public class DirOpLog extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DirOpLog.class);
	private String operName;
	private Integer operType;
	private String opObjType;
	private String logLevel;
	private Date opBeginDate;
	private Date opEndDate;
	private String funcId;
	private Page page;
	public DirOpLog(){
		page = new Page();
		page.setCountField("a.logId");
	}
	
	@Override
	protected String adminGo() {
		try{
			if(opEndDate != null){
				opEndDate = DateUtil.addDate(opEndDate, 1);
			}
			StringBuffer sb = new StringBuffer(" select a.logId from SLog a "
					+ getWhere() 
					+getOrder());
			QueryCache qc = new QueryLog(sb.toString());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryLog.idToObj(SLog.class, page.getResults()));
        	return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where  a.operatorType in (1,4) ");
		if (StringHelper.isNotEmpty(operName))
			sb.append(" and a.opName like :operName ");
		
		if(operType != null)
			sb.append("and a.opType=:operType ");
		
		if(StringHelper.isNotEmpty(opObjType))
			sb.append("and a.opObjType=:opObjType ");

		if(opBeginDate != null){
			sb.append(" and a.opTime >=:beginDate");
		}
		if(opEndDate != null){
			sb.append(" and a.opTime <:endDate");
		}
		return sb.toString();
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by opTime desc ";
	}
	public void setWhere(QueryCache qc) throws ParseException {
		if (StringHelper.isNotEmpty(operName))
			qc.setParameter("operName", "%" + operName.trim() + "%");
		
		if(operType != null)
			qc.setParameter("operType", operType);
		
		if(StringHelper.isNotEmpty(opObjType))
			qc.setParameter("opObjType", opObjType);
		
		if(opBeginDate != null){
			qc.setParameter("beginDate",opBeginDate);
		}
		if(opEndDate!= null){
			qc.setParameter("endDate",opEndDate);
		}
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public Date getOpBeginDate() {
		return opBeginDate;
	}

	public void setOpBeginDate(Date opBeginDate) {
		this.opBeginDate = opBeginDate;
	}

	public Date getOpEndDate() {
		if(opEndDate != null)
			opEndDate = DateUtil.addDate(opEndDate, -1);
		return opEndDate;
	}

	public void setOpEndDate(Date opEndDate) {
		this.opEndDate = opEndDate;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getOpObjType() {
		return opObjType;
	}

	public void setOpObjType(String opObjType) {
		this.opObjType = opObjType;
	}

	
}
