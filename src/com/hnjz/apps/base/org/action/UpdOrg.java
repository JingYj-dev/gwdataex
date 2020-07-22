/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file UpdOrg.java 
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
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * <p>更新组织</p>
 * 
 */
public class UpdOrg extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdOrg.class);
	private SOrg item;
	public UpdOrg() {
		this.item = new SOrg(); 
	}
	
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			
			if (StringHelper.isEmpty(item.getUuid())
					||StringHelper.isEmpty(item.getName())
					||StringHelper.isEmpty(item.getOpenFlag())
					||StringHelper.isEmpty(item.getCode())) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			SOrg oldItem = QueryCache.get(SOrg.class, item.getUuid());
			if (oldItem == null) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.recordnotfound"));
				return Action.ERROR;
		 	}
			List<TreeNode> allorglist = OrgTree.getInstance().getRootNode().getAllChildren();
			for(TreeNode org:allorglist){
				SOrg tmp = QueryCache.get(SOrg.class, org.getNodeId());
				//所有机构的编码都不能相同
				if(item.getCode().equals(tmp.getCode()) && (!item.getOrgId().equals(tmp.getOrgId())) ){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("systemMsg.codeExist"));				
					return Action.ERROR;
				}else if(item.getName().equals(tmp.getName())  && (!item.getOrgId().equals(tmp.getOrgId()))){
					TreeNode nodetmp = OrgTree.getInstance().getTreeNode(tmp.getUuid());
					TreeNode nodeold = OrgTree.getInstance().getTreeNode(item.getUuid());
					if(nodetmp.getParent()!=null && nodeold.getParent()!=null && nodetmp.getParentId().equals(nodeold.getParentId())){
						result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("systemMsg.nameExist"));				
						return Action.ERROR;
					}
				}
			}
			if(checkUpdate(oldItem, item)){
				oldItem.setCode(item.getCode());
				oldItem.setName(item.getName());
				oldItem.setOpenFlag(item.getOpenFlag());
				tx = new TransactionCache();
				tx.update(oldItem);
				tx.commit();
				
				LogPart lp = new LogPart();		
				lp.setOpObjType(SOrg.class.getName());
				lp.setOpObjId(item.getUuid());
				lp.setRelObjType(SOrg.class.getName());
				lp.setRelObjId(item.getUuid());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(oldItem.toJson().toString());
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
				
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"),oldItem.toJson());
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
	public boolean checkUpdate(SOrg oldItem,SOrg tmp){
		if(tmp.getCode().equals(oldItem.getCode())&&tmp.getName().equals(oldItem.getName())&& tmp.getOpenFlag().equals(oldItem.getOpenFlag())&& tmp.getParentId().equals(oldItem.getParentId()))
			return false;
		else{
			return true; 
		 }
	}
	@Override
	public Object getModel() {
		return item;
	}

	public SOrg getItem() {
		return item;
	}

	public void setItem(SOrg item) {
		this.item = item;
	}

}
