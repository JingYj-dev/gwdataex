/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file UpdRoleAction.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
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
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>更新角色</p>
 * 
 */
public class UpdRole extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdRole.class);
	private SRole item = null;
	public UpdRole(){
		item = new SRole();
	}
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
		if (StringHelper.isEmpty(item.getUuid())||StringHelper.isEmpty(item.getName()) || StringHelper.isEmpty(item.getSysId())||StringHelper.isEmpty(item.getOpenFlag())) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			Object uuid = new QueryCache("select a.uuid from SRole a where a.name=:name and a.uuid!=:uuid")
				.setParameter("name",item.getName()).setParameter("uuid", item.getUuid()).setMaxResults(1).uniqueResult();
			if(uuid != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.nameExist"));				
				return Action.ERROR;
			}
			SRole oldItem = QueryCache.get(SRole.class, item.getUuid());
			if(checkUpdate(oldItem, item)){
				oldItem.setName(item.getName());
				oldItem.setOpenFlag(item.getOpenFlag());
				oldItem.setRemark(item.getRemark());
				oldItem.setSysId(item.getSysId());
				oldItem.setRoleType(item.getRoleType());
				tx = new TransactionCache();
				tx.update(oldItem);
				tx.commit();
				
				LogPart lp = new LogPart();		
				lp.setOpObjType(SRole.class.getName());
				lp.setOpObjId(item.getUuid());
				lp.setRelObjType(SRole.class.getName());
				lp.setRelObjId(item.getUuid());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(oldItem));
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
				
			  //LogMan.addLog(getSLog(funcid, oldItem));
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
	
	public boolean checkUpdate(SRole oldItem,SRole tmp){
		if(tmp.getName().equals(oldItem.getName())&& tmp.getOpenFlag().equals(oldItem.getOpenFlag())&& tmp.getSysId().equals(oldItem.getSysId())&& tmp.getRoleType().equals(oldItem.getRoleType())&&tmp.getRemark().equals(oldItem.getRemark()))
			return false;
		else{
			return true; 
		 }
	}
	
	public Object getModel() {
		// TODO Auto-generated method stub
		return item;
	}
	public SRole getItem() {
		return item;
	}
	public void setItem(SRole item) {
		this.item = item;
	}

	

}
