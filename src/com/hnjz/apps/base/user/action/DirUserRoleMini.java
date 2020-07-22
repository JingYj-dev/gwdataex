package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.apps.base.role.model.SRole;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DirUserRoleMini extends AdminAction {
	private static Log log = LogFactory.getLog(DirUserRoleMini.class);
	private Page page;
	private String sysIdsearch;
	private SUser item;
	private String userId;//用户uuid
	public DirUserRoleMini() {
		page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(5);
	}
	@Override
	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(userId)) {
				item = QueryCache.get(SUser.class, userId);
			}
			List<String> userroleIdList = UserProvider.getRoleIdList(userId);
			List<String> idList = new ArrayList<String>();
			StringBuffer sb = new StringBuffer(" select a.uuid from SRole a where a.openFlag='1' ");
			//业务类型用户显示业务类型角色，管理类推
			if(StringHelper.isEmpty(item.getUserType()) || BaseEnvironment.USERTYPE_NORMAL.equals(item.getUserType())){
				sb.append(" and a.roleType = '" +BaseEnvironment.ROLETYPE_NORMAL+ "' ");
			}else{
				sb.append(" and a.roleType = '" +BaseEnvironment.ROLETYPE_ADMIN+ "' ");
			}
			if(StringHelper.isNotEmpty(sysIdsearch)){
				sb.append(" and a.sysId=:sysId ");
			}
			QueryCache qc  = new QueryCache(sb.toString());
			if(StringHelper.isNotEmpty(sysIdsearch)){
				qc.setParameter("sysId", sysIdsearch).list();
			}
				
			StringBuffer ss = new StringBuffer(" select count(a.uuid) from SRole a where a.openFlag='1' ");
			//业务类型用户显示业务类型角色，管理类推
			if(StringHelper.isEmpty(item.getUserType()) || BaseEnvironment.USERTYPE_NORMAL.equals(item.getUserType())){
				ss.append(" and a.roleType = '" +BaseEnvironment.ROLETYPE_NORMAL+ "' ");
			}else{
				ss.append(" and a.roleType = '" +BaseEnvironment.ROLETYPE_ADMIN+ "' ");
			}
			if(StringHelper.isNotEmpty(sysIdsearch)){
				ss.append(" and a.sysId=:sysId ");
			}
			QueryCache qs  = new QueryCache(sb.toString());
			if(StringHelper.isNotEmpty(sysIdsearch)){
				qs.setParameter("sysId", sysIdsearch);
			}
						
			idList = qc.list();
			
			for(String roleid:userroleIdList){
				idList.remove(roleid.trim());
			}
			if(StringHelper.isEmpty(sysIdsearch) && idList.size()==0){
				setMessage(Messages.getString("systemMsg.NoAddUserRole"));
				return Action.ERROR;
			}
			List<SRole> rolelist = QueryCache.idToObj(SRole.class, idList);
			page.setResults(rolelist);
			//设置分页
			loadPage(page);
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	private void loadPage(Page page){
		List results = page.getResults();
		if(results != null && results.size() > 0){
			page.setTotalRows(results.size());
			page.setTotalPages((int)Math.ceil(results.size()*1.0/page.getPageSize()));
			int total = results.size();
			int curPage = page.getCurrentPage();
			int pageSize = page.getPageSize();
			if(curPage > page.getTotalPages()){
				curPage = page.getTotalPages();
			}
			int startIndex = (curPage>0?curPage-1:0) * pageSize;
			if(startIndex > total){
				startIndex =0;
			}
			int endIndex = startIndex + pageSize;
			if(endIndex > total){endIndex=total;}
			results = results.subList(startIndex, endIndex );
			page.setResults(results);
		}
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

	public String getSysIdsearch() {
		return sysIdsearch;
	}
	public void setSysIdsearch(String sysIdsearch) {
		this.sysIdsearch = sysIdsearch;
	}
	public SUser getItem() {
		return item;
	}
	public void setItem(SUser item) {
		this.item = item;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
