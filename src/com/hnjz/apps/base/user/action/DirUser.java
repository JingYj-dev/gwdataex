package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.common.UserProvider;
import com.hnjz.apps.base.org.action.OrgTree;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DirUser extends AdminAction{
	private static Log log = LogFactory.getLog(DirUser.class);
	private String username;
	private String openFlag;
	private String orgId;
	private String includeFlag="1";
	private String tree;
	private String activeStatus;
	private String postIdsearch;
	private List<String> userIdList = new ArrayList<String>();
	private Page page;
	public DirUser() {
		page = new Page();
		page.setCountField("a.uuid");
	}
	@Override
	protected String adminGo() {
		try {
			if(StringHelper.isNotEmpty(postIdsearch)){
				List<SUser> UserList = UserProvider.queryUserListByPost(postIdsearch);
				for(SUser user:UserList){
					userIdList.add(user.getUuid().trim());
				}
			}
			QueryCache qc = new QueryCache("select a.uuid from SUser a " + getWhere() + getOrder());			
			setWhere(qc);  
			page = qc.page(page); 
			page.setResults(QueryCache.idToObj(SUser.class, page.getResults()));
			return SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.activeStatus,a.issueDate desc ";
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer("where a.orgId in(:orgId) and a.delFlag='" + Environment.UN_DELETE_STATE + "'");
		//run模式下只能显示普通用户
		if(!ConfigurationManager.isAdminMode()){
			sb.append(" and a.userType = '" +BaseEnvironment.USERTYPE_NORMAL+ "' ");
		}
		if(StringHelper.isNotEmpty(openFlag))
			sb.append(" and a.openFlag = :openFlag ");
		if(StringHelper.isNotEmpty(activeStatus))
			sb.append(" and a.activeStatus = :activeStatus ");
		if(StringHelper.isNotEmpty(postIdsearch))
			sb.append(" and a.uuid in (:userIdList) ");
		if(StringHelper.isNotEmpty(username) )
			sb.append(" and (a.loginName like :name or a.realName like :name) ");
		return sb.toString();
	}
	
	public void setWhere(QueryCache qc) {
		if(StringHelper.isNotEmpty(orgId)) {
			if("2".equals(includeFlag)) {
				OrgTree gt = OrgTree.getInstance();
				List orgIds = gt.getListById(OrgTree.getInstance().getTreeNode(orgId).getAllChildren());
				orgIds.add(gt.getInstance().getRootNode().getNodeId());
				orgIds.add(orgId);
				qc.setParameter("orgId", orgIds);
			} else {
				qc.setParameter("orgId", orgId);
			}
		}else{
			if("2".equals(includeFlag)) {
				OrgTree gt = OrgTree.getInstance();
				List orgIds = gt.getListById(OrgTree.getInstance().getRootNode().getAllChildren());
				qc.setParameter("orgId", orgIds);
			} else {
				List orgIds = new ArrayList<String>();
				orgIds.add(OrgTree.getInstance().getRootNode().getNodeId());
				qc.setParameter("orgId", orgIds);
			}
		}
		if(StringHelper.isNotEmpty(username)) 
			qc.setParameter("name", "%" + username.trim() + "%");
		if(StringHelper.isNotEmpty(openFlag))
			qc.setParameter("openFlag", openFlag.trim());
		if(StringHelper.isNotEmpty(activeStatus))
			qc.setParameter("activeStatus", activeStatus.trim());
		if(StringHelper.isNotEmpty(postIdsearch))
			qc.setParameter("userIdList", userIdList);
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getOpenFlag() {
		return openFlag;
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getIncludeFlag() {
		return includeFlag;
	}

	public void setIncludeFlag(String includeFlag) {
		this.includeFlag = includeFlag;
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getPostIdsearch() {
		return postIdsearch;
	}
	public void setPostIdsearch(String postIdsearch) {
		this.postIdsearch = postIdsearch;
	}
	public List<String> getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}
}
