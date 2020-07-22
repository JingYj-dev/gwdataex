package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.apps.base.role.action.RoleItem;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public  class DirUserRole extends AdminAction{
	private static Log log = LogFactory.getLog(DirUserRole.class);
	private Page page;
	private String userId;
	private List RoleList;
	private boolean Flag = true;
	public DirUserRole() {
		page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(11);
		page.setCountField("a.uuid");
	}

	@Override
	protected String adminGo() {
		try {
			SUser sUser = QueryCache.get(SUser.class, userId);
			if(sUser == null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.failed"));	
				return Action.ERROR;	
			}
			List<String> userRoleList = UserProvider.getRoleIdList(userId);
		    List<String> roleIdList = new ArrayList<String>();
            for(String roleid:userRoleList){
            	roleIdList.add(roleid.trim());
            }
            if(roleIdList.size() == RoleItem.getSRoleCount(sUser.getUserType())){
            	Flag = false;
            }
            RoleList= QueryCache.idToObj(SRole.class,roleIdList);
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List getRoleList() {
		return RoleList;
	}

	public void setRoleList(List roleList) {
		RoleList = roleList;
	}

	public boolean isFlag() {
		return Flag;
	}

	public void setFlag(boolean flag) {
		Flag = flag;
	}


}
