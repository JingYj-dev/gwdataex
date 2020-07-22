/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DictMan.java creation date: [Jan 13, 2014 15:21:03 PM] by liuzhb
 * http://www.css.com.cn
 */
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

public class DirDict extends AdminAction {
	private static Log log = LogFactory.getLog(DirDict.class);
	private String dictName = null;
	private String dictCode = null;
	private String parentId = null;
	private String name;
	private Page page;
	
	public DirDict() {
		page = new Page();
		page.setCountField("a.uuid");
	}
	public String adminGo() {
		try {
			if (StringHelper.isEmpty(parentId)){
				parentId = DictMan.getDictType("0", "d_root").getUuid();
			}
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
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if (StringHelper.isNotEmpty(parentId))
			sb.append(" and a.parentId = :parentId ");
//		if (StringHelper.isNotEmpty(dictName))
//			sb.append(" and a.name like :dictName ");
//		if(StringHelper.isNotEmpty(dictCode))
//			sb.append("and a.code like :dictCode ");
		if(StringHelper.isNotEmpty(name))
			sb.append(" and (a.name like :name or a.code like :name) ");
		return sb.toString();
	}

	public void setWhere(QueryCache qc) {
		if (StringHelper.isNotEmpty(parentId))
			qc.setParameter("parentId", parentId);
//		if (StringHelper.isNotEmpty(dictName))
//			qc.setParameter("dictName", "%" + dictName.trim() + "%");
//		if(StringHelper.isNotEmpty(dictCode))
//			qc.setParameter("dictCode", "%"+ dictCode.trim()+"%");
		if(StringHelper.isNotEmpty(name))
			qc.setParameter("name", "%" + name.trim() + "%");
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.code";
	}
	public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	public String getDictCode() {
		return dictCode;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
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
	
}
