package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.model.SAlertSetting;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DirSAlertSetting extends AdminAction {
	private static final long serialVersionUID = -3464653131463418344L;
	private static Log log = LogFactory.getLog(DirSAlertSetting.class);
	private String eventType;
	private String severLevel;
	private String alertType;
	private Page page;
	
	public DirSAlertSetting() {
		page = new Page();
		page.setCountField("a.setId");
	}
	public String adminGo() {
		try {
			QueryCache qc = new QueryCache(" select a.setId from SAlertSetting a " + getWhere() +getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(SAlertSetting.class, page.getResults()));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if (StringHelper.isNotEmpty(eventType))
			sb.append(" and a.eventType = :eventType ");
		if (StringHelper.isNotEmpty(severLevel))
			sb.append(" and a.severLevel = :severLevel ");
		if (StringHelper.isNotEmpty(alertType))
			sb.append("and a.alertType = :alertType");
		return sb.toString();
	}

	public void setWhere(QueryCache qc) {
		if (StringHelper.isNotEmpty(eventType))
			qc.setParameter("eventType", eventType);
		if (StringHelper.isNotEmpty(severLevel))
			qc.setParameter("severLevel", severLevel);
		if (StringHelper.isNotEmpty(alertType))
			qc.setParameter("alertType", alertType);
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by a.setId";
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getSeverLevel() {
		return severLevel;
	}
	public void setSeverLevel(String severLevel) {
		this.severLevel = severLevel;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
}
