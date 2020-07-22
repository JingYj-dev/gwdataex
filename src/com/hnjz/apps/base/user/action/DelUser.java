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
import com.hnjz.apps.base.user.model.SUserPost;
import com.hnjz.apps.base.user.model.SUserRole;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelUser extends AdminAction {
	private static Log log = LogFactory.getLog(DelUser.class);
	private String  ids;
	public DelUser() {
	}

	public String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List<String> lst = StringHelper.strToList(ids);
			List<SUser> itemLst = QueryCache.idToObj(SUser.class, lst);
			int num =0;
			tx = new TransactionCache();
			for(SUser item : itemLst ){
					tx.delete(item);
					LogPart lp = new LogPart();		
					lp.setOpObjType(SUser.class.getName());
					lp.setOpObjId(item.getUuid());
					lp.setRelObjType(SUser.class.getName());
					lp.setRelObjId(item.getUuid());
					lp.setOpType(LogConstant.LOG_TYPE_DELETE);
					lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lp.setLogData("");
					lp.setResult(LogConstant.RESULT_SUCCESS);
					lp.save();

			}
			StringBuffer sbrole =  new StringBuffer("delete from SUserRole a where a.userId in (:userId)");
			Map paramsrole=new HashMap();
			paramsrole.put("userId", lst);
			StringBuffer sbpost =  new StringBuffer("delete from SUserPost a where a.userId in (:userId)");
			Map paramspost=new HashMap();
			paramspost.put("userId", lst);
			tx.executeUpdate(sbrole.toString(), paramsrole);
			tx.executeUpdate(sbpost.toString(), paramspost);
			tx.commit();

			for(SUser item : itemLst ){
				List<String> userRoleids = new ArrayList<String>();
				userRoleids.addAll(item.getRoleList().getListById());
				List<String> userPostids = new ArrayList<String>();
				userPostids.addAll(item.getPostList().getListById());
				for(String suerRole:userRoleids){
					LogPart lp = new LogPart();		
					lp.setOpObjType(SUserRole.class.getName());
					lp.setOpObjId(suerRole);
					lp.setRelObjType(SUser.class.getName());
					lp.setRelObjId(item.getUuid());
					lp.setOpType(LogConstant.LOG_TYPE_DELETE);
					lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lp.setLogData("");
					lp.setResult(LogConstant.RESULT_SUCCESS);
					lp.save();
				}
				for(String suserPost:userPostids){
					LogPart lp = new LogPart();		
					lp.setOpObjType(SUserPost.class.getName());
					lp.setOpObjId(suserPost);
					lp.setRelObjType(SUser.class.getName());
					lp.setRelObjId(item.getUuid());
					lp.setOpType(LogConstant.LOG_TYPE_DELETE);
					lp.setLogLevel(LogConstant.LOG_LEVEL_IMPORTANT);
					lp.setLogData("");
					lp.setResult(LogConstant.RESULT_SUCCESS);
					lp.save();
				}
			}
			if(num > 0){
				String str=Integer.toString(num);
				result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.NoDeleteUser",new String[]{str}));	
				return Action.SUCCESS;
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

	public String getIds(){
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}



	

	
	

}
