package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.model.SUserRole;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DelUserRole extends AdminAction{
	private static Log log = LogFactory.getLog(DelUserRole.class);
	private String ids; //角色uuid
	private String userId; //用户uuid
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
		if(StringHelper.isEmpty(ids) || StringHelper.isEmpty(userId)){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
			return Action.ERROR;		
		}
		List<String> listIds = StringHelper.strToList(ids);
//		StringBuffer sb =  new StringBuffer("delete from SUserRole a where a.userId= :userId and a.roleId in (:listIds)");
//		tx = new TransactionCache();
//		Map params=new HashMap();
//		params.put("userId", userId);
//		params.put("listIds", listIds);
//		tx.executeUpdate(sb.toString(), params);
//		tx.commit();
		List<String> sb = new QueryCache("select a.uuid from SUserRole a where a.userId= :userId and a.roleId in (:listIds) ")
		.setParameter("userId", userId).setParameter("listIds", listIds).list();
		tx = new TransactionCache();
		tx.delete(SUserRole.class,sb);
		tx.commit();
		SUser usrObj = QueryCache.get(SUser.class,userId);
		usrObj.getRoleList().removeAll();
		
		for(String userRoleid : sb){
			LogPart lp = new LogPart();		
			lp.setOpObjType(SUserRole.class.getName());
			lp.setOpObjId(userRoleid);
			lp.setRelObjType(SUser.class.getName());
			lp.setRelObjId(userId);
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
