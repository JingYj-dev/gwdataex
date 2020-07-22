package com.hnjz.apps.base.common;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.apps.base.user.model.SUser;

import java.util.List;

/**
 * 系统用户操作定义,主要定义查询操作
 * @author paladin
 */
public class UserProvider  {
 
	/**
	 * 获取当前用户
	 * @return
	 */
	public static SUser currentUser(){
		return (SUser)WebBaseUtil.getCurrentUser();
	}
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	public static SUser getUser(String userId){
		return QueryCache.get(SUser.class, userId);
	}
	
	/**
	 * 获取用户信息（带机构ID）：默认用户属于某一机构下
	 * @param userId
	 * @return
	 */
	public static SUser getUserAndOrgId(String userId){
		SUser user= QueryCache.get(SUser.class, userId);
		//查询带缓存
		if(user!=null){
			String orgId = (String)new QueryCache("select a.orgId from SUser a where a.uuid=:userId")
			.setParameter("userId", userId).setMaxResults(1).uniqueResultCache();
			if(orgId!=null)
				user.setOrgId(orgId);
		}
		return user;
	}
	
	/**
	 * 获取用户机构信息
	 * @param userId
	 * @return
	 */
	public static List<SOrg> getOrgList(String userId){
		List idlist = new QueryCache("select a.orgId from SUser a where a.uuid=:userId")
		.setParameter("userId", userId).list();
		List<SOrg> uplist = QueryCache.idToObj(SOrg.class, idlist);
		return uplist;
	}
	public static List<String> getOrgs(String userId){
		List idlist = new QueryCache("select a.orgId from SUser a where a.uuid=:userId")
		.setParameter("userId", userId).list();
		return idlist;
	}

	public static SOrg getUserOrg(String userId){
		List idlist = new QueryCache("select a.orgId from SUser a where a.uuid=:userId")
		.setParameter("userId", userId).list();
		SOrg org = null;
		if(idlist!=null&&idlist.size()>0){
			 org = QueryCache.get(SOrg.class, idlist.get(0).toString());
		}
		if(org != null)
			return org;
		else
			return null;
	}


	/**
	 * 获取用户岗位信息
	 * @param userId
	 * @return
	 */
	public static List<SPost> getPostList(String userId){
		if(StringHelper.isEmpty(userId)){
			return null;
		}
		List postList = QueryCache.get(SUser.class,userId).getPostList().getList();
		return postList;
	}

	public static List<String> getPostIdList(String userId){
		if(StringHelper.isEmpty(userId)){
			return null;
		}
		List idlist = QueryCache.get(SUser.class,userId).getPostList().getListById();
		return idlist;
	}


	/**
	 * 获取用户角色信息
	 * @param userId
	 * @return
	 */
	public static List<SRole> getRoleList(String userId){
		if(StringHelper.isEmpty(userId)){
			return null;
		}
		List idlist = QueryCache.get(SUser.class,userId).getRoleList().getList();
		return idlist;
	}
	public static List<String> getRoleIdList(String userId){
		if(StringHelper.isEmpty(userId)){
			return null;
		}
		List idlist = QueryCache.get(SUser.class,userId.trim()).getRoleList().getListById();
		return idlist;
	}

	/**
	 * 获取用户功能信息
	 * @param userId
	 * @return
	 */
	public static List<SFunc> getFuncList(String userId){
		return null;
	}

	/**
	 * 获取用户功能点列表信息
	 * @param userId
	 * @return
	 */
	public static java.util.Set<String> getFuncPointList(String userId){
		return null;
	}

	/**
	 * 查询用户信息
	 * @param params 查询参数
	 * @param page   分页参数，可空
	 * @return
	 */
	public static List<SUser> queryOrgUserList(SUser params,Page page){
		return null;
	}

	/**
	 * 查询特定机构某些岗位下的用户
	 * @param orgId  机构id
	 * @param postId  岗位id
	 * @param page    分页参数，可空
	 * @return
	 */
	public static List<SUser> queryPostUserList(String orgId,String[] postId,Page page){
		return null;
	}

	/**
	 * 指定部门岗位下是否存在人员
	 * @param orgId
	 * @param postId
	 * @return
	 */
	public static boolean existOrgPostUsers(String orgId,String postId){
		if(StringHelper.isEmptyByTrim(orgId) || StringHelper.isEmpty(postId)){
			return false;
		}
		String sql = "select count(a.uuid) from SUserPost a,SUser b where a.userId=b.uuid and  a.postId = :postId and a.orgId = :orgId and b.delFlag='2' and b.openFlag != '2'";
		Number num=(Number)new QueryCache(sql)
		.setParameter("postId", postId).setParameter("orgId", orgId).uniqueResult();
		if(num!=null && num.intValue()>0)
			return true;
		return false;
	}

	/**
	 * 通过岗位获取该岗位下的所有用户
	 * @param postId 岗位ID
	 * @return
	 */
	public static List<SUser> queryUserListByPost(String postId){
		List<String> idlist = new QueryCache("select a.userId from SUserPost a,SUser b where a.userId=b.uuid and  a.postId = :postId and b.delFlag='2' and b.openFlag != '2'")
		.setParameter("postId", postId).list();
		List<SUser> uplist = new java.util.LinkedList<SUser>();
		for(String userId : idlist){
			SUser user=getUserAndOrgId(userId);
			if(user!=null){
				uplist.add(user);
			}
		}
		return uplist;
	}
	public static List<SUser> queryUserListByPostOrgs(String postId, List<String> orgIds){
		List<SUser> uplist = new java.util.LinkedList<SUser>();
		if(orgIds==null || orgIds.isEmpty())
			return uplist;
		List<String> idlist = new QueryCache("select a.userId from SUserPost a ,SUser b where a.userId=b.uuid and a.postId = :postId and a.orgId in (:orgId) and b.delFlag='2' and b.openFlag != '2'")
		.setParameter("postId", postId).setParameter("orgId", orgIds).list();		
		for(String userId : idlist){
			SUser user=getUserAndOrgId(userId);
			if(user!=null){
				uplist.add(user);
			}
		}
		return uplist;		 
	}
 
}