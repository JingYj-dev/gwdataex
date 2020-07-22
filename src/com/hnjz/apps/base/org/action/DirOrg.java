
/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirOrg.java 
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>查询组织</p>
 * 
 */
public class DirOrg extends AdminAction {
	private static Log log = LogFactory.getLog(DirOrg.class);
	private String organName; 
	private String code;
	private String name;
	private String parentId ;
	private String includeFlag="1";
	private String openFlag;
	private String postIdsea;
	private Page page;
	private List<String> nodeList;
	List<String> orgIdList = new ArrayList<String>();
	public DirOrg(){
//		page = new Page();
//		page.setCountField("a.uuid");
		page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(15);
	}
	@Override
	protected String adminGo() {
		try{	
			
/*			if(StringHelper.isEmpty(parentId)){
				OrgTree gt = OrgTree.getInstance();
				TreeNode root = gt.getRootNode();
				parentId = root.getNodeId();
			}*/

//			if(StringHelper.isNotEmpty(postIdsea)){
//				orgIdList = SPostItem.queryOrgListByPost(postIdsea);
//			}

			nodeList = new ArrayList<String>();
			OrgTree gt = OrgTree.getInstance();
			String nodeId = parentId;
			if(StringHelper.isEmpty(parentId)){
				nodeId = gt.getRootNode().getNodeId();
			}
			nodeList.add(nodeId);
			if("2".equals(includeFlag)) {
				nodeList.addAll(gt.getListById(OrgTree.getInstance().getTreeNode(nodeId).getAllChildren()));
			}
			QueryCache qc = new QueryCache("select a.uuid from SOrg a " + getWhere() +getOrder());
			setWhere(qc);
			List<SOrg> orgItems = QueryCache.idToObj(SOrg.class, qc.list());
			List<SOrg> newOrgItems = new ArrayList<SOrg>();
			for(SOrg item:orgItems){
				newOrgItems.add(item);
			}
			if(StringHelper.isNotEmpty(postIdsea) && orgItems!=null){
				for(SOrg item:orgItems){
					if(!item.getPostList().getListById().contains(postIdsea)){
						newOrgItems.remove(item);
					}
				}
			}
			page.setResults(newOrgItems);
			loadPage(page);
//			page = qc.page(page);
//			page.setResults(QueryCache.idToObj(SOrg.class, page.getResults()));
//			JSONObject jo = OrgItem.getRootOrgan();
//			JSONArray ja = OrgItem.getSubOrgan(parentId);
//			if(jo != null) 
//				ja.add(jo);
//			result = ja.toString();
			return SUCCESS;
		} catch (Exception ex) {
			setMessage(Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer("where a.delFlag='2' ");
		if(StringHelper.isNotEmpty(name))
			sb.append(" and (a.name like :name or a.code like :name) ");
		if (StringHelper.isNotEmpty(openFlag))
			sb.append(" and a.openFlag =:openFlag ");
		if(nodeList != null && nodeList.size() > 0 ){
			if(StringHelper.isEmpty(parentId) || (StringHelper.isNotEmpty(parentId) && (OrgTree.getInstance().getTreeNode(parentId) == OrgTree.getInstance().getRootNode()) ))
				sb.append(" and (a.parentId in(:nodeList) or a.parentId is null) ");
			else{
				sb.append(" and (a.parentId in(:nodeList) ) ");
			}
		}
//		if(StringHelper.isNotEmpty(postIdsea))
//			sb.append(" and a.uuid in (:orgIdList) ");
 		return sb.toString();
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.name , a.openFlag";
	}
	public void setWhere(QueryCache qc) {
		if(StringHelper.isNotEmpty(name))
			qc.setParameter("name", "%" + name.trim() + "%");
		if (StringHelper.isNotEmpty(openFlag))
			qc.setParameter("openFlag", openFlag);
		if(nodeList != null && nodeList.size() > 0) 
			qc.setParameter("nodeList", nodeList);
//		if(StringHelper.isNotEmpty(postIdsea))
//			qc.setParameter("orgIdList", orgIdList);
		

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
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
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
	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}
	public String getIncludeFlag() {
		return includeFlag;
	}
	public void setIncludeFlag(String includeFlag) {
		this.includeFlag = includeFlag;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPostIdsea() {
		return postIdsea;
	}
	public void setPostIdsea(String postIdsea) {
		this.postIdsea = postIdsea;
	}
	

}
