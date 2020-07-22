/**
\ * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file RoleItem.java creation date: [Jan 10, 2014 11:39:51 AM] by liuzhb
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.role.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.func.action.FuncTree;
import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.apps.base.role.model.SRoleFunc;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.user.model.SUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/** 
 * <descption>function infomation</descption>
 * 
 * @author liuzhb
 * @version Jan 10, 2014 11:39:51 AM
 */ 

public class RoleItem {
	private static Log log = LogFactory.getLog(RoleItem.class);
	
	public static List<String> getSRoleIdList(String userId, String sysId){
		List<String> list = null;
		try {
			QueryCache qc = new QueryCache(" select a.roleId from SUserRole a where a.userId=:userId and a.sysId=:sysId ")
				.setParameter("userId", userId)
				.setParameter("sysId", sysId);
			list = qc.listCache();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return list;
	}
	public static List<String> getSRoleFuncIdList(String funcId){
		List<String> list = null;
		try {
			QueryCache qc = new QueryCache(" select a.uuid from SRoleFunc a where a.funcId=:funcId ")
				.setParameter("funcId", funcId);
			list = qc.list();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return list;
	}
	public static Long getSRoleCount(){
		Long roleCount = null;
		try {
			QueryCache qc = new QueryCache("select count(a.uuid) from SRole  a ");
			List<Integer> Ids = qc.listCache();
			if(Ids!= null)
				roleCount  = (Long)qc.uniqueResult();
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return roleCount;
	}
	
	public static List<SRole> getSRoleBySysId(String sysId){
		List<SRole>  roleList = null;
		try {
			QueryCache qc = new QueryCache("select a.uuid from SRole  a where a.sysId=:sysId ").setParameter("sysId", sysId);
			List<Integer> Ids = qc.listCache();
			if(Ids!= null)
				roleList= QueryCache.idToObj(SRole.class, Ids);
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return roleList;
	}
	
	public static boolean getRoleUseFlag(String userId,String roleId){
		if(StringHelper.isEmpty(userId)){
			return false;
		}
		SUser sUser = QueryCache.get(SUser.class,userId);
		if(sUser.getRoleList().getListById().contains(roleId)){
			return true;
		}
		return false;
	}
	
	
	public static Long getSRoleCount(String userType){
		Long roleCount = null;
		try{
			//roleCount = (Long)new QueryCache("select count(a.uuid) from SRole a ").uniqueResult(); 
			StringBuffer ss = new StringBuffer(" select count(a.uuid) from SRole a where 1=1 ");
			//业务类型用户显示业务类型角色，管理类推
			if(StringHelper.isEmpty(userType) || BaseEnvironment.USERTYPE_NORMAL.equals(userType)){
				ss.append(" and a.roleType = '" +BaseEnvironment.ROLETYPE_NORMAL+ "' ");
			}else{
				ss.append(" and a.roleType = '" +BaseEnvironment.ROLETYPE_ADMIN+ "' ");
			}
			roleCount  = (Long)new QueryCache(ss.toString()).uniqueResult();
		}catch (Exception ex){
			log.error(ex.getMessage(),ex);
		}
		return roleCount;
	}
	
	public static List<SRoleFunc> getSRoleFuncByRoleId(String roleId){
		List<SRoleFunc>  rolefuncList = null;
		try {
			QueryCache qc = new QueryCache("select a.uuid from SRoleFunc  a where a.roleId=:roleId ").setParameter("roleId", roleId);
			List<Integer> Ids = qc.listCache();
			if(Ids!= null)
				rolefuncList= QueryCache.idToObj(SRoleFunc.class, Ids);
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return rolefuncList;
	}
	
	public static JSONArray getSubFunc(String sysId, String roleId) throws JSONException{
		List fids = new QueryCache("select a.funcId from SRoleFunc a where a.roleId=:roleId")
			.setParameter("roleId", roleId).list();
		JSONArray jaTree = new JSONArray();
		String roleType = QueryCache.get(SRole.class,roleId).getRoleType();
		loadSubFunc(sysId,roleType, fids, jaTree);
		return jaTree;
	}
	public static JSONArray getFunc(SRole role) throws JSONException{
		List fids = role.getRoleFuncList().getListById();
		JSONArray jaTree = new JSONArray();
		String roleType = role.getRoleType();
		loadSubFunc(role.getSysId(),roleType, fids, jaTree);
		return jaTree;
	}
	private static List<TreeNode> getSubFuncNode(List<String> funcIds){
		List<TreeNode> lst = null;
		if(funcIds!=null && funcIds.size()>0){
			lst = new ArrayList<TreeNode>();
			FuncTree funcTree = FuncTree.getInstance();
			for(String funcId : funcIds){
				TreeNode funcNode = funcTree.getTreeNode(funcId);
				if(funcNode!=null){
					lst.add(funcNode);//add own
					List<TreeNode> children = funcNode.getAllChildren();
					if(children!=null && children.size()>0){
						lst.addAll(children);
					}
				}
				
			}
		}
		return lst;
	}
	public static void loadSubFunc(String parentId,String roleType, List fids, JSONArray jaTree) throws JSONException{
		StringBuffer sb = new StringBuffer("select a.uuid from SFunc a where a.parentId=:parentId ");
		//业务类型角色显示业务类型功能，管理类推
		if(StringHelper.isEmpty(roleType) || BaseEnvironment.ROLETYPE_NORMAL.equals(roleType)){
			sb.append(" and a.funcType = '" +BaseEnvironment.FUNCTYPE_NORMAL+ "' ");
		}else{
			sb.append(" and a.funcType = '" +BaseEnvironment.FUNCTYPE_ADMIN+ "' ");
		}
		sb.append(" order by a.orderNum ");
		List<String> ids = new QueryCache(sb.toString()).setParameter("parentId", parentId).list();
		List<TreeNode> funcNodes = getSubFuncNode(ids);
		if(funcNodes!=null && funcNodes.size()>0){
			JSONObject one;
			for(TreeNode funcNode : funcNodes){
				SFunc func = QueryCache.get(SFunc.class, funcNode.getNodeId());
				if(func!=null){
					boolean auth = false;
					//业务类型角色显示业务类型功能，管理类推
					if(StringHelper.isEmpty(roleType) || BaseEnvironment.ROLETYPE_NORMAL.equals(roleType)){
						auth = BaseEnvironment.FUNCTYPE_NORMAL.equals(func.getFuncType());
					}else{
						auth = BaseEnvironment.FUNCTYPE_ADMIN.equals(func.getFuncType());
					}
					if(!auth)continue;
					one = new JSONObject();
					one.put("id", func.getUuid());
					one.put("name", func.getName());
					one.put("pId", func.getParentId());
					if(fids.contains(func.getUuid()))
						one.put("checked", true);
					jaTree.add(one);
				}
				
			}
		}
	}
	public static JSONArray getSubUserRole(String userId, String sysId) throws JSONException{
		List jsids = new QueryCache("select a.roleId from SUserRole a where a.userId=:userId")
			.setParameter("userId", userId).listCache();
		List ids = new QueryCache("select a.uuid from SRole a where a.sysId=:sysId and a.openFlag='1'")
			.setParameter("sysId", sysId).listCache();
		List<SRole> lst = QueryCache.idToObj(SRole.class, ids);
		JSONArray jaTree = new JSONArray();
		JSONObject one;
		Object id;
		if(lst != null) 
			for(SRole item : lst) {
				one = new JSONObject();
				one.put("id", item.getUuid());
				one.put("name", item.getName());
				one.put("pId", "0");
				one.put("nocheck", false);
				one.put("isParent", false);
				if(jsids.contains(item.getUuid())){
					one.put("chkDisabled", true);
					one.put("checked", true);
				}
				jaTree.add(one);
			}
		return jaTree;
	}
	public static JSONObject getRootUserRole() throws JSONException{//10
		JSONObject one = new JSONObject();
		one.put("id", "0");
		one.put("name", "所有角色");
		one.put("pId", "00");
		one.put("isParent", true);
		one.put("nocheck", true);
		return one;
	}
	/**
	 * @author liuzhb on Jan 10, 2014 11:39:51 AM
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getSRoleIdList("1", "1");
	}

}
