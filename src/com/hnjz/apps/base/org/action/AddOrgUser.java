package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.org.model.SUserOrg;
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

public class AddOrgUser extends AdminAction {
	private static Log log = LogFactory.getLog(AddOrgUser.class);
	private String parentId;
	private String ids;
 
	public AddOrgUser() {
	}

	public String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			
			List<String> list = StringHelper.strToList(ids);
			if(list != null && list.size()>0) {
				tx = new TransactionCache();
				List lst = new QueryCache("select a.uuid from SUserOrg a where a.userId in (:userId)")
				.setParameter("userId", list).list();
				List<SUserOrg> uolist = QueryCache.idToObj(SUserOrg.class, lst);
				tx.delete(uolist);
				tx.commit();
				
				tx = new TransactionCache();
				for(String uid : list){
					SUserOrg item = new SUserOrg();
					item.setOrgId(parentId);
					item.setUserId(uid);
					tx.save(item);
				}
				tx.commit();
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
