package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.org.model.SOrgPost;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.user.action.DelUserPost;
import com.hnjz.apps.base.user.model.SUserPost;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelOrgPost extends AdminAction{
	private static Log log = LogFactory.getLog(DelUserPost.class);
	private String ids;//postId
	private String orgId;
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
		if(StringHelper.isEmpty(ids)||StringHelper.isEmpty(orgId)){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
			return Action.ERROR;		
		}
		SOrg sOrg = QueryCache.get(SOrg.class, orgId);
		List<String> listIds = StringHelper.strToList(ids);
		List<SPost> postList = QueryCache.idToObj(SPost.class, listIds);
		List<String> listids = new ArrayList<String>();
		int num = 0;
		for(String sb : listIds){
			listids.add(sb.trim());
		}
		//判断机构是否存在用户已被分配岗位
		if(postList.size()==1){//单条记录删除
			String postid = listIds.get(0);
			List<SUserPost> tmp = OrgItem.getUserByOrgPostIdList(orgId, postid.trim());
			
			if(tmp!=null && tmp.size()>0){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("systemMsg.NoDeletePost"));				
				return Action.ERROR;
			}
		}else{//批量删除
				for(String postid:listIds){
					List<SUserPost> tmp = OrgItem.getUserByOrgPostIdList(orgId, postid.trim());
					if(tmp!=null && tmp.size()>0){
						num++;
						listids.remove(postid.trim());
					}
				}
		}
		if(listids.size()==0){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,Messages.getString("systemMsg.NoDeletePost"));				
			return Action.ERROR;
		}
//		List<String> postObjId = sOrg.getPostList().getListById();
//		List<String> postIds = new ArrayList<String>();
//		for(String postid : postObjId){
//			postIds.add(postid.trim());
//		}
		tx = new TransactionCache();
		StringBuffer sb =  new StringBuffer("delete from SOrgPost a where a.orgId= :orgId and a.postId in (:listIds)");
		Map params=new HashMap();
		params.put("orgId", orgId);
		params.put("listIds", listids);
		tx.executeUpdate(sb.toString(), params);
		tx.commit();
		sOrg.getPostList().removeAll();
		
		for(String orgpost : listids){
			LogPart lp = new LogPart();		
			lp.setOpObjType(SOrgPost.class.getName());
			lp.setOpObjId(orgpost);
			lp.setRelObjType(SOrg.class.getName());
			lp.setRelObjId(orgId);
			lp.setOpType(LogConstant.LOG_TYPE_DELETE);
			lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
			lp.setLogData("");
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
		}
		

		if(num != 0){
			String str_num=Integer.toString(num);
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeletePostCount",new String[]{str_num}));
			return Action.SUCCESS;
		}
		result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
		return Action.SUCCESS;
	} catch (Exception ex) {
		log.error(ex.getMessage(), ex);
		if (tx != null) {
			tx.rollback();
		}
		result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
		return Action.ERROR;
	}
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
