package com.hnjz.apps.base.org.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DirOrgUser extends AdminAction{
	private static Log log = LogFactory.getLog(DirOrgUser.class);
	private String parentId, realName, loginName;
	private Page page;
	
	public DirOrgUser() {
		page = new Page();
		page.setCountField("a.uuid");
	}

	@Override
	protected String adminGo() {
		try {
			QueryCache qc = new QueryCache("select a.uuid, b.orgId from s_user a left join s_user_org b on a.uuid=b.userId " + getWhere() + getOrder(), true);
			setWhere(qc);
			page = qc.page(page);
			List<SUser> ulist = new ArrayList();
			for(int i = 0; i < page.getResults().size(); i++){
				Object[] arr = (Object[]) page.getResults().get(i);
				SUser tmp = QueryCache.get(SUser.class, arr[0].toString());
				tmp.setOrgId(arr[1]==null?"":arr[1].toString().toString());
				ulist.add(tmp);
			}
			page.setResults(ulist);
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "";
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer("where a.delFlag='" + Environment.UN_DELETE_STATE + "'");
		if(StringHelper.isNotEmpty(loginName))
			sb.append(" and a.loginName like :loginName ");
		if(StringHelper.isNotEmpty(realName))
			sb.append(" and a.realName like :realName ");
		return sb.toString();
	}
	
	public void setWhere(QueryCache qc) {
		if(StringHelper.isNotEmpty(loginName)) 
			qc.setParameter("loginName", "%" + loginName.trim() + "%");
		if(StringHelper.isNotEmpty(realName)) 
			qc.setParameter("realName", "%" + realName.trim() + "%");
			
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}
