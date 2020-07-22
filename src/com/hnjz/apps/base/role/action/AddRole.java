/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file AddRoleAction.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.role.action;

import com.hnjz.apps.base.common.BaseEnvironment;
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
 * <p>添加角色</p>
 * 
 */
public class AddRole extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(AddRole.class);
	private SRole item;
	public AddRole(){
		this.item = new SRole(); 
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getName())||StringHelper.isEmpty(item.getSysId())||StringHelper.isEmpty(item.getOpenFlag())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			Object uuid = new QueryCache("select a.uuid from SRole a where a.name=:name")
				.setParameter("name",item.getName()).setMaxResults(1).uniqueResult();
			if(uuid != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.nameExist"));				
				return Action.ERROR;
			}
			tx = new TransactionCache();
			item.setDelFlag("2");
			if(StringHelper.isEmpty(item.getRoleType())){
				item.setRoleType(BaseEnvironment.ROLETYPE_NORMAL);
			}
			tx.save(item);
			tx.commit();
			
			LogPart lp = new LogPart();		
			lp.setOpObjType(SRole.class.getName());
			lp.setOpObjId(item.getUuid());
			lp.setRelObjType(SRole.class.getName());
			lp.setRelObjId(item.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_ADD);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(item));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			
		//	LogMan.addLog(getSLog(funcid, item));
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.getUuid());
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

	/** 
	 * @Description   
	 * @Author Xingzhc 
	 * @CreateDate 2014Jan 6, 201411:03:12 AM
	 * @Updator 
	 * @UpdateDate  
	 * @version  1.0.0 
	 */
	public Object getModel() {
		// TODO Auto-generated method stub
		return item;
	}

}
