/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file GetFuncAction.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.role.action.GetRole;
import com.hnjz.apps.base.sys.action.SysItem;
import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>获得单个的功能</p>
 * 
 */
public class GetFunc extends AdminAction {
	private static Log log = LogFactory.getLog(GetRole.class);
	private String uuid ;
	private SFunc parentFunc,item=null;
	private String parentId;
	private String sysId;
	private String parentFuncName;
 
	public GetFunc(){
	}
	
	@Override
	protected String adminGo() {
		try{
			if (StringHelper.isNotEmpty(uuid)) {
				item = QueryCache.get(SFunc.class, uuid);	
				if(StringHelper.isNotEmpty(item.getRemark())){
					String str = RegexCheck.TagReverse(item.getRemark());
					item.setRemark(str);
				}
			}else{
				if(StringHelper.isEmpty(parentId)){
					parentId = FuncTree.getInstance().getRootNode().getNodeId();
				}
				SSys sys = QueryCache.get(SSys.class, parentId);
				List<SSys> sysList = SysItem.getSystems();   //获取所有系统Id
				List<TreeNode> brotherNodes = new ArrayList<TreeNode>();
				boolean Flag = false;
				if(sys!=null){
					if(sysList!=null && sysList.size()!=0){
						for(SSys sysDemo : sysList){
							if(sysDemo.getUuid().equals(sys.getUuid())){
								Flag = true;
							}
						}
					}
				}
				if(Flag){//如果选中的节点是系统而不是功能，需要从数据库中查询
					QueryCache qc = new QueryCache(
					" select a.uuid from SFunc a where a.parentId=:parentId ")
					.setParameter("parentId", parentId);
					List<String> brotherIds = qc.list();
					for(String brotherId : brotherIds){
						brotherNodes.add(FuncTree.getInstance().getTreeNode(brotherId));
					}
				}else{
					brotherNodes = FuncTree.getInstance().getTreeNode(parentId).getChildren();
				}
					int orderNum =0;
					if(brotherNodes !=null && brotherNodes.size()!=0){
						for(TreeNode node:brotherNodes){
								String nodeId = node.getNodeId();
								SFunc func = QueryCache.get(SFunc.class, nodeId);
								if(func != null) {
									if(orderNum < func.getOrderNum()){
										orderNum = func.getOrderNum();
									}
								}
						}
					}
					orderNum++;
					item = new SFunc();
					item.setOrderNum(orderNum);
					item.setParentId(parentId);
					item.setSysId(sysId);
					item.setFuncType(BaseEnvironment.FUNCTYPE_NORMAL);
				}
			parentFunc = QueryCache.get(SFunc.class,item.getParentId());
			if(parentFunc!=null){
				parentFuncName = parentFunc.getName();
				item.setSysId(parentFunc.getSysId());
				item.setParentId(parentId);
			}	
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
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

	public SFunc getItem() {
		return item;
	}

	public void setItem(SFunc item) {
		this.item = item;
	}	
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getParentFuncName() {
		return parentFuncName;
	}

	public void setParentFuncName(String parentFuncName) {
		this.parentFuncName = parentFuncName;
	}


}
