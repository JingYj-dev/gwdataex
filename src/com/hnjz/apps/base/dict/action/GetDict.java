/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DictMan.java creation date: [Jan 13, 2014 15:21:03 PM] by liuzhb
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.dict.action;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.QueryDict;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetDict extends AdminAction {
	private static Log log = LogFactory.getLog(GetDict.class);
	private String uuid = null;
	private String parentId = null;
	private SDict dict = null;
	public GetDict() {
	}
	public String adminGo() {
		try {
			if (StringHelper.isEmpty(uuid)) {
				if (StringHelper.isEmpty(parentId)) {
					setMessage(Messages.getString("systemMsg.fieldEmpty"));
					return Action.ERROR;
				}
				dict = new SDict();
				dict.setParentId(parentId);
				SDict obj = QueryDict.get(SDict.class, parentId);
				if(obj==null){
					setMessage(Messages.getString("systemMsg.NoParentDict"));
					return Action.ERROR;
				}
				String tableName = obj.getTableName();
				if(tableName.equals("0")){
					tableName = "d_root";
				}else if(tableName.equals("d_root")){
					tableName = obj.getCode();
				}
				dict.setTableName(tableName);

			}else{
				dict = QueryDict.get(SDict.class, uuid);
				if(dict == null){
					setMessage(Messages.getString("systemMsg.readError"));
					return Action.ERROR;
				}
				if(StringHelper.isNotEmpty(dict.getRemark())){
					String str = RegexCheck.TagReverse(dict.getRemark());
					dict.setRemark(str);
				}
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.dbFaild"));
			return Action.ERROR;
		} 
	}
	public SDict getDict() {
		return dict;
	}
	public void setDict(SDict dict) {
		this.dict = dict;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
