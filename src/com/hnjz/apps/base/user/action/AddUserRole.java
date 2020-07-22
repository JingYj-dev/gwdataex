package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.base.user.model.SUserRole;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class AddUserRole extends AdminAction {
	private static Log log = LogFactory.getLog(AddUserRole.class);
	private String userId;
	private String roleids;
	private String sysids;
	public AddUserRole() {
	}

	public String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(userId)|| StringHelper.isEmpty(roleids)|| StringHelper.isEmpty(sysids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.idsEmpty"));	
				return Action.ERROR;		
			}
			SUser sUser = QueryCache.get(SUser.class, userId);
			List<String> roleIdList = StringHelper.strToList(roleids);
			List<String> sysIdList = StringHelper.strToList(sysids);
			List<SUserRole> roles = new ArrayList<SUserRole>();
			for (int i = 0; i < roleIdList.size()  && i < sysIdList.size(); i++) {
				String roleid = roleIdList.get(i);
				String sysid = sysIdList.get(i);
				if(StringHelper.isNotEmpty(roleid) && StringHelper.isNotEmpty(sysid)){
					SUserRole userrole = new SUserRole();
					userrole.setRoleId(roleid.trim());
					userrole.setSysId(sysid.trim());
					userrole.setUserId(userId);
					userrole.setUuid(UuidUtil.getUuid());
					roles.add(userrole);
					sUser.getRoleList().add(roleid.trim());
				}
			}
			tx = new TransactionCache();
			tx.save(roles);
			tx.commit();
			
			for(SUserRole userrole : roles){
				LogPart lp = new LogPart();		
				lp.setOpObjType(SUserRole.class.getName());
				lp.setOpObjId(userrole.getUuid());
				lp.setRelObjType(SUser.class.getName());
				lp.setRelObjId(userId);
				lp.setOpType(LogConstant.LOG_TYPE_ADD);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(userrole));
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
