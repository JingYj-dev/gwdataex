/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file FuncItem.java creation date: [Jan 9, 2014 11:32:44 AM] by liuzhb
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.apps.base.sys.action.SysItem;
import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.apps.base.user.model.SUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/** 
 * <descption>function infomation</descption>
 * 
 * @author liuzhb
 * @version Jan 9, 2014 11:32:44 AM
 */

public class FuncItem {
	private static Log log = LogFactory.getLog(FuncItem.class);
	
	/**
	 * 根据funcId获取SFunc对象
	 * @author liuzhb on Jan 10, 2014 11:32:20 AM
	 * @param funcId
	 * @return
	 */
	public static SFunc getSFunc(String funcId){
		SFunc sFunc = null;
		try {
			QueryCache qc = new QueryCache(" from SFunc a where a.funcId=:funcId").setParameter("funcId", funcId);
			sFunc = (SFunc)qc.uniqueResult();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return sFunc;
	}
	/**
	 * 返回用户（userId）在系统（sysId）中所拥有的功能，结果类型为List<String>
	 * @author liuzhb on Jan 10, 2014 3:48:18 PM
	 * @param userId
	 * @param sysId
	 * @return
	 */
	public static List<String> getSFuncIdList(String userId, String sysId){
		List<String> result = null;
		try {
			SUser user=QueryCache.get(SUser.class, userId);
			List<String> roleIdlist =user.getRoleList().getListById(); //RoleItem.getSRoleIdList(userId, sysId);
			if(roleIdlist != null && roleIdlist.size() > 0){
				QueryCache qc = new QueryCache("select a.funcId from SRoleFunc a where a.roleId in (:roleId)").setParameter("roleId", roleIdlist);
				result = qc.listCache();
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}
	
	/**
	 * 返回用户（userId）在系统（sysId）中所拥有的功能，结果类型为Set<String>
	 * @author liuzhb on Jan 10, 2014 3:48:13 PM
	 * @param userId
	 * @param sysId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getSFuncIdSet(String userId, String sysId){
		Set<String> result =  new HashSet<String>();
		try {
			List<String> funcIdList = getSFuncIdList(userId, sysId);
			if(funcIdList != null && funcIdList.size() > 0){
				funcIdList= new QueryCache("SELECT funcId from SFunc Where uuid in (:uuid)").setParameter("uuid", funcIdList).listCache();
				/*Iterator<String> iter = funcIdList.iterator();				
				while(iter.hasNext()){
					String funcId = iter.next();
					result.add(funcId);
				}*/
				if(funcIdList != null && funcIdList.size() > 0)
					result.addAll(funcIdList);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}	
	
	/**
	 * 根据功能ID获取功能编码列表
	 * @param funcIdList 功能ID列表
	 * @return
	 */
	public static Set<String> getSFuncIdSet(List<String> funcIdList){
		Set<String> result =  new HashSet<String>();
		try {
			if(funcIdList != null && funcIdList.size() > 0){
				
				//方式一、直接从数据库获取
				/*funcIdList= new QueryCache("SELECT funcId from SFunc Where uuid in (:uuid)").setParameter("uuid", funcIdList).listCache();
				if(funcIdList != null && funcIdList.size() > 0)
					result.addAll(funcIdList);
				*/
				//方式二、从缓存获取
				 Iterator<String> iter = funcIdList.iterator();				
				while(iter.hasNext()){
					String funcId = iter.next();
					SFunc func=QueryCache.get(SFunc.class, funcId);
					if(func!=null && StringHelper.isNotEmpty(func.getFuncId()))
						result.add(func.getFuncId());
				} 
				
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}	
	/**
	 * 返回用户（userId）在系统（sysId）中所拥有的功能点，结果类型为List<String>
	 * @author liuzhb on Jan 10, 2014 3:48:18 PM
	 * @param userId
	 * @param sysId
	 * @return
	 */
	public static List<String> getSFuncActionIdList(String userId, String sysId){
		List<String> result = null;
		try {
			List<String> funcIdlist = FuncItem.getSFuncIdList(userId, sysId);
			if(funcIdlist != null && funcIdlist.size() > 0){
				QueryCache qc = new QueryCache("select a.actionCode from SFuncAction a where a.funcCode in (:funcCode)").setParameter("funcCode", funcIdlist);
				result = qc.list();
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}
	
	/**
	 * 根据给定的功能ID列表获取功能项编码
	 * @param funcIdlist
	 * @return
	 */
	public static Set<String> getSFuncActionIdList(List<String> funcIdlist){
		Set<String> actions =  new HashSet<String>();		
		try {
			if(funcIdlist != null && funcIdlist.size() > 0){
				QueryCache qc = new QueryCache("select a.actionCode from SFuncAction a where a.funcCode in (:funcCode)").setParameter("funcCode", funcIdlist);
				List<String> result  = qc.listCache();
				if(result!=null && !result.isEmpty()){
					actions.addAll(result);
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return actions;
	}
	
	/**
	 * 返回用户（userId）在系统（sysId）中所拥有的功能，结果类型为Set<String>
	 * @author liuzhb on Jan 10, 2014 3:48:13 PM
	 * @param userId
	 * @param sysId
	 * @return
	 */
	public static Set<String> getSFuncActionIdSet(String userId, String sysId){
		Set<String> result = null;
		try {
			List<String> funcActionIdList = getSFuncActionIdList(userId, sysId);
			if(funcActionIdList != null && funcActionIdList.size() > 0){
				Iterator<String> iter = funcActionIdList.iterator();
				result = new HashSet<String>();
				while(iter.hasNext()){
					String funcId = iter.next();
					result.add(funcId);
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}	
	
	public static List<SFunc> getSFuncBySysId(String sysId){
		List<SFunc>  funcList = null;
		try {
			QueryCache qc = new QueryCache("select a.uuid from SFunc a where a.sysId=:sysId ").setParameter("sysId", sysId);
			List<Integer> Ids = qc.listCache();
			if(Ids!= null){
				funcList= QueryCache.idToObj(SFunc.class, Ids);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return funcList;
	}	
	
	public static JSONArray getSubFunc(String parentId) throws JSONException{
		JSONArray jsonArray = new JSONArray();
		JSONObject one;
		if(StringHelper.isEmpty(parentId)){
			List<SSys> sysList = SysItem.getOpenSystems();
			if(sysList != null) 
				for(SSys item : sysList) {
					one = new JSONObject();
					one.put("id", item.getUuid());
					one.put("name", item.getName());
					one.put("pId", "0");
					one.put("sysId",item.getUuid());
					one.put("isParent", true);
					one.put("chkDisabled",true);
					jsonArray.add(one);
				}
		}else{
			List<SFunc> lst = getFuncs(parentId);
			List ids;
			if(lst != null) 
				for(SFunc item : lst) {
					one = new JSONObject();
					one.put("id", item.getUuid());
					one.put("name", item.getName());
					one.put("sysId", item.getSysId());
					one.put("pId", item.getParentId());
					one.put("url", item.getUrl());
					one.put("funcId",item.getFuncId());
					ids = getFuncIds(item.getUuid());
					if(ids == null || ids.size() < 1)
						one.put("isParent", false);
					else
						one.put("isParent", true);
					jsonArray.add(one);
				}
		}
		
		return jsonArray;
	}
	
	public static JSONArray getSubFuncMenu(String parentId,String funcId) throws JSONException{
		JSONArray jsonArray = new JSONArray();
		JSONObject one;
		if(StringHelper.isEmpty(parentId)){
			List<SSys> sysList = SysItem.getOpenSystems();
			if(sysList != null) 
				for(SSys item : sysList) {
					one = new JSONObject();
					one.put("id", item.getUuid());
					one.put("name", item.getName());
					one.put("pId", "0");
					one.put("sysId",item.getUuid());
					one.put("isParent", true);
					one.put("chkDisabled",true);
					jsonArray.add(one);
				}
		}else{
			List<SFunc> lst = getFuncs(parentId);
			List ids;
			if(lst != null) 
				for(SFunc item : lst) {
					one = new JSONObject();
					one.put("id", item.getUuid());
					one.put("name", item.getName());
					one.put("sysId", item.getSysId());
					one.put("pId", item.getParentId());
					one.put("url", item.getUrl());
					one.put("funcId",item.getFuncId());
					ids = getFuncIds(item.getUuid());
					if(ids == null || ids.size() < 1)
						one.put("isParent", false);
					else
						one.put("isParent", true);
					if(StringHelper.isNotEmpty(funcId)){
						if( funcId.equals(item.getFuncId()))
							one.put("checked", true);
					}
					jsonArray.add(one);
				}
		}
		
		return jsonArray;
	}
	
	private static QueryCache getFuncIdsQC(String parentId) {
		return new QueryCache("select a.uuid from SFunc a where a.parentId=:parentId order by a.orderNum")
		.setParameter("parentId", parentId);
	}
	
	public static List getFuncs(String parentId){
		QueryCache qc = getFuncIdsQC(parentId);
		List list = qc.list();
		return QueryCache.idToObj(SFunc.class, list);
	}
	public static List getFuncIds(String parentId){
		QueryCache qc = getFuncIdsQC(parentId);
		return qc.listCache();
	}

	/**
	 * @author liuzhb on Jan 9, 2014 11:32:44 AM
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SFunc sFunc = null;
//		//获取功能
//		sFunc = getSFunc("DJGN");
//		System.out.println(sFunc.getName());
		
		//获取功能id
		getSFuncIdList("1", "1");
	}

	
	public static List<String> getSFuncJoinList(String userId){
		List<String> funcIdList = new ArrayList<String>();
		try {
			SUser user=QueryCache.get(SUser.class, userId);
			List<SRole> roleList =user.getRoleList().getList(); //RoleItem.getSRoleIdList(userId, sysId);
			if(roleList != null && roleList.size() > 0){
				for(SRole role : roleList ){
					funcIdList.addAll(role.getRoleFuncList().getListById());
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return funcIdList;
	}
	
	public static Set<String> getSFuncActionJoinList(List<String> funcIdlist){
		Set<String> actions =  new HashSet<String>();		
		try {
			if(funcIdlist != null && funcIdlist.size() > 0){
				List<SFunc> funcList = QueryCache.idToObj(SFunc.class, funcIdlist);
				for(SFunc func : funcList){
					actions.addAll(func.getFuncActionList().getListById());
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return actions;
	}
}
