/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetOrg.java 
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * <p>获得单个组织</p>
 * 
 */
public class GetOrg extends AdminAction {
	private static Log log = LogFactory.getLog(GetOrg.class);
	private String uuid;
	private String parentId;
	private SOrg item;
	public GetOrg(){
	}
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
		try {
			if(StringHelper.isNotEmpty(uuid)){
				item = QueryCache.get(SOrg.class, uuid);
			}else{
				if(StringHelper.isEmpty(parentId)){
					parentId = OrgTree.getInstance().getRootNode().getNodeId();
				}
				List<TreeNode> brotherNodes = OrgTree.getInstance().getTreeNode(parentId).getChildren();
				int orderNum =0;
				if(brotherNodes !=null && brotherNodes.size()!=0){
					for(TreeNode node:brotherNodes){
							String nodeId = node.getNodeId();
							SOrg org = QueryCache.get(SOrg.class, nodeId);
							if(orderNum < org.getOrderNum()){
								orderNum = org.getOrderNum();
							}
							
					}
				}
				orderNum++;
				item = new SOrg();
				item.setOrderNum(orderNum);
				item.setParentId(parentId);
				item.setOpenFlag("1");
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			setMessage(Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
		
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public SOrg getItem() {
		return item;
	}
	public void setItem(SOrg item) {
		this.item = item;
	}

}
