/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file UpdRoleStatus.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.role.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * <p>更新角色功能</p>
 * 
 */
public class UpdRoleStatus extends AdminAction {
	private static Log log = LogFactory.getLog(UpdRoleStatus.class);
	private String  ids;
	public String openStatus;
	
	public UpdRoleStatus (){
	}
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)||StringHelper.isEmpty(openStatus)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List listIds = StringHelper.strToList(ids);
			List<SRole> roleList = QueryCache.idToObj(SRole.class, listIds);
			tx = new TransactionCache();
			for(SRole role : roleList){
				    role.setOpenFlag(openStatus);
					tx.update(role);
			}
			tx.commit();
			for(SRole role : roleList){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SRole.class.getName());
				lp.setOpObjId(role.getUuid());
				lp.setRelObjType(SRole.class.getName());
				lp.setRelObjId(role.getUuid());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(role));
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
