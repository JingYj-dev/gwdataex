/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file UpdSysAction.java creation date: [2014-1-9 09:30:20] by Xingzhc
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.sys.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>更新系统</p>
 * 
 */
public class UpdSys extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdSys.class);
	private SSys item = null;
	
	public UpdSys(){
		item = new SSys();
	}
	@Override
	protected String adminGo() {
		if (StringHelper.isEmpty(item.getSysId())||StringHelper.isEmpty(item.getUuid())||StringHelper.isEmpty(item.getName()) || StringHelper.isEmpty(item.getUrl())||StringHelper.isEmpty(item.getOpenFlag())) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			SSys oldItem = QueryCache.get(SSys.class, item.getUuid());
			if(oldItem == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.recordnotfound"));				
				return Action.ERROR;
			}
			Object uuid = new QueryCache("select a.uuid from SSys a where a.sysId=:sysId and a.uuid!=:uuid")
				.setParameter("sysId", item.getSysId()).setParameter("uuid", item.getUuid()).setMaxResults(1).uniqueResult();
			
			if(uuid != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("sysmgrMsg.idExist"));				
				return Action.ERROR;
			}
			
			if(checkUpdate(oldItem, item)){
				oldItem.setSysId(item.getSysId());
				oldItem.setName(item.getName());
				oldItem.setOpenFlag(item.getOpenFlag());
				oldItem.setRemark(item.getRemark());
				oldItem.setUrl(item.getUrl());
				tx = new TransactionCache();
				tx.update(oldItem);
				tx.commit();
				
				LogPart lp = new LogPart();		
				lp.setOpObjType(SSys.class.getName());
				lp.setOpObjId(item.getUuid());
				lp.setRelObjType(SSys.class.getName());
				lp.setRelObjId(item.getUuid());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(oldItem));
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

	public boolean checkUpdate(SSys oldItem,SSys tmp){
		if(tmp.getName().equals(oldItem.getName())&& tmp.getOpenFlag().equals(oldItem.getOpenFlag())&& tmp.getUrl().equals(oldItem.getUrl())&& tmp.getRemark().equals(oldItem.getRemark()) && tmp.getSysId().equals(oldItem.getSysId()))
			return false;
		else{
			return true; 
		 }
	}
	
	public SSys getItem() {
		return item;
	}
	public void setItem(SSys item) {
		this.item = item;
	}
	public Object getModel() {
		return item;
	}

}
