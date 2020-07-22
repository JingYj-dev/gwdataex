package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.org.model.SOrgPost;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.user.action.AddUserPost;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class AddOrgPost extends AdminAction{
	private static Log log = LogFactory.getLog(AddUserPost.class);
	private String orgId;
	private String ids;
	public AddOrgPost(){
	}
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(ids)|| StringHelper.isEmpty(orgId)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.idsEmpty"));	
				return Action.ERROR;		
			}
			SOrg orgObj = QueryCache.get(SOrg.class,orgId);
			List<String> rlist = StringHelper.strToList(ids);
			List<SOrgPost> items= new ArrayList<SOrgPost>();
			for (int i = 0; i < rlist.size() ; i++) {
				String rid=rlist.get(i);
				SOrgPost item = new SOrgPost();
				item.setOrgId(orgId);
				item.setPostId(rid.trim());
				items.add(item);
				orgObj.getPostList().add(rid);	
			}
			tx = new TransactionCache();
			tx.save(items);
			tx.commit();
			
			for(SOrgPost orgpost : items){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SOrgPost.class.getName());
				lp.setOpObjId(orgpost.getUuid());
				lp.setRelObjType(SOrg.class.getName());
				lp.setRelObjId(orgId);
				lp.setOpType(LogConstant.LOG_TYPE_ADD);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(orgpost));
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
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

}
