package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.apps.base.log.model.SLogExt;
import com.hnjz.apps.base.log.service.QueryLog;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirLogAction.java creation date: [Jan 13, 2014 1:54:27 PM] by Xingzhc
 * http://www.css.com.cn
 */

public class DirLogExt extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(DirLogExt.class);
	private final String funcid = "ACL_DIRLOGEXT";
	
	private String opName; //操作人姓名
	private String opType; //操作类型
	private Date opTime; //操作时间
	private String logLevel; //日志级别
	private String logType; //日志类型:业务 管理 安全
	private String opResult; //操作结果
	private Date beginDate;
	private Date endDate;
	private String funcId;
	private Page page;
	
	private String userId;
	public DirLogExt(){
		page = new Page();
		page.setCountField("a.logId");
		setFuncid(funcid);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected String adminGo() {
		try{
			userId = UserProvider.currentUser().getUserId();
			StringBuffer sb = new StringBuffer(" select a.logId from SLogExt a "+ getWhere() + getOrder());
			QueryCache qc = new QueryLog(sb.toString());
			setWhere(qc);
			page = qc.page(page);
			List<SLogExt> listLogExts = QueryLog.idToObj(SLogExt.class, page.getResults());
			page.setResults(listLogExts);
			//page.setResults(qc.list());
        	return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if (StringHelper.isNotEmpty(funcId)) {
			sb.append(" and a.funcId like :funcId ");
		}
		
		if(beginDate != null) {
			sb.append(" and a.opTime >=:beginDate");
		}
		
		if(endDate != null) {
			sb.append(" and a.opTime <:endDate");
		}
		
		if (StringHelper.isNotEmpty(opName)) { //操作人
			sb.append(" and a.opName like :opName ");
		}
		
		if(StringHelper.isNotEmpty(opType)) {
			sb.append(" and a.opType =:opType ");
		}
		
		if(StringHelper.isNotEmpty(logType)) {
			sb.append(" and a.logType =:logType ");
		}
		
		if(StringHelper.isNotEmpty(logLevel)) {
			sb.append(" and a.logLevel =:logLevel ");
		}
		
		return sb.toString();
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "";
	}
	public void setWhere(QueryCache qc) throws ParseException {
		if(StringHelper.isNotEmpty(funcId)) {
			qc.setParameter("funcId", funcId);
		}
		
		if(beginDate != null){
			qc.setParameter("beginDate",beginDate);
		}
		
		if(endDate != null){
			qc.setParameter("endDate",DateUtil.addDate(endDate, 1));
		}
		
		if (StringHelper.isNotEmpty(opName)) {
			qc.setParameter("opName", "%" + opName.trim() + "%");
		}
		
		if(StringHelper.isNotEmpty(opType)) {
			qc.setParameter("opType", opType);
		}
		
		if(StringHelper.isNotEmpty(logType)) {
			qc.setParameter("logType", logType);
		}
		
		if(StringHelper.isNotEmpty(logLevel)) {
			qc.setParameter("logLevel", logLevel);
		}
	}
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getOpResult() {
		return opResult;
	}

	public void setOpResult(String opResult) {
		this.opResult = opResult;
	}

	public String getFuncid() {
		return funcid;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
