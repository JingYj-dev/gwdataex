package com.hnjz.apps.base.post.service;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.base.role.model.SRole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class SPostItem {
	private static Log log = LogFactory.getLog(SPostItem.class);
	public static boolean getSPostList(){
			QueryCache qc = new QueryCache("select a.uuid from SPost a");
			List<String> li = qc.list();
			if (li != null && li.size() > 0) {
				return true;
			}
		return false;
	}
	
	public static List<SRole> getRoleList(String postId) {
		if(StringHelper.isEmpty(postId)){
			return null;
		}
		QueryCache qc = new QueryCache("select a.roleId from SPostRole a where a.postId=:postId")
		.setParameter("postId", postId.trim());
		if(qc.list()!=null && qc.list().size()!=0){
			return QueryCache.idToObj(SRole.class, qc.list());
		}else{
			return null;
		}
	}
	
	
	
	public static String getOrgPostName(String orgid) {
		SOrg orgitem = QueryCache.get(SOrg.class, orgid);
		List<SPost> postList = orgitem.getPostList().getList();
		String postName = "";
		if(postList !=null ){
			for(SPost item : postList){
	        	postName += item.getName();
	        	postName += " ; ";
			}
	        if(postName.length()>2){
		        String post = postName.substring(0, postName.length()-2);
		        return post;
	        }else{
	        	return postName;
	        }
		}else{
			return null;
		}
	}
	
	
	public static List<String> queryOrgListByPost(String postId){
		List<String> idlist = new QueryCache("select a.uuid from SOrg a ").list();
		List<String> uplist = new java.util.LinkedList<String>();
		for(String orgId : idlist){
			SOrg org= QueryCache.get(SOrg.class, orgId);
			if(org!=null && org.getPostList().getListById().contains(postId)){
				uplist.add(orgId.trim());
			}
		}
		return uplist;		 
	}
	
	public static String getPostRoleName(String postId) {
		String roleName = "";
        List<SRole> postRoleList = SPostItem.getRoleList(postId);
        for(SRole role:postRoleList){
        	roleName += role.getName();
        	roleName += " ; ";
        }
        if(roleName.length()>2){
	        String post = roleName.substring(0, roleName.length()-2);
	        return post;
        }else{
        	return roleName;
        }

	}
	
	public static List<SPost> getPosts(){
		return QueryCache.idToObj(SPost.class, new QueryCache("select a.uuid from SPost a").listCache());
	}
}
