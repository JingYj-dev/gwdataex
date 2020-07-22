package com.hnjz.apps.base.user.action.admin;

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

public class DirUserAdmin extends AdminAction{
	private static Log log = LogFactory.getLog(DirUserAdmin.class);
	private String name, openFlag, orgId, includeFlag="1",tree,activeStatus;
	private String userType;
	private Page page;
	public DirUserAdmin() {
		page = new Page();
		page.setCountField("a.uuid");
	}
	@Override
	protected String adminGo() {
		try {
			QueryCache qc = new QueryCache("select a.uuid from SUser a " + getWhere() + getOrder());			
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(SUser.class, page.getResults()));
//			JSONObject jo = OrgItem.getRootOrgan();
//			JSONArray ja = OrgItem.getSubOrgan(OrgTree.getInstance().getRootNode().getNodeId());
//			if(jo != null) {
//				jo.put("checked", true);
//				ja.add(jo);
//			}
//			tree = ja.toString();
			return SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.issueDate desc";
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer("where a.orgId in(:orgId) and a.delFlag='" + Environment.UN_DELETE_STATE + "'");
//		StringBuffer sb = new StringBuffer("where a.delFlag='" + Environment.UN_DELETE_STATE + "'");
		//run模式下只能显示普通用户
		if(!ConfigurationManager.isAdminMode()){
			sb.append(" and a.userType = '1' ");
		}else if(StringHelper.isNotEmpty(userType)){
			sb.append(" and a.userType =  '" + userType +"' ");
		}
//		if(StringHelper.isNotEmpty(orgId)) 
//			sb.append(" and a.orgId in(:orgId) ");
		if(StringHelper.isNotEmpty(name) )
			sb.append(" and (a.loginName like :name or a.realName like :name) ");
//		if(StringHelper.isNotEmpty(loginName))
//			sb.append(" and a.loginName like :loginName ");
//		if(StringHelper.isNotEmpty(realName))
//			sb.append(" and a.realName like :realName ");
		if(StringHelper.isNotEmpty(openFlag))
			sb.append(" and a.openFlag = :openFlag ");
		if(StringHelper.isNotEmpty(activeStatus))
			sb.append(" and a.activeStatus = :activeStatus ");
		return sb.toString();
	}
	
	public void setWhere(QueryCache qc) {
		if(StringHelper.isNotEmpty(orgId)) {
			if("2".equals(includeFlag)) {
				OrgTree gt = OrgTree.getInstance();
				List orgIds = gt.getListById(OrgTree.getInstance().getTreeNode(orgId).getAllChildren());
				orgIds.add(orgId);
				qc.setParameter("orgId", orgIds);
			} else {
				qc.setParameter("orgId", orgId);
			}
		}else{
			if("2".equals(includeFlag)) {
				OrgTree gt = OrgTree.getInstance();
				List orgIds = gt.getListById(OrgTree.getInstance().getRootNode().getAllChildren());
				orgIds.add(gt.getInstance().getRootNode().getNodeId());
				qc.setParameter("orgId", orgIds);
			} else {
				List orgIds = new ArrayList<String>();
				orgIds.add(OrgTree.getInstance().getRootNode().getNodeId());
				qc.setParameter("orgId", orgIds);
			}
		}
		if(StringHelper.isNotEmpty(name)) 
			qc.setParameter("name", "%" + name.trim() + "%");
/*		if(StringHelper.isNotEmpty(realName)) 
			qc.setParameter("realName", "%" + realName.trim() + "%");*/
		if(StringHelper.isNotEmpty(openFlag))
			qc.setParameter("openFlag", openFlag.trim());
		if(StringHelper.isNotEmpty(activeStatus))
			qc.setParameter("activeStatus", activeStatus.trim());
			
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

/*	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}*/

	public String getOpenFlag() {
		return openFlag;
	}


	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

	
}
