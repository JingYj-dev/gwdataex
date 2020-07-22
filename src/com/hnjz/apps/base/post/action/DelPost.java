
/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DelPost.java 
 */
package com.hnjz.apps.base.post.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.org.action.OrgItem;
import com.hnjz.apps.base.org.model.SOrgPost;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.user.action.UserItem;
import com.hnjz.apps.base.user.model.SUserPost;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/** 
 * <p>删除岗位</p>
 * 
 */
public class DelPost extends AdminAction {
	private static Log log = LogFactory.getLog(DelPost.class);
	private String ids;
	public DelPost(){
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List listIds = StringHelper.strToList(ids);
			List<SPost> postList = QueryCache.idToObj(SPost.class, listIds);
			List<SPost> delList = new ArrayList<SPost>();
/*			if(postList.size()==1){
				SPost spost = postList.get(0);
				
				List<SOrgPost> orgPostlist = OrgItem.getPostByPostIdList(spost.getUuid());
				List<SUserPost> userPostList = UserItem.getSUserPostByPostId(spost.getUuid());
				if(userPostList!= null && userPostList.size()>0){//若存在用户分配该岗位，岗位不能删除
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.NoDeletePost"));
					return Action.ERROR;
				}
				if(orgPostlist!= null && orgPostlist.size()>0){//如果存在组织机构分配该岗位，则删除关联和岗位
					tx = new TransactionCache();
					tx.delete(orgPostlist);
					tx.commit();
				}
				tx = new TransactionCache();
				tx.delete(spost);
				tx.commit();
			}else{//批量删除
*/				int num = 0;
				tx = new TransactionCache();
				for(SPost post : postList){					
					List<SOrgPost> orgPostlist = OrgItem.getPostByPostIdList(post.getUuid().trim());
					List<SUserPost> userPostList = UserItem.getSUserPostByPostId(post.getUuid().trim());
					if(userPostList!=null && userPostList.size()>0){   //若存在用户分配该岗位，岗位不能删除
							num++;
					}else if(orgPostlist!= null && orgPostlist.size()>0){//如果存在组织机构分配该岗位，则删除关联和岗位
						tx.delete(orgPostlist);
						tx.delete(post);
						delList.add(post);
					}else{
						tx.delete(post);
						delList.add(post);
					}
				}			
				tx.commit();
				
				for(SPost post : delList ){
					LogPart lppost = new LogPart();		
					lppost.setOpObjType(SPost.class.getName());
					lppost.setOpObjId(post.getUuid());
					lppost.setRelObjType(SPost.class.getName());
					lppost.setRelObjId(post.getUuid());
					lppost.setOpType(LogConstant.LOG_TYPE_DELETE);
					lppost.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lppost.setLogData("");
					lppost.setResult(LogConstant.RESULT_SUCCESS);
					lppost.save();
					List<SOrgPost> orgPostlist = OrgItem.getPostByPostIdList(post.getUuid().trim());
					for(SOrgPost suserPost:orgPostlist){
						LogPart lp = new LogPart();		
						lp.setOpObjType(SOrgPost.class.getName());
						lp.setOpObjId(suserPost.getUuid());
						lp.setRelObjType(SPost.class.getName());
						lp.setRelObjId(post.getUuid());
						lp.setOpType(LogConstant.LOG_TYPE_DELETE);
						lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
						lp.setLogData("");
						lp.setResult(LogConstant.RESULT_SUCCESS);
						lp.save();
					}
				}
				
				if(num==postList.size()){
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.NoDeletePost"));	
					return Action.ERROR;
				}else if(num>=1&&num<postList.size()){
					String str=Integer.toString(num);
					result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeletePostCount",new String[]{str}));
					return Action.SUCCESS;
				}
/*			}*/
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

}
