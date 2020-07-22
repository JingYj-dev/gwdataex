/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DelSysAction.java creation date: [2014-1-8 14:30:20] by Xingzhc
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.sys.action;

import com.hnjz.apps.base.func.action.FuncItem;
import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.role.action.RoleItem;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.apps.base.sys.model.SSys;
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
 * <p>删除系统</p>
 * 
 */
public class DelSys extends AdminAction {
	private static Log log = LogFactory.getLog(DelSys.class);
	private String ids;
	public DelSys(){
	}
	@Override
	protected String adminGo(){
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List listIds = StringHelper.strToList(ids);
			List<SSys> sysList = QueryCache.idToObj(SSys.class, listIds);
/*			if(sysList.size()==1){
				SSys sys = sysList.get(0);
				List<SFunc> funclist = FuncItem.getSFuncBySysId(sys.getUuid());
				List<SRole> roleList = RoleItem.getSRoleBySysId(sys.getUuid());
				if((funclist!=null&&funclist.size()>0)||(roleList!= null&&roleList.size()>0)){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.NoDelete"));	
					return Action.ERROR;
				}else{
					tx = new TransactionCache();
					tx.delete(sys);
					tx.commit();
				}
			}else{*/
				int num = 0;
				tx = new TransactionCache();
				for(SSys sys : sysList){					
						List<SFunc> funclist = FuncItem.getSFuncBySysId(sys.getUuid());
						List<SRole> roleList = RoleItem.getSRoleBySysId(sys.getUuid());
						if((funclist!=null&&funclist.size()>0)||(roleList!= null&&roleList.size()>0)){
							num++;
						}else{
							tx.delete(sys);
						}			
				}			
				tx.commit();
				
				for(SSys sys : sysList){					
					List<SFunc> funclist = FuncItem.getSFuncBySysId(sys.getUuid());
					List<SRole> roleList = RoleItem.getSRoleBySysId(sys.getUuid());
					if((funclist!=null&&funclist.size()>0)||(roleList!= null&&roleList.size()>0)){
					}else{
						LogPart lp = new LogPart();		
						lp.setOpObjType(SSys.class.getName());
						lp.setOpObjId(sys.getUuid());
						lp.setRelObjType(SSys.class.getName());
						lp.setRelObjId(sys.getUuid());
						lp.setOpType(LogConstant.LOG_TYPE_DELETE);
						lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
						lp.setLogData("");
						lp.setResult(LogConstant.RESULT_SUCCESS);
						lp.save();
						
					}			
				}	
				
				if(num==sysList.size()){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.NoDelete"));	
					return Action.ERROR;
				}else if(num>=1&&num<sysList.size()){
					String str=Integer.toString(num);
					result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeleteCount",new String[]{str}));
					return Action.SUCCESS;
				}
/*			}*/
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
