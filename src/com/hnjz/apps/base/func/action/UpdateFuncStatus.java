/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file UpdSysStatus.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


@SuppressWarnings("all")
public class UpdateFuncStatus extends AdminAction {
	private static Log log = LogFactory.getLog(UpdateFuncStatus.class);
	private String  ids;
	public String openStatus;
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)||StringHelper.isEmpty(openStatus)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List listIds = StringHelper.strToList(ids);
			List<SFunc> funcList = QueryCache.idToObj(SFunc.class, listIds);
			tx = new TransactionCache();
			for(SFunc func : funcList){
			     	func.setOpenFlag(openStatus);
					tx.update(func);
			}
			tx.commit();
			for(SFunc func : funcList){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SFunc.class.getName());
				lp.setOpObjId(func.getUuid());
				lp.setRelObjType(SFunc.class.getName());
				lp.setRelObjId(func.getUuid());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(func));
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
			}
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
		
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getOpenStatus() {
		return openStatus;
	}
	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

}
