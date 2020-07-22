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

public class DirAuditLog extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DirAuditLog.class);
	private String operName;
	private Date auditBeginDate;
	private Date auditEndDate;
	private Integer operType;
	private String opObjType;
	private Integer eventType;
	private Page page;
	
	public DirAuditLog(){
		page = new Page();
		page.setCountField("a.logId");
	}

	@Override
	protected String adminGo() {
		try{
			if(auditEndDate != null){
				auditEndDate = DateUtil.addDate(auditEndDate, 1);
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
		StringBuffer sb = new StringBuffer(" where a.operatorType in ('2','3') ");
		if(StringHelper.isNotEmpty(operName))
			sb.append(" and a.opName like :operName ");
		
		if(auditBeginDate != null)
			sb.append(" and a.opTime >=:auditBeginDate");
		
		if(auditEndDate != null)
			sb.append(" and a.opTime <:auditEndDate");
		
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
		
		if(auditBeginDate != null)
			qc.setParameter("auditBeginDate",auditBeginDate);
		
		if(auditEndDate!= null)
			qc.setParameter("auditEndDate",auditEndDate);
		
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

	public Date getAuditBeginDate() {
		return auditBeginDate;
	}

	public void setAuditBeginDate(Date auditBeginDate) {
		this.auditBeginDate = auditBeginDate;
	}

	public Date getAuditEndDate() {
		if(auditEndDate != null)
			auditEndDate = DateUtil.addDate(auditEndDate, -1);
		return auditEndDate;
	}

	public void setAuditEndDate(Date auditEndDate) {
		this.auditEndDate = auditEndDate;
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
