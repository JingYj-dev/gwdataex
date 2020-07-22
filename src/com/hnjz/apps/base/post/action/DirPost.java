/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirPost.java 
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.post.action;

import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>查询岗位</p>
 * 
 */
public class DirPost extends AdminAction {
	private static Log log = LogFactory.getLog(DirPost.class);
	private String postName;
	private Page page;
	public DirPost(){
		page = new Page();
		page.setCountField("a.uuid");
	}
	@Override
	protected String adminGo() {
		try{
			QueryCache qc = new QueryCache(" select a.uuid from SPost a  " + getWhere() +getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(SPost.class, page.getResults()));
			return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		if (StringHelper.isNotEmpty(postName))
			sb.append(" and a.name like :postName ");
		return sb.toString();
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.name";
	}
	
	public void setWhere(QueryCache qc) {
		if (StringHelper.isNotEmpty(postName))
			qc.setParameter("postName", "%" + postName.trim() + "%");
	}
	
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

}
