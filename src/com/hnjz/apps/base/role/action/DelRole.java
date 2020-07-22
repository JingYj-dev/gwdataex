/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DelRoleAction.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
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
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.user.action.UserItem;
import com.hnjz.apps.base.user.model.SUserRole;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/** 
 * <p>删除角色</p>
 * 
 */
public class DelRole extends AdminAction {
	private static Log log = LogFactory.getLog(DelRole.class);
	private String ids;
	public DelRole(){
	}
	@Override 
	protected String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List listIds = StringHelper.strToList(ids);
			List<SRole> roleList = QueryCache.idToObj(SRole.class, listIds);
/*			if(roleList.size()==1){//单条记录删除
				SRole srole = roleList.get(0);
				List<SRoleFunc> rolefunclist = RoleItem.getSRoleFuncByRoleId(srole.getUuid());
				List<SUserRole> userRoleList = UserItem.getSUserRoleByRoleId(srole.getUuid());
				if(userRoleList!= null && userRoleList.size()>0){
					result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeleteRole"));
					return Action.ERROR;
				}
				tx = new TransactionCache();
				if(rolefunclist!= null && rolefunclist.size()>0){
					tx.delete(rolefunclist);
				}
				tx.delete(srole);
				tx.commit();
			}else{//批量删除
			int num = 0;
*/	
				List<SRoleFunc> rolefunc = new ArrayList<SRoleFunc>();
				List<SUserRole> roleuser = new ArrayList<SUserRole>();
				tx = new TransactionCache();
				for(SRole role : roleList){					
					List<SRoleFunc> rolefunclist = RoleItem.getSRoleFuncByRoleId(role.getUuid());
					List<SUserRole> userRoleList = UserItem.getSUserRoleByRoleId(role.getUuid());
					rolefunc.addAll(rolefunclist);
					roleuser.addAll(userRoleList);
					if(userRoleList!=null && userRoleList.size()>0){
						tx.delete(rolefunclist);
					}else if(rolefunclist!= null && rolefunclist.size()>0){
						tx.delete(rolefunclist);
					}
					tx.delete(role);
				}			
				tx.commit();
				
				for(SRole role : roleList){					
					if(roleuser!=null && roleuser.size()>0){
						for(SUserRole userrole:roleuser){
							LogPart lp = new LogPart();		
							lp.setOpObjType(SUserRole.class.getName());
							lp.setOpObjId(userrole.getUuid());
							lp.setRelObjType(SRole.class.getName());
							lp.setRelObjId(userrole.getRoleId());
							lp.setOpType(LogConstant.LOG_TYPE_DELETE);
							lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
							lp.setLogData("");
							lp.setResult(LogConstant.RESULT_SUCCESS);
							lp.save();
						}
					}else if(rolefunc!= null && rolefunc.size()>0){
							for(SRoleFunc funcrole:rolefunc){
							LogPart lp = new LogPart();		
							lp.setOpObjType(SRoleFunc.class.getName());
							lp.setOpObjId(funcrole.getUuid());
							lp.setRelObjType(SRole.class.getName());
							lp.setRelObjId(funcrole.getRoleId());
							lp.setOpType(LogConstant.LOG_TYPE_DELETE);
							lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
							lp.setLogData("");
							lp.setResult(LogConstant.RESULT_SUCCESS);
							lp.save();
						}
					}
					LogPart lprole = new LogPart();		
					lprole.setOpObjType(SRole.class.getName());
					lprole.setOpObjId(role.getUuid());
					lprole.setRelObjType(SRole.class.getName());
					lprole.setRelObjId(role.getUuid());
					lprole.setOpType(LogConstant.LOG_TYPE_DELETE);
					lprole.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lprole.setLogData("");
					lprole.setResult(LogConstant.RESULT_SUCCESS);
					lprole.save();
				}
//				if(num==roleList.size()){
//					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.NoDeleteRole"));	
//					return Action.ERROR;
//				}else if(num>=1 && num<roleList.size()){
//					String str=Integer.toString(num);
//					result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeleteRoleCount",new String[]{str}));
//					return Action.SUCCESS;
//				}
///*			}*/
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


}
