/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirFuncAction.java creation date: [Jan 9, 2014 9:55:46 AM] by Xingzhc
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * <p>查询功能列表</p>
 * 
 */
public class DirFunc extends AdminAction {
	private static Log log = LogFactory.getLog(DirFunc.class);
	private String sysId;
	private String funcName;
	private String funcId;
	private String parentId;
	private Page page;
	private boolean flag;
	private String includeFlag="1";
	private String openFlag;
	private String funcType;
	private String name;

	public DirFunc(){
		page = new Page();
		page.setCountField("a.uuid");
	}
	@Override
	protected String adminGo(){
		try{
			if(!flag) {
			QueryCache qc = new QueryCache("select a.uuid from SFunc a " + getWhere() +getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(SFunc.class, page.getResults()));
			}
			return SUCCESS;
		} catch (Exception ex){
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	public String getWhere() {
		
		StringBuffer sb = new StringBuffer("where 1=1 ");
		//run模式下只能显示业务类型功能
//		if(!ConfigurationManager.isAdminMode()){
//			sb.append(" and a.funcType = '" +BaseEnvironment.FUNCTYPE_NORMAL+ "' ");
//		}
		if(ConfigurationManager.isAdminMode() && StringHelper.isNotEmpty(funcType)){
			sb.append(" and a.funcType = '" +funcType+ "' ");
		}
/*		if (StringHelper.isNotEmpty(funcId))
			sb.append(" and a.funcId like :funcId ");
		
		if (StringHelper.isNotEmpty(funcName))
			sb.append(" and a.name like :funcName ");*/
		
		if(StringHelper.isNotEmpty(name))
			sb.append(" and a.name like :name or a.funcId like :name ");
		
		if(StringHelper.isNotEmpty(sysId))
			sb.append(" and a.sysId =:sysId ");
		
		if (StringHelper.isNotEmpty(openFlag))
			sb.append(" and a.openFlag =:openFlag ");
    
		if(StringHelper.isNotEmpty(parentId)){
			if("2".equals(includeFlag)) {
			   if(!parentId.trim().equals(sysId))
				   sb.append(" and a.parentId in(:parentId) ");
			}else
				 sb.append(" and a.parentId in(:parentId) ");
			 
		}

		return sb.toString();
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "order by a.openFlag,a.funcId";
	}
	
	public void setWhere(QueryCache qc) {
		
/*		if (StringHelper.isNotEmpty(funcId))
			qc.setParameter("funcId", "%" + funcId.trim() + "%");
		
		if (StringHelper.isNotEmpty(funcName))
			qc.setParameter("funcName", "%" + funcName.trim() + "%");*/
		
		if(StringHelper.isNotEmpty(name))
			qc.setParameter("name", "%" + name.trim() + "%");
		
		if(StringHelper.isNotEmpty(sysId))
			qc.setParameter("sysId", sysId);
		
		if (StringHelper.isNotEmpty(openFlag))
			qc.setParameter("openFlag", openFlag);
		
		if(StringHelper.isNotEmpty(parentId)){
			if("2".equals(includeFlag)) {
				FuncTree ft = FuncTree.getInstance();
				TreeNode tn = ft.getTreeNode(parentId);
				if(tn!=null){
					List parentIds = ft.getListById(FuncTree.getInstance().getTreeNode(parentId).getAllChildren());
					parentIds.add(parentId);
					qc.setParameter("parentId", parentIds);
				}
			} else {
				qc.setParameter("parentId", parentId);
			}
		}
		
	}
	public String getSysId(){
		return sysId;
	}
	public void setSysId(String sysId){
		this.sysId = sysId;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page){
		this.page = page;
	}
	public String getFuncName(){
		return funcName;
	}
	public void setFuncName(String funcName){
		this.funcName = funcName;
	}
	public String getParentId(){
		return parentId;
	}
	public void setParentId(String parentId){
		this.parentId = parentId;
	}
	public String getFuncId(){
		return funcId;
	}
	public void setFuncId(String funcId){
		this.funcId = funcId;
	}
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getIncludeFlag() {
		return includeFlag;
	}
	public void setIncludeFlag(String includeFlag) {
		this.includeFlag = includeFlag;
	}
	public String getOpenFlag() {
		return openFlag;
	}
	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}
	public String getFuncType() {
		return funcType;
	}
	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
