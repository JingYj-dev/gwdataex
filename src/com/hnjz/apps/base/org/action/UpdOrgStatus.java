/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file UpdOrgStatus.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/** 
 * <p>修改组织状态</p>
 * 
 */
public class UpdOrgStatus extends AdminAction {
	private static Log log = LogFactory.getLog(UpdOrgStatus.class);
	private String  ids;
	public String openStatus;
	public UpdOrgStatus(){
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
			List<SOrg> orgList = QueryCache.idToObj(SOrg.class, listIds);
			tx = new TransactionCache();
			for(SOrg org : orgList){
			    	org.setOpenFlag(openStatus);
					tx.update(org);
			}
			tx.commit();
			
			
			for(SOrg org : orgList){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SOrg.class.getName());
				lp.setOpObjId(org.getUuid());
				lp.setRelObjType(SOrg.class.getName());
				lp.setRelObjId(org.getUuid());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
//				JSONArray ja = OrgItem.getSubOrgan(OrgTree.getInstance().getRootNode().getNodeId());
				lp.setLogData(org.toJson().toString());
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
			}
			
			// LogMan.addLog(getSLog(funcid, oldItem));
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
