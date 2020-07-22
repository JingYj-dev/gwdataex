package com.hnjz.apps.base.audit.action;

import com.hnjz.apps.base.audit.model.AuditParam;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DirAuditParam extends AdminAction {
	private static final long serialVersionUID = -3464653131463418344L;
	private static Log log = LogFactory.getLog(DirAuditParam.class);
	private Integer paramId;
	private String paramName;
	private Page page;
	
	public DirAuditParam() {
		page = new Page();
		page.setCountField("a.paramId");
	}
	public String adminGo() {
		try {
			QueryCache qc = new QueryCache(" select a.paramId from AuditParam a " + getWhere() +getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(AuditParam.class, page.getResults()));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if (paramId != null)
			sb.append(" and a.paramId = :paramId ");
		if (StringHelper.isNotEmpty(paramName))
			sb.append(" and a.paramName like :paramName ");
		return sb.toString();
	}

	public void setWhere(QueryCache qc) {
		if (paramId != null)
			qc.setParameter("paramId", paramId);
		if (StringHelper.isNotEmpty(paramName))
			qc.setParameter("paramName", "%" + paramName.trim() + "%");
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.paramId";
	}
	public Integer getParamId() {
		return paramId;
	}
	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
}
