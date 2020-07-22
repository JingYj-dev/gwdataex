package com.hnjz.apps.base.common;

import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.org.action.OrgTree;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.user.model.SUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * 机构操作提供者
 * @author paladin
 */
public class OrgProvider {	 
	/**
	 * 查询机构下所有用户
	 * @param orgId  单位ID
	 * @return
	 */
	public  static List<SUser> queryOrgUserList(String orgId){
		List idlist = new QueryCache("select a.uuid from SUser a where a.orgId=:orgId")
		.setParameter("orgId", orgId).listCache();
		List<SUser> uplist = QueryCache.idToObj(SUser.class, idlist);
		return uplist;
	}
	public static List<String> queryUserId(String orgId){
		if(StringHelper.isEmpty(orgId)){
			return null;
		}
		List userIdList = QueryCache.get(SOrg.class,orgId).getUserList().getListById();
		return userIdList;
	}

	/**
	 * 获取机构岗位列表
	 * @param orgId
	 * @return
	 */
	public static List<SPost> queryPost(String orgId){
		if(StringHelper.isEmpty(orgId)){
			return null;
		}
		List postList = QueryCache.get(SOrg.class,orgId).getPostList().getList();
		return postList;
	}

	/**
	 * 获取机构岗位ID列表
	 * @param orgId
	 * @return
	 */
	public static List<String> queryPostId(String orgId){
		if(StringHelper.isEmpty(orgId)){
			return null;
		}
		List postIdList = QueryCache.get(SOrg.class,orgId).getPostList().getListById();
		return postIdList;
	}

	/**
	 * 获取下级机构ID列表
	 * @param orgId
	 * @return
	 */
	public static List<String> querySubOrgIdList(String orgId){
		return  new QueryCache("select a.uuid from SOrg a where a.parentId = :orgId")
		.setParameter("orgId", orgId).list();
	}

	/**
	 * 返回子机构列表
	 * @param orgId
	 * @return
	 */
	public static List<SOrg> querySubOrgList(String orgId){
		List<String> orgIds= new QueryCache("select a.uuid from SOrg a where a.parentId = :orgId")
		.setParameter("orgId", orgId).list();
		return QueryCache.idToObj(SOrg.class,orgIds);
	}

	/**
	 * 机构信息分页查询
	 * @param param  查询参数
	 * @param page 分页参数
	 * @return
	 */
	public static List<SOrg> queryOrgList(SOrg param,Page page){
		return null;
	}
	
	
	/**
	 * 根据机构ID查询机构信息
	 * @param orgId
	 * @return
	 */
	public static SOrg getOrg(String orgId){		
		return QueryCache.get(SOrg.class, orgId);
	}
	/**
	 * 获取机构树，用于一次性构建Ztree树节点<br/>包含所有子节点
	 * @return json 字符串
	 */
	public static String geOrgTreeRoot(){
		/*
		JSONObject jo = OrgItem.getRootOrgan();
		JSONArray ja = OrgItem.getSubOrgan(OrgTree.getInstance().getRootNode().getNodeId());
		if(jo != null) 
			 ja.add(jo);
		return ja.toString();
		*/
		JSONArray tree = new JSONArray();		 
		TreeNode tn = OrgTree.getInstance().getRootNode();
		if(tn!=null){
			JSONObject jo = null;
			SOrg item = QueryCache.get(SOrg.class, tn.getNodeId());
			if(item!=null){
				jo = new JSONObject();
				jo.put("id", item.getUuid());
				jo.put("name", item.getName());
				jo.put("pId", item.getParentId());
				jo.put("isParent", !tn.isLeaf());
				jo.put("open", true);
			}
			if(jo != null) 
				 tree.add(jo);
		}
		List<TreeNode> lst = tn.getAllChildren();	
		 
		JSONObject one;
		for (TreeNode node : lst){
			SOrg item = QueryCache.get(SOrg.class, node.getNodeId());
			if(item != null){
				//机构节点
				one = new JSONObject();
				one.put("id", item.getUuid());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				one.put("isParent", !node.isLeaf());
				one.put("open", false);
				tree.add(one);
			}
		}
		return tree.toString();
	}
	
	/**
	 * 构建机构树
	 * @param parentId 机构父节点
	 * @param isAllChild 是否全机构子树
	 * @return
	 */
	public static JSONArray getOrgTree(String parentId,boolean isAllChild){
		return getOrgTree(parentId, isAllChild, false);
	}
	
	public static JSONArray getOrgTree(String parentId,boolean isAllChild,boolean isOpen){
		OrgTree gt = OrgTree.getInstance();
		JSONArray tree = new JSONArray();
		if(StringHelper.isEmpty(parentId)){
			parentId = gt.getRootNode().getNodeId();
		}
		TreeNode tn = gt.getTreeNode(parentId);
		List<TreeNode> lst = null;
		if(isAllChild)
			lst = tn.getAllChildren();
		else
			lst = tn.getChildren();
		JSONObject one;
		for (TreeNode node : lst){
			SOrg item = QueryCache.get(SOrg.class, node.getNodeId());
			if(item != null){
				//机构节点
				one = new JSONObject();
				one.put("id", item.getUuid());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				one.put("isParent", !node.isLeaf());
				one.put("open", isOpen);
				tree.add(one);
			}
		}
		return tree;
	}
}