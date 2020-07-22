package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.org.model.SOrgPost;
import com.hnjz.apps.base.org.model.SUserOrg;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.model.SUserPost;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file orgItem.java
 * http://www.css.com.cn
 */

public class OrgItem {
	private static Log log = LogFactory.getLog(OrgItem.class);
	public static JSONObject getRootOrgan() throws JSONException{
		OrgTree gt = OrgTree.getInstance();
		TreeNode root = gt.getRootNode();
		SOrg item = QueryCache.get(SOrg.class, root.getNodeId());
		if(item == null)
			return null;
		JSONObject one = new JSONObject();
		one.put("id", item.getUuid());
		one.put("name", item.getName());
		one.put("pId", item.getParentId());
		one.put("isParent", true);
		one.put("open", true);
		return one;
	}
	// 获得孩子节点，封装成jsonArray
	public static JSONArray getSubOrgan(String parentId) throws JSONException{
		OrgTree gt = OrgTree.getInstance();
		TreeNode tn =null;
		JSONArray orgTree = new JSONArray();
		JSONObject one;
		if(StringHelper.isEmpty(parentId)){
			tn=gt.getRootNode();
			SOrg item = QueryCache.get(SOrg.class,tn.getNodeId());
			if(item != null){
				one = new JSONObject();
				one.put("id", item.getUuid());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				one.put("isParent", true);
				one.put("open", true);
				orgTree.add(one);
			}
		}else {
			 tn=gt.getTreeNode(parentId);
			}		
		List<TreeNode> lst = tn.getChildren();
		for (TreeNode node : lst){
			SOrg item = QueryCache.get(SOrg.class, node.getNodeId());
			if(item != null){
				one = new JSONObject();
				one.put("id", node.getNodeId());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				one.put("isParent", !node.isLeaf());
				if(StringHelper.isEmpty(item.getParentId())){
					one.put("checked", true);
				}
				orgTree.add(one);
			}
		}
		

		return orgTree;
	}
	public static List getOrganIds(String parentId){
		QueryCache qc = new  QueryCache("select a.uuid from SOrg a where a.parentId=:parentId and a.delFlag='2'")
		.setParameter("parentId", parentId);
		return qc.list();
	}
	public static SOrg getOrg(String orgId){
		SOrg o;
		if(StringHelper.isEmpty(orgId))
			o = new SOrg();
		else
			o = QueryCache.get(SOrg.class, orgId); 
		return o;
	}
	
