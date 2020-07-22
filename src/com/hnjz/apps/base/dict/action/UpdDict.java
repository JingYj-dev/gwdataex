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
import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UpdDict extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdDict.class);
	private SDict dict = null;
	public Object getModel() {
		return dict;
	}

	public UpdDict() {
		dict = new SDict();
	}

	public String adminGo() {
		try {
			if (dict == null || StringHelper.isEmpty(dict.getUuid())
					|| StringHelper.isEmpty(dict.getCode())
					|| StringHelper.isEmpty(dict.getName())
					|| StringHelper.isEmpty(dict.getParentId())
					|| StringHelper.isEmpty(dict.getTableName())) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
						Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			Object uuid = new QueryDict(
					"select a.uuid from SDict a where a.parentId = :parentId and a.code =:code and a.uuid!=:uuid")
					.setParameter("parentId", dict.getParentId()).setParameter(
							"code", dict.getCode()).setParameter("uuid", dict.getUuid()).setMaxResults(1).uniqueResult();
			if (uuid != null) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
						Messages.getString("sysmgrMsg.idExist"));
				return Action.ERROR;
			} else {
				SDict tmp = QueryDict.get(SDict.class,dict.getUuid());
//				SDict tmp = DictMan.getDictType(dict.getTableName(),
//						dict.getCode());
				if (checkUpdate(dict, tmp)) {
//					if(dict.getCode().equals(tmp.getCode())){
//						List<String> childIds = DictItem.getSDictByParentId(dict.getUuid());
//						List<SDict> childItem = QueryDict.idToObj(SDict.class, childIds);
//						String newParentId = DictMan.getUuid(dict.getTableName(), dict.getCode());
//						for(SDict item:childItem){
//							item.setParentId(newParentId);
//							DictMan.updateDict(item);
//						}
//						dict.setUuid(DictMan.getUuid(dict.getTableName(), dict.getCode()));
//					}
					DictMan.updateDict(dict);
				}
				// LogMan.addLog(getSLog(funcid, dict));
				result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS,
						Messages.getString("systemMsg.success"));
				return Action.SUCCESS;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
					Messages.getString("systemMsg.dbFaild"));
			return Action.ERROR;
		}
	}


	public boolean checkUpdate(SDict obj1, SDict obj2) {
		if (obj1.getCode().equals(obj2.getCode())
				&& obj1.getName().equals(obj2.getName())
				&& obj1.getRemark().equals(obj2.getRemark())) {
			return false;
		}
		return true;
	}

	public SDict getDict() {
		return dict;
	}

	public void setDict(SDict dict) {
		this.dict = dict;
	}
}
