package com.hnjz.apps.base.dict.action;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.base.dict.service.QueryDict;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DirServerid extends AdminAction {
	private static Log log = LogFactory.getLog(DirServerid.class);
	private String parentId;
	private String name;
	private Page page;
	
	public DirServerid() {
		page = new Page();
		page.setCountField("a.uuid");
		page.setOrderString("code");
		page.setOrderFlag(Page.OEDER_ASC); 
	}
	
	public String adminGo() {
		try {
			parentId = DictMan.getDictType("d_root","d_serverid").getUuid();
			QueryCache qc = new QueryDict(" select a.uuid from SDict a " + getWhere() +getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryDict.idToObj(SDict.class, page.getResults()));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1=1 and a.parentId =:parentId ");
		if(StringHelper.isNotEmpty(name))
			sb.append(" and (a.name like :name or a.code like :name) ");
		return sb.toString();
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "";
	}

	public void setWhere(QueryCache qc) {
		qc.setParameter("parentId", parentId);
		if(StringHelper.isNotEmpty(name))
			qc.setParameter("name", "%" + name.trim() + "%");
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
