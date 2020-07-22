package com.hnjz.apps.base.org.action;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DragOrgTree extends AdminAction {
	private static Log log = LogFactory.getLog(DragOrgTree.class);
	private String targetId;  //如果modeType为inner,targetId为新的parentId;如果modeType为prev或者next,targetId为兄弟节点
	private String moveType;
	private String nodeIds;
	private void updOrgTree(String parentId,List<?> nodeIds){
		for(int i=0;i<nodeIds.size();i++){
			Object node = nodeIds.get(i);
			String nodeId = "";
			if(node instanceof SOrg){nodeId = ((SOrg)node).getUuid();}else{nodeId=node.toString();};
        	OrgTree.getInstance().deleteTreeNode(nodeId);
			TreeNode newNode = new TreeNode();
			newNode.setNodeId(nodeId);
			newNode.setParentId(parentId.trim());
			OrgTree.getInstance().addTreeNode(newNode);
		}
	}
	private List<SOrg> getOrderOrgs(String parentId,String targetId,List<String> nodeIds,String moveType){
		//parentId:新的父节点，targetId:目标节点Id，nodeIds:拖拽节点，moveType:拖拽的类型（inner,next,prev）
		if(nodeIds==null || nodeIds.size()==0){
			return null;
		}
		List<TreeNode> brotherNodes = OrgTree.getInstance().getTreeNode(parentId).getChildren();   //获得所有兄弟节点
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
		List<SOrg> brotherOrgs = new ArrayList<SOrg>();
		int orderNum = 1;
		for(String brotherId : brotherIds){
			SOrg org = QueryCache.get(SOrg.class, brotherId);
			org.setOrderNum(orderNum++);
			org.setParentId(parentId);
			brotherOrgs.add(org);
		}
		return brotherOrgs;
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
				parentId = OrgTree.getInstance().getTreeNode(targetId).getParentId();
			}
			List<String> listIds = StringHelper.strToList(nodeIds.trim());
			List<SOrg> orderOrgs = getOrderOrgs(parentId, targetId, listIds, moveType);
			tx = new TransactionCache();
			tx.update(orderOrgs);
			tx.commit();
			updOrgTree(parentId,orderOrgs);
			OrgTree.getInstance().reloadTree();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
			
/*            if(moveType.equals("prev") || moveType.equals("next")){
            	newParentId = OrgTree.getInstance().getTreeNode(newParentId).getParentId();
			}
            //更新数据库的操作
			StringBuffer sb =  new StringBuffer("update SOrg a set a.parentId =:newParentId where a.uuid =:nodeId ");
			Map params=new HashMap();
			params.put("newParentId", newParentId.trim());
			params.put("nodeId", nodeId);
			tx = new TransactionCache();
			tx.executeUpdate(sb.toString(), params);
			tx.commit();
			//更新树节点

			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;*/
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
