package com.hnjz.apps.base.post.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.post.model.SPostRole;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class AddPostRole extends AdminAction {
	private static Log log = LogFactory.getLog(AddPostRole.class);
	private String postId;
	private String roleids;
	private String sysids;
	public AddPostRole() {
	}

	public String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(postId)|| StringHelper.isEmpty(roleids)|| StringHelper.isEmpty(sysids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.idsEmpty"));	
				return Action.ERROR;		
			}
			SPost sPost = QueryCache.get(SPost.class, postId);
			List<String> roleIdList = StringHelper.strToList(roleids);
			List<String> sysIdList = StringHelper.strToList(sysids);
			List<SPostRole> posts = new ArrayList<SPostRole>();
			for (int i = 0; i < roleIdList.size()  && i < sysIdList.size(); i++) {
				String roleid = roleIdList.get(i);
				String sysid = sysIdList.get(i);
				if(StringHelper.isNotEmpty(roleid) && StringHelper.isNotEmpty(sysid)){
					SPostRole postrole = new SPostRole();
					postrole.setRoleId(roleid.trim());
					postrole.setSysId(sysid.trim());
					postrole.setPostId(postId);
					postrole.setUuid(UuidUtil.getUuid());
					posts.add(postrole);
				}
			}
			tx = new TransactionCache();
			tx.save(posts);
			tx.commit();
			
			for(SPostRole postrole : posts){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SPostRole.class.getName());
				lp.setOpObjId(postrole.getUuid());
				lp.setRelObjType(SPost.class.getName());
				lp.setRelObjId(postId);
				lp.setOpType(LogConstant.LOG_TYPE_ADD);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(postrole));
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}



	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getRoleids() {
		return roleids;
	}

	public void setRoleids(String roleids) {
		this.roleids = roleids;
	}

	public String getSysids() {
		return sysids;
	}

	public void setSysids(String sysids) {
		this.sysids = sysids;
	}

	

}
