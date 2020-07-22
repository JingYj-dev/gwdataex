
/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file AddRoleFuncAction.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.role.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.apps.base.role.model.SRoleFunc;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.ListUtil;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>添加角色功能</p>
 * 
 */
public class AddRoleFunc extends AdminAction {
	private static Log log = LogFactory.getLog(AddRoleFunc.class);
	private String roleId;
	private String ids;
	public AddRoleFunc() {
	}
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(roleId)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			SRole roleObj = QueryCache.get(SRole.class, roleId);
			List<String> newFuncs = StringHelper.isEmpty(ids)?null:StringHelper.strToList(ids);
			
			List<String> oldFuncs = roleObj.getRoleFuncList().getListById();
//			new QueryCache("select a.funcId from SRoleFunc a where a.roleId=:roleId").setParameter("roleId", roleId).list();
			List<String> delFuncsList =  ListUtil.getFilter(oldFuncs, newFuncs);
			List<String> addFuncsList =  ListUtil.getFilter(newFuncs, oldFuncs);
			List<String> delList = null;
			
			if(delFuncsList!=null && delFuncsList.size()>0){
				delList = new QueryCache("select a.uuid from SRoleFunc a where a.roleId=:roleId and a.funcId in (:funcId)").setParameter("roleId", roleId)
				.setParameter("funcId", delFuncsList).list();
			}
			List<SRoleFunc> addList = null;
			if(addFuncsList != null && addFuncsList.size()>0){
				addList = new ArrayList<SRoleFunc>();
				for(String func : addFuncsList){
					SRoleFunc item = new SRoleFunc();
					item.setFuncId(func);
					item.setRoleId(roleId);
					addList.add(item);
				}
			}
			tx = new TransactionCache();
			if(delList != null && delList.size()>0){
				tx.delete(SRoleFunc.class,delList);
				if(roleObj.getRoleFuncList() !=null){
					roleObj.getRoleFuncList().removeAll();
				}
			}
			if(addList != null && addList.size()>0){
				tx.save(addList);
				for(SRoleFunc addfunc : addList){
					if(roleObj.getRoleFuncList() !=null){
						roleObj.getRoleFuncList().add(addfunc.getFuncId());
					}
				}
			}
			tx.commit();
			
			
			
			if(delList != null && delList.size()>0){
				for(String delId : delList){
					LogPart lp = new LogPart();		
					lp.setOpObjType(SRoleFunc.class.getName());
					lp.setOpObjId(delId);
					lp.setRelObjType(SRole.class.getName());
					lp.setRelObjId(roleId);
					lp.setOpType(LogConstant.LOG_TYPE_DELETE);
					lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lp.setLogData("");
					lp.setResult(LogConstant.RESULT_SUCCESS);
					lp.save();
				}
			}
			if(addList != null && addList.size()>0){
				for(SRoleFunc roleFunc : addList){
					LogPart lp = new LogPart();		
					lp.setOpObjType(SRoleFunc.class.getName());
					lp.setOpObjId(roleFunc.getUuid());
					lp.setRelObjType(SRole.class.getName());
					lp.setRelObjId(roleFunc.getRoleId());
					lp.setOpType(LogConstant.LOG_TYPE_ADD);
					lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lp.setLogData(Json.object2json(roleFunc));
					lp.setResult(LogConstant.RESULT_SUCCESS);
					lp.save();
				}
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
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

}
