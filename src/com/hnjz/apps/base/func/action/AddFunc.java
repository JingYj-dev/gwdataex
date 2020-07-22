/**
 * Copyright (c) Css Team
 * All rights reserved.
 * This file AddFuncAction.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * <p>添加功能</p>
 */
public class AddFunc extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(AddFunc.class);
	private SFunc item;
	public AddFunc(){
		this.item = new SFunc();
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getFuncId())||StringHelper.isEmpty(item.getName())||StringHelper.isEmpty(item.getSysId())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			//查询该功能是否存在，此处不使用缓存
			Object id = new QueryCache("select a.uuid from SFunc a where a.funcId=:funcId and a.sysId=:sysId")
				.setParameter("funcId",item.getFuncId()).setParameter("sysId",item.getSysId()).setMaxResults(1).uniqueResult();
			if(id != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("sysmgrMsg.funcExsit"));				
				return Action.ERROR;
			}
			//设置此功能排序编号，此处不使用缓存
			Object orderNum = new QueryCache("select a.orderNum from SFunc a order by a.orderNum desc")
			.setMaxResults(1).uniqueResult();
			if(orderNum==null)
				item.setOrderNum(1);
			else{
				item.setOrderNum(Integer.parseInt(String.valueOf(orderNum))+1);
			}
			if(StringHelper.isEmpty(item.getFuncType())){
				item.setFuncType(BaseEnvironment.FUNCTYPE_NORMAL);
			}
			//保存功能
			tx = new TransactionCache();
			item.setOpenFlag("1");
			tx.save(item);
			tx.commit();
			
			TreeNode node = new TreeNode();
			node.setNodeId(item.getUuid());
			node.setParentId(item.getParentId());
			FuncTree.getInstance().addTreeNode(node);
			FuncTree.getInstance().reloadTreeCache();
			
			LogPart lp = new LogPart();		
			lp.setOpObjType(SFunc.class.getName());
			lp.setOpObjId(item.getUuid());
			lp.setRelObjType(SFunc.class.getName());
			lp.setRelObjId(item.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_ADD);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(item));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"),item.toJson());
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
