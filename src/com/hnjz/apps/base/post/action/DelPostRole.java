package com.hnjz.apps.base.post.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.apps.base.post.model.SPostRole;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelPostRole extends AdminAction{
	private static Log log = LogFactory.getLog(DelPostRole.class);
	private String ids; //角色uuid
	private String postId; //用户uuid
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
		if(StringHelper.isEmpty(ids) || StringHelper.isEmpty(postId)){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
			return Action.ERROR;		
		}
		List<String> listIds = StringHelper.strToList(ids);
		List<String> sb = new QueryCache("select a.uuid from SPostRole a where a.postId= :postId and a.roleId in (:listIds) ")
		.setParameter("postId", postId).setParameter("listIds", listIds).list();
		tx = new TransactionCache();
		tx.delete(SPostRole.class,sb);
		tx.commit();
//		SPostItem.getRoleList(postId);
		
		for(String postRoleid : sb){
			LogPart lp = new LogPart();		
			lp.setOpObjType(SPostRole.class.getName());
			lp.setOpObjId(postRoleid);
			lp.setRelObjType(SPost.class.getName());
			lp.setRelObjId(postId);
			lp.setOpType(LogConstant.LOG_TYPE_DELETE);
			lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
			lp.setLogData("");
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
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
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}

	
	
}
