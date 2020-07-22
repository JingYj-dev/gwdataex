/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DictMan.java creation date: [Jan 13, 2014 15:21:03 PM] by liuzhb
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.dict.action;

import com.hnjz.apps.base.dict.service.DictItem;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelDict extends AdminAction {
	private static Log log = LogFactory.getLog(DelDict.class);
	private String ids;
	public DelDict() {
	}
	public String adminGo() {
		if (StringHelper.isEmpty(ids)) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		try {
 			List<String> dictIdList = StringHelper.strToList(ids);
			if(dictIdList.size()==1){
				if(DictItem.getSDictByParentId(ids) !=null && DictItem.getSDictByParentId(ids).size()>0){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.NoDeleteDict"));
					return Action.ERROR;
				}
				DictMan.delDict(dictIdList);
			}else{
				int num=0;
				List<String> dictList =null;
				for(int i=0;i<dictIdList.size();i++){
					String id = dictIdList.get(i);
					dictList= StringHelper.strToList(id);
					if(DictItem.getSDictByParentId(id) !=null && DictItem.getSDictByParentId(id).size()>0){
						num++;
					}else{
						DictMan.delDict(dictList);
					}
				}
				if(num==dictIdList.size()){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.NoDeleteDict"));	
					return Action.ERROR;
				}else if(num>=1 && num<dictIdList.size()){
					String str_num=Integer.toString(num);
					result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeleteDictCount",new String[]{str_num}));
					return Action.SUCCESS;
				}
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.dbFaild"));
			return Action.ERROR;
		}
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
