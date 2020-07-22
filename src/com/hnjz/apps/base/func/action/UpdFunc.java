/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file UpdFuncAction.java creation date: [Jan 9, 2014 11:32:44 AM] by Xingzhc
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
import com.hnjz.apps.base.role.action.RoleItem;
import com.hnjz.apps.base.role.model.SRoleFunc;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class UpdFunc extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdFunc.class);
	private SFunc item ;
	public UpdFunc(){
		item = new SFunc();
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			
			if (StringHelper.isEmpty(item.getFuncId())||StringHelper.isEmpty(item.getName())||StringHelper.isEmpty(item.getSysId())) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			SFunc tmp = QueryCache.get(SFunc.class, item.getUuid());
			if (tmp == null) {
				result = Ajax.JSONResult(1, Messages.getString("systemMsg.recordnotfound"));
				return Action.ERROR;
		 	}

			Object id = new QueryCache("select a.uuid from SFunc a where a.funcId=:funcId and a.sysId=:sysId and a.uuid!=:uuid")
			.setParameter("funcId",item.getFuncId()).setParameter("sysId",item.getSysId()).setParameter("uuid",item.getUuid()).setMaxResults(1).uniqueResult();
		   //一个系统下不能存在两个功能代码相同的功能
			if(id != null){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
					Messages.getString("sysmgrMsg.funcExsit"));				
			return Action.ERROR;
		    }
			if(tmp.getFuncId().equals(item.getFuncId()) == false){ //如果修改funcId，判断对应的功能是否被分配角色
				List<String> rolefuncList = RoleItem.getSRoleFuncIdList(tmp.getFuncId());
				if(rolefuncList.size()>0){//如果功能已被分配角色，修改关联
					for(String rolefuncId : rolefuncList){
						SRoleFunc rf = QueryCache.get(SRoleFunc.class, rolefuncId);
						rf.setFuncId(item.getFuncId());
						tx = new TransactionCache();
						tx.update(rf);
						tx.commit();
					}
				}
			}
			tmp.setName(item.getName());
			tmp.setFuncId(item.getFuncId());
			tmp.setFuncType(item.getFuncType());
			tmp.setOperType(item.getOperType());
			tmp.setLogLevel(item.getLogLevel());
			tmp.setParentId(item.getParentId());
			tmp.setSysId(item.getSysId());
		    tmp.setRemark(item.getRemark());
			tx = new TransactionCache();
			tx.update(tmp);
			tx.commit();
			
			LogPart lp = new LogPart();		
			lp.setOpObjType(SFunc.class.getName());
			lp.setOpObjId(item.getUuid());
			lp.setRelObjType(SFunc.class.getName());
			lp.setRelObjId(item.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(tmp));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"),tmp.toJson());
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

	@Override
	public Object getModel() {
		return item;
	}
	public SFunc getItem() {
		return item;
	}
	public void setItem(SFunc item) {
		this.item = item;
	}

}
