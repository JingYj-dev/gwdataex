package com.hnjz.apps.base.user.spi.impl;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.spi.IUserListener;
import com.hnjz.common.exception.base.CssBaseException;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.db.query.QueryCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class InitUserFromDbListener extends AbstractConfigurablePlugin implements IUserListener {
	private static Log log = LogFactory.getLog(InitUserFromDbListener.class);
	@Override
	public void afterLogin(SUser sUser) throws CssBaseException {
		if(sUser==null)
			return;
		//获取功能ID列表：数据库+缓存
//		List<String> funcIdList = getSFuncIdList(sUser.getUuid());
//		List<String> funcIdList = getSFuncIdNewList(sUser.getUuid());
//		//设置用户（userId）所拥有的功能:数据库+缓存
//		sUser.setFunctions(getSFuncCodeSet(funcIdList));
		List<SFunc> funcList = getSFuncIdNewList(sUser.getUuid());  //已去重的所有开启功能id
//		Set<String> functions = new HashSet<String>();
//		functions.addAll(funcIdList);
//		sUser.setFunctions(functions);
		sUser.setFunctions(getSFuncCodeNewSet(funcList));
		//设置用户（userId）在系统（sysId）中所拥有的功能点
		sUser.setFuncActions(getSFuncActionCodeNewList(funcList));		 
	}
	
	/**
	 * 获取用户拥有的功能ID,直接从数据库获取
	 * @param userId 用户ID
	 * @return
	 */
	private List<String> getSFuncIdList(String userId){
		List<String> funcIdList = new ArrayList<String>();
		try {
			try {
//				QueryCache qc = new QueryCache("select a.funcId from SRoleFunc a,SUserRole b,SRole c where a.roleId = b.roleId and b.roleId = c.uuid and b.userId=:userId and c.delFlag='2' and c.openFlag='1'").setParameter("userId", userId);
//				return  qc.listCache();	
				QueryCache qcRoleFunc = new QueryCache("select a.funcId from SRoleFunc a,SUserRole b,SRole c where a.roleId = b.roleId and b.roleId = c.uuid and b.userId=:userId and c.delFlag='2' and c.openFlag='1'").setParameter("userId", userId);
				QueryCache qcPostFunc = new QueryCache("select a.funcId from SRoleFunc a,SUserPost b,SPostRole c,SRole d where a.roleId = c.roleId and c.postId = b.postId and b.userId=:userId and d.delFlag='2' and d.openFlag='1' and a.roleId = d.uuid ").setParameter("userId", userId);
//				return  qc.listCache();	
				List<String> funcList = new ArrayList<String>();
				funcList.addAll(qcRoleFunc.listCache());
				funcList.addAll(qcPostFunc.listCache());
				return funcList;
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return funcIdList;
	}
	private List<SFunc> getSFuncIdNewList(String userId){//获取用户所有关联的功能id
		List<SFunc> funcList = new ArrayList<SFunc>();
			try {
				SUser currentUser = QueryCache.get(SUser.class, userId);
				List<String> roleIdList = new ArrayList<String>();//用户直接关联的角色id+用户通过岗位关联的角色id
				List<String> roleList = currentUser.getRoleList().getListById();
				roleIdList.addAll(roleList);
				List<SPost> postList = currentUser.getPostList().getList();
				for(SPost post:postList){
					roleIdList.addAll(post.getPostRoleList().getListById());
				}
				List<SRole> userroleList = QueryCache.idToObj(SRole.class, roleIdList); //用户直接或间接关联的角色对象
				//对所有角色含有的功能进行去重
				for(SRole role:userroleList){
					List<SFunc> rolefuncId = role.getRoleFuncList().getList();
					if(rolefuncId!=null && rolefuncId.size()!=0 ){
						for(SFunc func:rolefuncId){
							if("1".equals(func.getOpenFlag()) && !funcList.contains(func)){
								funcList.add(func);
							}
						}
					}
						
				}
				
//				for(SRole role : userroleList){
//					funcList.addAll(role.getRoleFuncList().getListById());
//				}
				
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
			return funcList;
	}
	private Set<String> getSFuncCodeSet(List<String> funcIdList){
		Set<String> result =  new HashSet<String>();
		try {
			if(funcIdList != null && funcIdList.size() > 0){				
				//方式一、直接从数据库获取
				 funcIdList= new QueryCache("SELECT funcId from SFunc Where uuid in (:uuid) and openFlag = '1'").setParameter("uuid", funcIdList).listCache();
				if(funcIdList != null && funcIdList.size() > 0)
					result.addAll(funcIdList);			
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}	
	private Set<String> getSFuncCodeNewSet(List<SFunc> funcList){
		Set<String> result =  new HashSet<String>();
		try {
			if(funcList != null && funcList.size() > 0){		
				for(SFunc func:funcList){
					result.add(func.getFuncId());			
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}	
	
	private   Set<String> getSFuncActionCodeList(List<String> funcIdList){
		Set<String> result =  new HashSet<String>();
		try {
			if(funcIdList != null && funcIdList.size() > 0){				
				//方式一、直接从数据库获取
				 funcIdList= new QueryCache("SELECT actionCode from SFuncAction Where funcCode in (:uuid) and openFlag = '1'").setParameter("uuid", funcIdList).listCache();
				if(funcIdList != null && funcIdList.size() > 0)
					result.addAll(funcIdList);			
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}
	private   Set<String> getSFuncActionCodeNewList(List<SFunc> funcList){
		Set<String> result =  new HashSet<String>();
		try {
			if(funcList != null && funcList.size() > 0){
				for(SFunc func:funcList){
					List<String> funcActions = func.getFuncActionList().getListById();
					if( funcActions!=null && funcActions.size()!=0){//功能项去重
						for(String funcaction:funcActions){
							if(!result.contains(funcaction)){
								result.add(funcaction);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}
	@Override
	public void beforeLogout(SUser user) throws CssBaseException {}

	@Override
	protected void doConfig(Map<String, String> configMap) {}
}
