/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DelOrg.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.common.OrgProvider;
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
import com.hnjz.apps.base.sys.action.DelSys;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.hnjz.apps.oa.admin.common.model.FwWordOrgCache;
//import com.hnjz.apps.oa.admin.common.model.SwWordOrgCache;

/**
 * <p>删除组织</p>
 */
public class DelOrg extends AdminAction {
	private static Log log = LogFactory.getLog(DelSys.class);
	private String  ids;
	public DelOrg(){
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
			List<SOrg> orgList = QueryCache.idToObj(SOrg.class, listIds);
			int num = 0;
			tx = new TransactionCache();
			List<String> delOrgIds = new ArrayList<String>();
			for(SOrg org : orgList){	
				List<String> postIdlist =   OrgProvider.queryPostId(org.getUuid()); 
				List<String> userIdlist =   OrgProvider.queryUserId(org.getUuid());
//				List<String> fwTypelist =   FwWordOrgCache.getFwDocTypeList(org.getUuid());
//				List<String> fwWordlist =   FwWordOrgCache.getFwWordList(org.getUuid()).getListById();
//				List<String> swWordlist =   SwWordOrgCache.getSwWordList(org.getUuid()).getListById();
				
				
				List<String> postIds = new ArrayList<String>();
				List<String> userIds = new ArrayList<String>();
				List<String> fwTypeIds = new ArrayList<String>();
				List<String> fwWordIds = new ArrayList<String>();
				List<String> swWordIds = new ArrayList<String>();
				
				
				if((userIdlist!= null && userIdlist.size()>0)
						|| (OrgTree.getInstance().getTreeNode(org.getUuid()).isLeaf() == false)
						/*|| (fwTypelist !=null && fwTypelist.size()>0)
						|| (fwWordlist !=null && fwWordlist.size()>0)
						|| (swWordlist !=null && swWordlist.size()>0)*/){
						num++;
				}else{
					if(postIdlist != null && postIdlist.size()>0){
						for(String postid:postIdlist){
							postIds.add(postid.trim());
						}
						StringBuffer sb =  new StringBuffer("delete from SOrgPost a where a.orgId= :orgId and a.postId in (:listIds)");
						Map params=new HashMap();
						params.put("orgId", org.getUuid());
						params.put("listIds", postIds);
						tx.executeUpdate(sb.toString(), params);
						SOrg orgObj = QueryCache.get(SOrg.class,org.getUuid());
						orgObj.getPostList().removeAll();
					}
					org.setDelFlag("1");
					tx.update(org);
					LogPart lp = new LogPart();		
					lp.setOpObjType(SOrg.class.getName());
					lp.setOpObjId(org.getUuid());
					lp.setRelObjType(SOrg.class.getName());
					lp.setRelObjId(org.getUuid());
					lp.setOpType(LogConstant.LOG_TYPE_DELETE);
					lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lp.setLogData("");
					lp.setResult(LogConstant.RESULT_SUCCESS);
					lp.save();
					delOrgIds.add(org.getUuid());
					OrgTree.getInstance().deleteTreeNode(org.getUuid());
				}
			}			
			tx.commit();
			
			for(String delorg : delOrgIds){
				List<String> postIdlist =   OrgProvider.queryPostId(delorg); 
				for(String postid : postIdlist){
					LogPart lp = new LogPart();		
					lp.setOpObjType(SOrgPost.class.getName());
					lp.setOpObjId(postid);
					lp.setRelObjType(SOrg.class.getName());
					lp.setRelObjId(delorg);
					lp.setOpType(LogConstant.LOG_TYPE_DELETE);
					lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lp.setLogData("");
					lp.save();
				}
			}
			
			if(num>0){
				String str=Integer.toString(num);
				result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeleteOrgCount",new String[]{str}),delOrgIds);	
				return Action.SUCCESS;
			}
			OrgTree.getInstance().reloadTreeCache();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), listIds);
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
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
