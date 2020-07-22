/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetRoleAction.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.role.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>获得单个角色</p>
 * 
 */
public class GetRole extends AdminAction {
	private static Log log = LogFactory.getLog(GetRole.class);
	private String uuid;
	private SRole item;
	public GetRole(){
	}
	@Override
	protected String adminGo() {
		try{
			// TODO Auto-generated method stub
			if (!StringHelper.isEmpty(uuid)) {
				item = QueryCache.get(SRole.class, uuid);
				if(StringHelper.isNotEmpty(item.getRemark())){
					String str = RegexCheck.TagReverse(item.getRemark());
					item.setRemark(str);
				}
			}else{
				item = new SRole();
				item.setOpenFlag("1");
				item.setRoleType(BaseEnvironment.ROLETYPE_NORMAL);
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			setMessage(Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public SRole getItem() {
		return item;
	}
	public void setItem(SRole item) {
		this.item = item;
	}
}
