/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetSysAction.java creation date: [2014-1-8 14:30:20] by Xingzhc
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.sys.action;

import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * <p>获得单个系统</p>
 * 
 */
public class GetSys extends AdminAction {
	private static Log log = LogFactory.getLog(GetSys.class);
	private String uuid;
	private SSys item;
	
	public GetSys() {
	}
	@Override
	protected String adminGo() {
		if (!StringHelper.isEmpty(uuid) ) {
			item = QueryCache.get(SSys.class, uuid);
			if(StringHelper.isNotEmpty(item.getRemark())){
				String str = RegexCheck.TagReverse(item.getRemark());
				item.setRemark(str);
			}
		}else{
			item = new SSys();
			item.setOpenFlag("1");
		}
		return Action.SUCCESS;
		
	}
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public SSys getItem() {
		return item;
	}
	public void setItem(SSys item) {
		this.item = item;
	}



}
