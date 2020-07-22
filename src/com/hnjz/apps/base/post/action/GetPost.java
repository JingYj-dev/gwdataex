/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetPost.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.post.action;

import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.sys.action.GetSys;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * <p>获得单个岗位</p>
 * 
 */
public class GetPost extends AdminAction {
	private static Log log = LogFactory.getLog(GetSys.class);
	private String uuid;
	private SPost item;
	public GetPost(){
	}
	@Override
	protected String adminGo() {
		if (!StringHelper.isEmpty(uuid)) {
			item = QueryCache.get(SPost.class, uuid);
			if(StringHelper.isNotEmpty(item.getRemark())){
				String str = RegexCheck.TagReverse(item.getRemark());
				item.setRemark(str);
			}
			
		}else{
			item = new SPost();
		}
		return Action.SUCCESS;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public SPost getItem() {
		return item;
	}
	public void setItem(SPost item) {
		this.item = item;
	}

}
