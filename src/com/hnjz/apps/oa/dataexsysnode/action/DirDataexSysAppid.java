package com.hnjz.apps.oa.dataexsysnode.action;

import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysAppid;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DirDataexSysAppid extends AdminAction {
	private static final long serialVersionUID = 5732545014279593439L;
	private static Log log = LogFactory.getLog(DirDataexSysAppid.class);
	private String appidName;
	private String appidCode;
	private Page page;
	
	public DirDataexSysAppid() {
		page = new Page();
		page.setCountField("a.uuid");
	}
	public String adminGo() {
		try {
			QueryCache qc = new QueryCache(" select a.uuid from DataexSysAppid a " + getWhere() +getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(DataexSysAppid.class, page.getResults()));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if (StringHelper.isNotEmpty(appidName))
			sb.append(" and a.appidName like :appidName ");
		if (StringHelper.isNotEmpty(appidCode))
			sb.append(" and a.appidCode like :appidCode ");
		return sb.toString();
	}

	public void setWhere(QueryCache qc) {
		if (StringHelper.isNotEmpty(appidName))
			qc.setParameter("appidName", "%" + appidName.trim() + "%");
		if (StringHelper.isNotEmpty(appidCode))
			qc.setParameter("appidCode", "%" + appidCode.trim() + "%");
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.createdTime desc";
	}
	public String getAppidName() {
		return appidName;
	}
	public void setAppidName(String appidName) {
		this.appidName = appidName;
	}
	public String getAppidCode() {
		return appidCode;
	}
	public void setAppidCode(String appidCode) {
		this.appidCode = appidCode;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	
}