	public static List<SOrgPost> getPostByOrgIdList(String orgId){
		List<SOrgPost>  orgpostList = null;
		try {
			QueryCache qc = new QueryCache("select a.uuid from SOrgPost  a where a.orgId=:orgId ").setParameter("orgId", orgId);
			List<Integer> Ids = qc.listCache();
			if(Ids!= null)
				orgpostList= QueryCache.idToObj(SOrgPost.class, Ids);
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return orgpostList;
	}
	
	public static List<SOrgPost> getPostByPostIdList(String postId){
		List<SOrgPost>  orgpostList = null;
		try {
			QueryCache qc = new QueryCache("select a.uuid from SOrgPost  a where a.postId=:postId ").setParameter("postId", postId);
			List<Integer> Ids = qc.listCache();
			if(Ids!= null)
				orgpostList= QueryCache.idToObj(SOrgPost.class, Ids);
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return orgpostList;
	}
	
	
	public static List<SUser> getUserByOrgIdList(String orgId){
		List listt = new QueryCache("select a.uuid from SUser  a where a.orgId=:orgId and a.delFlag='2'").setParameter("orgId", orgId).listCache();
		return QueryCache.idToObj(SUser.class, listt);
	}
	
	public static List<SUser> getUserObjByOrgIdList(String orgId){
		List<SUser> relist = new ArrayList();
		List listt = new QueryCache("select a.uuid from SUserOrg  a where a.orgId=:orgId ").setParameter("orgId", orgId).listCache();
		List<SUserOrg> list = QueryCache.idToObj(SUserOrg.class, listt);
		if(list !=null) {
			for(SUserOrg uo : list) {
				SUser u = QueryCache.get(SUser.class, uo.getUserId());
				if(u != null){
					relist.add(u);
				}
			}
		}
		return relist;
	}
	public static List<SUserPost> getUserByOrgPostIdList(String orgId,String postId){
		List listt = new QueryCache("select a.uuid from SUserPost  a where a.orgId=:orgId and a.postId =:postId").setParameter("postId", postId).setParameter("orgId", orgId).listCache();
		return QueryCache.idToObj(SUserPost.class, listt);
	}
	public static JSONArray getUserJsonByOrgIdList(String orgId){
		JSONArray jaTree = new JSONArray();
		List listt = new QueryCache("select a.uuid from SUserOrg  a where a.orgId=:orgId ").setParameter("orgId", orgId).listCache();
		List<SUserOrg>  list = QueryCache.idToObj(SUserOrg.class, listt);
		if(list !=null) {
			for(SUserOrg uo : list) {
				SUser u = QueryCache.get(SUser.class, uo.getUserId());
				if(u != null){
					JSONObject one = new JSONObject();
					one.put("id", u.getUserId());
					one.put("name", u.getRealName());
					one.put("pId", orgId);
					one.put("isParent", false);
					jaTree.add(one);
				}
			}
		}
		return jaTree;
	}
	
	public static JSONArray getOrgPostTree(String orgId) throws JSONException{
		List postids = new QueryCache("select a.postId from SOrgPost a where a.orgId=:orgId")
			.setParameter("orgId", orgId).list();
		JSONArray jaTree = new JSONArray();
		JSONObject one = new JSONObject();
		one.put("id", "0");
		one.put("name", "所有岗位");
		one.put("pId", "-1");
		one.put("isParent", true);
		one.put("use", false);
		jaTree.add(one);
		List list = new QueryCache("select a.uuid from SPost a").list();
		List<SPost> lst = QueryCache.idToObj(SPost.class, list);
		if(lst != null) 
			for(SPost item : lst) {
				one = new JSONObject();
				one.put("id", item.getUuid());
				one.put("name", item.getName());
				one.put("pId", "0");
				one.put("isParent", false);
				one.put("use", true);
				if(postids.contains(item.getUuid()))
					one.put("checked", true);
				jaTree.add(one);
			}
		return jaTree;
	}
	public static JSONArray getOrgTree() throws JSONException{//10
		OrgTree gt = OrgTree.getInstance();
		List<TreeNode> lst = new ArrayList();
		lst.add(gt.getRootNode());
		lst.addAll(gt.getRootNode().getAllChildren());
		JSONObject one;
		JSONArray orgTree = new JSONArray();
		for (TreeNode o : lst) {
			SOrg item = QueryCache.get(SOrg.class, o.getNodeId());
			if(item == null)
				continue;
			one = new JSONObject();
			one.put("id", item.getUuid());
			one.put("name", item.getName());
			one.put("pId", item.getParentId());
			one.put("isParent", true);
			orgTree.add(one);
		}
		return orgTree;
	}

	public static JSONArray getOrgUserTree() throws JSONException{//10
		OrgTree gt = OrgTree.getInstance();
		List<TreeNode> lst = new ArrayList();
		lst.add(gt.getRootNode());
		lst.addAll(gt.getRootNode().getAllChildren());
		JSONObject one;
		JSONArray orgTree = new JSONArray();
		for (TreeNode o : lst) {
			SOrg item = QueryCache.get(SOrg.class, o.getNodeId());
			if(item == null)
				continue;
			one = new JSONObject();
			one.put("id", item.getUuid());
			one.put("name", item.getName());
			one.put("pId", item.getParentId());
			one.put("isParent", true);
			orgTree.add(one);
			if(o.getChildren()==null || o.getChildren().size() == 0){
				orgTree.addAll(getUserJsonByOrgIdList(item.getUuid()));
			}
		}
		return orgTree;
	}
	public static JSONArray getOrgRadio(String orgId) throws JSONException{//10
		OrgTree gt = OrgTree.getInstance();
		List<TreeNode> lst = new ArrayList();
		lst.add(gt.getRootNode());
		lst.addAll(gt.getRootNode().getAllChildren());
		JSONObject one;
		JSONArray orgTree = new JSONArray();
		for (TreeNode o : lst) {
			SOrg item = QueryCache.get(SOrg.class, o.getNodeId());
			if(item == null)
				continue;
			one = new JSONObject();
			one.put("id", item.getUuid());
			one.put("name", item.getName());
			one.put("pId", item.getParentId());
			if(StringHelper.isEmpty(item.getParentId())){
				one.put("open", true);
			}
			if(o.getChildren() == null || o.getChildren().size() == 0) 
				one.put("isParent", false);
			else
				one.put("isParent", true);
			if(orgId != null && orgId.equals(item.getUuid()))
				one.put("checked", true);
			orgTree.add(one);
		}
		return orgTree;
	}
	public static JSONObject getRootOrgRadio(String parentId, String orgId) throws JSONException{//10
		SOrg item = QueryCache.get(SOrg.class, parentId);
		if(item == null)
			return null;
		JSONObject one = new JSONObject();
		one.put("id", item.getUuid());
		one.put("name", item.getName());
		one.put("pId", item.getParentId());
		one.put("isParent", true);
		if(orgId != null && orgId.equals(item.getUuid())){
			one.put("checked", true);
		}
		one.put("open", true);
		return one;
	}
	public static JSONArray getSubOrgRadio(String parentId, String orgId) throws JSONException{
		List uuids = OrgItem.getOrganIds(parentId);
		List<SOrg> lst = QueryCache.idToObj(SOrg.class, uuids);
		JSONArray orgTree = new JSONArray();
		JSONObject one;
		Object id;
		List ids;
		if(lst != null)
			for(SOrg item : lst) {
				one = new JSONObject();
				one.put("id", item.getUuid());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				ids = OrgItem.getOrganIds(item.getUuid());
				if(ids == null || ids.size() < 1) 
					one.put("isParent", false);
				else
					one.put("isParent", true);
				if(orgId != null && orgId.equals(item.getUuid()))
					one.put("checked", true);
				orgTree.add(one);
			}
		return orgTree;
	}
}
