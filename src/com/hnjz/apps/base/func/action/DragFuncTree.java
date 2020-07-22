package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.sys.action.SysItem;
import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DragFuncTree extends AdminAction {
	private static Log log = LogFactory.getLog(DragFuncTree.class);
	private String targetId;  //如果modeType为inner,targetId为新的parentId;如果modeType为prev或者next,targetId为兄弟节点
	private String moveType;
	private String nodeIds;
	private void updFuncTree(String parentId,List<?> nodeIds){
		for(int i=0;i<nodeIds.size();i++){
			Object node = nodeIds.get(i);
			String nodeId = "";
			if(node instanceof SFunc){nodeId = ((SFunc)node).getUuid();}else{nodeId=node.toString();};
			TreeNode nodeDemo = FuncTree.getInstance().getTreeNode(nodeId);
			System.out.println(FuncTree.getInstance().getTreeNode(nodeId).getNodeId());
			FuncTree.getInstance().getRootNode();
        	FuncTree.getInstance().deleteTreeNode(nodeId);
			TreeNode newNode = new TreeNode();
			newNode.setNodeId(nodeId);
			newNode.setParentId(parentId.trim());
			FuncTree.getInstance().addTreeNode(newNode);
			FuncTree.getInstance().reloadTree();
		}
	}
	private List<SFunc> getOrderFuncs(String parentId,String targetId,List<String> nodeIds,String moveType){
		//parentId:新的父节点，targetId:目标节点Id，nodeIds:拖拽节点，moveType:拖拽的类型（inner,next,prev）
		if(nodeIds==null || nodeIds.size()==0){
			return null;
		}
		boolean Flag = false;
		SSys sys = QueryCache.get(SSys.class, parentId);
		List<SSys> sysList = SysItem.getSystems();   //获取所有系统Id
		List<TreeNode> brotherNodes = new ArrayList<TreeNode>();
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
				TreeNode node = FuncTree.getInstance().getTreeNode(brotherId);
				if(node!=null){
					brotherNodes.add(node);
				}
			}
		}else{
			TreeNode node = FuncTree.getInstance().getTreeNode(parentId);
			if(node !=null){
				brotherNodes = FuncTree.getInstance().getTreeNode(parentId).getChildren();
			}
		}
//		List<TreeNode> brotherNodes = FuncTree.getInstance().getTreeNode(parentId).getChildren();   //获得所有兄弟节点
		List<String> brotherIds = new ArrayList<String>();
		//filter nodeIds
		if(brotherNodes!=null){
			for(TreeNode node : brotherNodes){
				brotherIds.add(node.getNodeId());
			}
		}
		brotherIds.removeAll(nodeIds);
		//add nodeIds with order
		int targetIndex = brotherIds.indexOf(targetId);
		if("prev".equals(moveType)){
			if(targetIndex!=-1){
				targetIndex--;
			}
		}else if("inner".equals(moveType)){
			targetIndex = -1;
		}
		targetIndex++;
		for(String nodeId : nodeIds){
			brotherIds.add(targetIndex++, nodeId);
		}
		List<SFunc> brotherFuncs = new ArrayList<SFunc>();
		int orderNum = 1;
		for(String brotherId : brotherIds){
			SFunc func = QueryCache.get(SFunc.class, brotherId);
			func.setOrderNum(orderNum++);
			func.setParentId(parentId);
			brotherFuncs.add(func);
		}
		return brotherFuncs;
	}
	protected String adminGo() {
		TransactionCache tx = null;
		String parentId = targetId;
		try{
			if(StringHelper.isEmpty(moveType)||StringHelper.isEmpty(targetId)||StringHelper.isEmpty(nodeIds)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			if(moveType.equals("prev") || moveType.equals("next")){
				if(FuncTree.getInstance().getTreeNode(targetId)!=null){
					parentId = FuncTree.getInstance().getTreeNode(targetId).getParentId();
				}else{
					parentId = QueryCache.get(SFunc.class, targetId).getParentId();
				}
			}
			List<String> listIds = StringHelper.strToList(nodeIds.trim());
			List<SFunc> orderFuncs = getOrderFuncs(parentId, targetId, listIds, moveType);
			tx = new TransactionCache();
			tx.update(orderFuncs);
			tx.commit();
			updFuncTree(parentId,orderFuncs);
			FuncTree.getInstance().reloadTree();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
				
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getMoveType() {
		return moveType;
	}
	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}
	public String getNodeIds() {
		return nodeIds;
	}
	public void setNodeIds(String nodeIds) {
		this.nodeIds = nodeIds;
	}
}
