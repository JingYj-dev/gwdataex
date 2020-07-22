/*
 * Created on 2006-7-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.org.action.OrgItem;
import com.hnjz.apps.base.org.action.OrgTree;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.apps.base.sys.action.SysItem;
import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.util.CookieUtil;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.model.SUserPost;
import com.hnjz.apps.base.user.model.SUserRole;
import com.opensymphony.webwork.ServletActionContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

//import com.hnjz.apps.oa.admin.oaform.model.OaForm;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class UserItem {
	private static Log log = LogFactory.getLog(UserItem.class);
	public static String getUserImgUrl() {
		String imgUrl = "";
		return imgUrl;
	}
	public static void clearCookies() {
		CookieUtil.SetCookies(Environment.Cookie_UserID, "0", 0, ServletActionContext.getResponse());
	}
	
	/**
	 * 登录失败超过固定次数后，改变安全状态为关闭
	 */
	public static void updateSafeStatus(SUser sUser) {
			if(sUser != null){
				TransactionCache tc = new TransactionCache();
				sUser.setOpenFlag("3");
				tc.update(sUser);
				tc.commit();
			}
		
		
	}
	
	/**
	 * 登录失败超过固定次数后，改变安全状态为关闭
	 */
	public static void updateActiveStatus(SUser sUser) {
			if(sUser != null){
				TransactionCache tc = new TransactionCache();
				sUser.setActiveStatus("1");
				tc.update(sUser);
				tc.commit();
			}
	}
	
	/**
	 * 当登录失败的时候更新最后登录时间以及失败次数，登录成功后失败次数清零
	 * @author liuzhb on Jan 9, 2014 4:45:12 PM
	 * @param loginName
	 * @return
	 */
	public static void updateFailedLoginCount(SUser sUser) {
		
			if(sUser != null){
				TransactionCache tc = new TransactionCache();
				sUser.setFailedLoginCount(sUser.getFailedLoginCount() + 1);
				tc.update(sUser);
				tc.commit();
			}
		//}
	}
	/**
	 * 登录成功后，总登录次数加1，失败次数清零
	 * @author liuzhb on Jan 9, 2014 4:59:49 PM
	 * @param loginName
	 */
	public static void updateTotalLoginCount(String userId) {
		SUser sUser = QueryCache.get(SUser.class, userId);
		if(sUser != null){
			TransactionCache tc = new TransactionCache();
			sUser.setLastLoginTime(Calendar.getInstance().getTime());
			sUser.setFailedLoginCount(0);
			sUser.setTotalLoginCount(sUser.getTotalLoginCount() + 1);
			tc.update(sUser);
			tc.commit();
		}
	}
	
	
	public static void updateTotalLoginCount(SUser sUser) {
		if(sUser != null){
			TransactionCache tc = new TransactionCache();
			sUser.setLastLoginTime(Calendar.getInstance().getTime());
			sUser.setFailedLoginCount(0);
			sUser.setTotalLoginCount(sUser.getTotalLoginCount() + 1);
			tc.update(sUser);
			tc.commit();
		}
	}
	
	private final static String USER_ID_CACHE_NAME = "user_id_cached/";
	private static String  getUserIdByName(String loginName){
		if(StringHelper.isEmpty(loginName))
			return null;
		String key=null;
		try {
			key = com.hnjz.util.Md5Util.getMD5(USER_ID_CACHE_NAME+loginName.trim());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(key==null)
			return null;
		String userId = (String) MemCachedFactory.get(key);
		if(StringHelper.isEmpty(userId)){
			QueryCache qc = new QueryCache("select a.uuid from SUser a where a.loginName=:loginName and a.delFlag=:delFlag").setParameter("delFlag", Environment.UN_DELETE_STATE).setParameter("loginName", loginName);
			userId = (String)qc.uniqueResult();
			if(userId!=null){
				MemCachedFactory.set(key, userId);
			}
		}
		return userId;		
	}
	/**
	 * 获取登录失败次数
	 * 用户ID查询优化 ( by shenhc)(2014-09-28) 
	 * @author liuzhb on Jan 9, 2014 5:00:15 PM
	 * @param loginName
	 */
	public static SUser getFailedLoginCount(String loginName) {	
		String userId = getUserIdByName(loginName);
		/*QueryCache qc = new QueryCache("select a.uuid from SUser a where a.loginName=:loginName and a.delFlag=:delFlag").setParameter("delFlag", Environment.UN_DELETE_STATE).setParameter("loginName", loginName);
		String userId = (String)qc.uniqueResult();
		*/
		if(StringHelper.isNotEmpty(userId)){
			//QueryCache onlyCache=new QueryCache();
			//onlyCache.remove(SUser.class, userId);
			SUser sUser = QueryCache.get(SUser.class, userId);
			if(sUser != null){
				//如果当前日期与用户最后登录日期不在同一天，则登录失败次数清零
				if(sUser.getLastLoginTime()!=null){
					boolean flag = DateUtil.compareDate(sUser.getLastLoginTime(), Calendar.getInstance().getTime());
					if(flag == false){
						TransactionCache tc = new TransactionCache();
						sUser.setFailedLoginCount(0);
						sUser.setLastLoginTime(Calendar.getInstance().getTime());
						tc.update(sUser);
						tc.commit();
					}
				}
				return sUser;
			}
		}
		return null;
	}
	
	public static List<SUserRole> getSUserRoleByRoleId(String roleId){
		List<SUserRole>  userRoleList = null;
		try {
			QueryCache qc = new QueryCache("select a.uuid from SUserRole  a where a.roleId=:roleId ").setParameter("roleId", roleId);
			List<Integer> Ids = qc.listCache();
			if(Ids!= null)
				userRoleList= QueryCache.idToObj(SUserRole.class, Ids);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return userRoleList;
	}
	
	public static JSONArray getUserRoleTree(String userId) throws JSONException{
		List rids = new QueryCache("select a.roleId from SUserRole a where a.userId=:userId")
			.setParameter("userId", userId).list();
		List<SSys> sysList = SysItem.getSystems();
		JSONArray jsonArray = new JSONArray();
		JSONObject one;
		if(sysList != null) 
			for(SSys item : sysList) {
				one = new JSONObject();
				one.put("id", item.getUuid());
				one.put("name", item.getName());
				one.put("pId", "0");
				one.put("sysId",item.getUuid());
				one.put("isParent", true);
				one.put("use", false);
				jsonArray.add(one);
				List list = new QueryCache("select a.uuid from SRole a where a.sysId=:sysId")
				.setParameter("sysId", item.getUuid()).list();
				List<SRole> lst = QueryCache.idToObj(SRole.class, list);
				if(lst != null) 
					for(SRole item2 : lst) {
						one = new JSONObject();
						one.put("id", item2.getUuid());
						one.put("name", item2.getName());
						one.put("sysId", item2.getSysId());
						one.put("pId", item2.getSysId());
						one.put("isParent", false);
						one.put("use", true);
						if(rids.contains(item2.getUuid()))
							one.put("checked", true);
						jsonArray.add(one);
					}
				
			}
		return jsonArray;
	}
	
	
	public static List<SUserPost> getSUserPostByPostId(String postId){
		List<SUserPost>  userPostList = null;
		try {
			QueryCache qc = new QueryCache("select a.uuid from SUserPost  a where a.postId=:postId ").setParameter("postId", postId);
			List<Integer> Ids = qc.listCache();
			if(Ids!= null)
				userPostList= QueryCache.idToObj(SUserPost.class, Ids);
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return userPostList;
	}
	
	public static JSONArray getUserPostTree(String userId) throws JSONException{
		List postids = new QueryCache("select a.postId from SUserPost a where a.userId=:userId")
			.setParameter("userId", userId).list();
		JSONArray jaTree = new JSONArray();
		JSONObject one = new JSONObject();
		one.put("id", "0");
		one.put("name", "所有岗位");
		one.put("pId", "-1");
		one.put("isParent", true);
		one.put("open", true);
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
	public static JSONObject getRootUserOrg(String parentId) throws JSONException{
		SOrg item = QueryCache.get(SOrg.class, parentId);
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
	public static JSONArray getSubUserOrg(String parentId) throws JSONException{
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
				if(ids == null || ids.size() < 1) {
					List<SUser> ulist = getUsers(parentId);
					if(ulist != null){
						for(SUser o : ulist) {
							one = new JSONObject();
							one.put("id", o.getUuid());
							one.put("name", o.getRealName());
							one.put("pId", parentId);
							one.put("isParent", false);
							orgTree.add(one);
						}
					}
				}
				orgTree.add(one);
			}
		return orgTree;
	}
	public static List getUsers(String parentId){
//		List list = new QueryCache("select a.uuid from s_user a left join s_user_org b on a.uuid=b.userId "
//				+ "where b.orgId=:parentId and a.delFlag=:delFlag order by a.orderNum", true)
//				.setParameter("parentId", parentId).setParameter("delFlag", Environment.UN_DELETE_STATE).list();
		List list = new QueryCache("select a.uuid from s_user a where a.orgId=:parentId and a.delFlag=:delFlag order by a.orderNum", true)
		.setParameter("parentId", parentId).setParameter("delFlag", Environment.UN_DELETE_STATE).list();
		return QueryCache.idToObj(SUser.class, list);
	}
	public static List getUsers(List<String> parentIds){
		List list = new QueryCache("select a.uuid from s_user a where a.orgId in (:parentId) and a.delFlag=:delFlag order by a.orderNum", true)
		.setParameter("parentId", parentIds).setParameter("delFlag", Environment.UN_DELETE_STATE).list();
		return QueryCache.idToObj(SUser.class, list);
	}
	public static Map<String,List<SUser>> getUsers(){
		Map<String,List<SUser>> result = new HashMap<String,List<SUser>>();
		List<Object[]> list = new QueryCache("select a.orgId,a.uuid from s_user a  order by a.orderNum", true).list();
		if(list!=null && list.size()>0){
			for(Object[] os : list){
				List<SUser> users = result.get(os[0]);
				if(users==null){
					users = new ArrayList<SUser>();
				}
				users.add(QueryCache.get(SUser.class, (String)os[1]));
				result.put((String)os[0], users);
			}
		}
		return result;
	}
	public static List getPosts(String parentId){
		List list = new QueryCache("select a.uuid from s_post a left join s_org_post b on a.uuid=b.postId "
				+ "where b.orgId=:parentId ", true)
		.setParameter("parentId", parentId).list();
		return QueryCache.idToObj(SPost.class, list);
	}
	public static Map<String,List<SPost>> getPosts(){
		Map<String,List<SPost>> result = new HashMap<String,List<SPost>>();
		List<Object[]> list = new QueryCache("select b.orgId,a.uuid from s_post a , s_org_post b where a.uuid=b.postId ", true).list();
		if(list!=null && list.size()>0){
			for(Object[] os : list){
				List<SPost> posts = result.get(os[0]);
				if(posts==null){
					posts = new ArrayList<SPost>();
				}
				posts.add(QueryCache.get(SPost.class, (String)os[1]));
				result.put((String)os[0], posts);
			}
		}
		return result;
	}
	
	
	// 按人授权 ，机构下的人进行授权   type =3  实现 机构数下的人查询 by xingzhc
	public static JSONArray getOrgUserTree() throws JSONException{
	    // 获得维护权限中的关联机构的Id的集合
		JSONArray jaTree = new JSONArray();
		JSONObject one = new JSONObject();
		OrgTree gt = OrgTree.getInstance();
		SOrg orgitem = QueryCache.get(SOrg.class, gt.getRootNode().getNodeId());
		if(orgitem == null)
			return jaTree;
		one = new JSONObject();
		one.put("id", orgitem.getUuid());
		one.put("name", orgitem.getName());
		one.put("pId", orgitem.getParentId());
		one.put("isParent", true);
		one.put("open",true);
		jaTree.add(one);
		List<TreeNode> lst = gt.getRootNode().getAllChildren();
		for (TreeNode node : lst){
			SOrg item = QueryCache.get(SOrg.class, node.getNodeId());
			if(item != null){
				one = new JSONObject();
				one.put("id", node.getNodeId());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				one.put("isParent", true);
				one.put("use", true);
				jaTree.add(one);
				JSONArray userTree = getUserJsonByOrgIdList(node.getNodeId());
				jaTree.addAll(userTree);
			}
		}
		return jaTree;
	}
	
	public static JSONArray getUserJsonByOrgIdList(String orgId){
		List<SUser> list  = OrgItem.getUserByOrgIdList(orgId);
		JSONArray jaTree = new JSONArray();
		if(list != null) {
			for(SUser user : list){
				JSONObject one = new JSONObject();
				one.put("id", user.getUserId());
				one.put("name", user.getRealName());
				one.put("pId", orgId);
				one.put("isParent", false);
				one.put("use", true);
				jaTree.add(one);
			}
		}
		return jaTree;
	}
	// 机构下  查询  by xingzhc 单选，用于组织机构树的选择 组织门户的添加
	public static JSONArray getOrgTree() throws JSONException{
	    // 获得维护权限中的关联机构的Id的集合
		JSONArray jaTree = new JSONArray();
		JSONObject one = new JSONObject();
		OrgTree gt = OrgTree.getInstance();
		SOrg orgitem = QueryCache.get(SOrg.class, gt.getRootNode().getNodeId());
		if(orgitem == null)
			return jaTree;
		one = new JSONObject();
		one.put("id", orgitem.getUuid());
		one.put("name", orgitem.getName());
		one.put("pId", orgitem.getParentId());
		one.put("isParent", true);
		one.put("open",true);
		jaTree.add(one);
		List<TreeNode> lst = gt.getRootNode().getAllChildren();
		for (TreeNode node : lst){
			SOrg item = QueryCache.get(SOrg.class, node.getNodeId());
			if(item != null){
				one = new JSONObject();
				one.put("id", node.getNodeId());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				one.put("isParent", !node.isLeaf());
				one.put("use", true);
				jaTree.add(one);
			}
		}
		return jaTree;
	}
	
	public static JSONArray getOrgTreeByOrgId(String orgId) throws JSONException{
	    // 获得维护权限中的关联机构的Id的集合 by xingzhc 
		JSONArray jaTree = new JSONArray();
		JSONObject one = new JSONObject();
		OrgTree gt = OrgTree.getInstance();
		SOrg orgitem = QueryCache.get(SOrg.class, orgId);
		if(orgitem == null)
			return jaTree;
		one = new JSONObject();
		one.put("id", orgitem.getUuid());
		one.put("name", orgitem.getName());
		one.put("pId", orgitem.getParentId());
		one.put("isParent", true);
		one.put("open",true);
		jaTree.add(one);
		TreeNode tn = gt.getTreeNode(orgId);
		List<TreeNode> lst = tn.getAllChildren();
		for (TreeNode node : lst){
			SOrg item = QueryCache.get(SOrg.class, node.getNodeId());
			if(item != null){
				one = new JSONObject();
				one.put("id", node.getNodeId());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				one.put("isParent", !node.isLeaf());
				one.put("use", true);
				jaTree.add(one);
			}
		}
		return jaTree;
	}
	public static JSONArray getOrgUserTreeByOrgId(String orgId) throws JSONException{
	    // 获得维护权限中的关联机构的Id的集合 by xingzhc 
		JSONArray jaTree = new JSONArray();
		JSONObject one = new JSONObject();
		OrgTree gt = OrgTree.getInstance();
		SOrg orgitem = QueryCache.get(SOrg.class, orgId);
		if(orgitem == null)
			return jaTree;
		one = new JSONObject();
		one.put("id", orgitem.getUuid());
		one.put("name", orgitem.getName());
		one.put("pId", orgitem.getParentId());
		one.put("isParent", true);
		one.put("open",true);
		jaTree.add(one);
		TreeNode tn = gt.getTreeNode(orgId);
		List<TreeNode> lst = tn.getAllChildren();
		for (TreeNode node : lst){
			SOrg item = QueryCache.get(SOrg.class, node.getNodeId());
			if(item != null){
				one = new JSONObject();
				one.put("id", node.getNodeId());
				one.put("name", item.getName());
				one.put("pId", item.getParentId());
				one.put("isParent", true);
				one.put("use", true);
				jaTree.add(one);
				JSONArray userTree = getUserJsonByOrgIdList(node.getNodeId());
				jaTree.addAll(userTree);
			}
		}
		return jaTree;
	}
	
}
