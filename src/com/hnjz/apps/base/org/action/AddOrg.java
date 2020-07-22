
/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file AddOrg.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.tree.TreeNode;
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

import java.util.List;

/**
 * <p>添加组织</p>
 * 
 */
public class AddOrg extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(AddOrg.class);
	private SOrg item;
	
	public AddOrg(){
		this.item = new SOrg(); 
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getName())||StringHelper.isEmpty(item.getCode())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			List<TreeNode> allorglist = OrgTree.getInstance().getRootNode().getAllChildren();
			for(TreeNode org:allorglist){
				SOrg tmp = QueryCache.get(SOrg.class, org.getNodeId());
				//所有机构的编码都不能相同
				if(tmp.getCode().equals(item.getCode()) ){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("systemMsg.codeExist"));				
					return Action.ERROR;
				}else if(tmp.getName().equals(item.getName()) ){
					TreeNode nodetmp = OrgTree.getInstance().getTreeNode(tmp.getUuid());
					if(nodetmp.getParent()!=null  && nodetmp.getParentId().equals(item.getParentId())){
						result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("systemMsg.nameExist"));				
						return Action.ERROR;
					}
				}
			}
			tx = new TransactionCache();
			item.setDelFlag("2");
			tx.save(item);
			tx.commit();
			TreeNode node = new TreeNode();
			node.setNodeId(item.getUuid());
			node.setParentId(item.getParentId());
			OrgTree.getInstance().addTreeNode(node);
			OrgTree.getInstance().reloadTreeCache();
			
			
			LogPart lp = new LogPart();		
			lp.setOpObjType(SOrg.class.getName());
			lp.setOpObjId(item.getUuid());
			lp.setRelObjType(SOrg.class.getName());
			lp.setRelObjId(item.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_ADD);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(item));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.toJson());
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
		// TODO Auto-generated method stub
		return item;
	}

	public SOrg getItem() {
		return item;
	}
	public void setItem(SOrg item) {
		this.item = item;
	}

}
