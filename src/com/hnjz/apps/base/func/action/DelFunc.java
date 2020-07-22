package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.func.model.SFuncAction;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.role.model.SRoleFunc;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DelFuncAction.java creation date: [Jan 9, 2014 1:46:27 PM] by Xingzhc
 * http://www.css.com.cn
 */
public class DelFunc extends AdminAction {
	private static Log log = LogFactory.getLog(DelFunc.class);
	private String ids;
	public DelFunc(){
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List<String> listIds = StringHelper.strToList(ids);
			List<String> delList = new ArrayList<String>();
			List<SFunc> funcList = QueryCache.idToObj(SFunc.class, listIds);
				int num = 0;
				tx = new TransactionCache();
				for(SFunc func : funcList){					
						List<SFuncAction> funcActionlist = FuncPointItem.getFuncActionByCode(func.getUuid());
						List<SRoleFunc> roleFuncList = SRoleFunc.getRoleFuncByfuncId(func.getUuid());
						if((funcActionlist!=null && funcActionlist.size()>0)|| (FuncTree.getInstance().getTreeNode(func.getUuid().trim()).isLeaf() == false)){
							num++;
						}else{
							delList.add(func.getUuid().trim());
							tx.delete(func);
							tx.delete(roleFuncList);
							
							
							LogPart lp = new LogPart();		
							lp.setOpObjType(SFunc.class.getName());
							lp.setOpObjId(func.getUuid());
							lp.setRelObjType(SFunc.class.getName());
							lp.setRelObjId(func.getUuid());
							lp.setOpType(LogConstant.LOG_TYPE_DELETE);
							lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
							lp.setLogData("");
							lp.setResult(LogConstant.RESULT_SUCCESS);
							lp.save();
							
							
							for(SRoleFunc rolefunc : roleFuncList){
								LogPart lprolefunc = new LogPart();		
								lprolefunc.setOpObjType(SRoleFunc.class.getName());
								lprolefunc.setOpObjId(rolefunc.getUuid());
								lprolefunc.setRelObjType(SFunc.class.getName());
								lprolefunc.setRelObjId(func.getUuid());
								lprolefunc.setOpType(LogConstant.LOG_TYPE_DELETE);
								lprolefunc.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
								lprolefunc.setLogData("");
								lp.setResult(LogConstant.RESULT_SUCCESS);
								lprolefunc.save();
							}

							
							
						}			
				}			
				tx.commit();
				if(num>0){
					String str=Integer.toString(num);
					result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeleteFuncCount",new String[]{str}),delList);
					return Action.SUCCESS;
					
				}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"),listIds);
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
