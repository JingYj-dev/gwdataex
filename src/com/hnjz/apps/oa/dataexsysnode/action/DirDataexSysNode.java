package com.hnjz.apps.oa.dataexsysnode.action;

import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DirDataexSysNode extends AdminAction {
	private static final long serialVersionUID = -3464653131463418344L;
	private static Log log = LogFactory.getLog(DirDataexSysNode.class);
	private String exnodeName;
	private String exnodeType;
	private String openFlag;
	private Page page;
	
	public DirDataexSysNode() {
		page = new Page();
		page.setCountField("a.exnodeId");
	}
	public String adminGo() {
		try {
			QueryCache qc = new QueryCache(" select a.exnodeId from DataexSysNode a " + getWhere() +getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(DataexSysNode.class, page.getResults()));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if (StringHelper.isNotEmpty(exnodeName))
			sb.append(" and a.exnodeName like :exnodeName ");
		if (StringHelper.isNotEmpty(exnodeType))
			sb.append(" and a.exnodeType like :exnodeType ");
		if (StringHelper.isNotEmpty(openFlag))
			sb.append(" and a.exnodeStatus = :openFlag ");
		return sb.toString();
	}

	public void setWhere(QueryCache qc) {
		if (StringHelper.isNotEmpty(exnodeName))
			qc.setParameter("exnodeName", "%" + exnodeName.trim() + "%");
		if (StringHelper.isNotEmpty(exnodeType))
			qc.setParameter("exnodeType", "%" + exnodeType.trim() + "%");
		if (StringHelper.isNotEmpty(openFlag))
			qc.setParameter("openFlag", openFlag.trim());
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.createdTime desc";
	}
	public String getExnodeName() {
		return exnodeName;
	}
	public void setExnodeName(String exnodeName) {
		this.exnodeName = exnodeName;
	}
	public String getExnodeType() {
		return exnodeType;
	}
	public void setExnodeType(String exnodeType) {
		this.exnodeType = exnodeType;
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

}
