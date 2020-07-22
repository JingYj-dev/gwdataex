package com.hnjz.apps.base.menu.action;

import com.hnjz.apps.base.menu.model.SMenu;
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

public class DirMenu extends AdminAction{

	private static Log log = LogFactory.getLog(DirMenu.class);
	private Page page;
	private String parentId;
	private String menuNameOrFuncId;
	private String openFlag;
	private String includeFlag="1";
	private String sysId;
	public DirMenu() {
		page = new Page();
		page.setCountField("a.menuId");
	}

	@Override
	public String adminGo() {
		try{
			if(StringHelper.isNotEmpty(parentId)){
				QueryCache qc = new QueryCache(" select a.menuId  from SMenu a  " + getWhere() +getOrder());
				setWhere(qc);
				page = qc.page(page);
				page.setResults(QueryCache.idToObj(SMenu.class, page.getResults()));
			}
			return SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}
	
	private String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
			 	
			 	if(StringHelper.isNotEmpty(openFlag)){
			 		sb.append(" and a.openFlag = :openFlag "); 		
			 	}
			 	if(StringHelper.isNotEmpty(parentId)){
			 		if("2".equals(includeFlag)) {
						   if(!parentId.trim().equals(sysId))
							   sb.append(" and a.parentId in(:parentId) ");
						}else
							 sb.append(" and a.parentId in(:parentId) ");	
			 	}
			 	if(StringHelper.isNotEmpty(menuNameOrFuncId)){
			 		sb.append(" and (a.funcId like :menuNameOrFuncId or a.menuName like :menuNameOrFuncId)"); 		
			 	}
		return sb.toString();
	}
	
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : "";
	}
	
	private void setWhere(QueryCache qc) {
			 
			 	if(StringHelper.isNotEmpty(openFlag)){	
			 		qc.setParameter("openFlag", openFlag);	
			 	}
			 	if(StringHelper.isNotEmpty(parentId)){	
			 		if("2".equals(includeFlag)) {
			 			MenuTree ft = MenuTree.getInstance();
						TreeNode tn = ft.getTreeNode(parentId);
						if(tn!=null){
							List parentIds = ft.getListById(MenuTree.getInstance().getTreeNode(parentId).getAllChildren());
							parentIds.add(parentId);
							qc.setParameter("parentId", parentIds);
						}
					} else {
						qc.setParameter("parentId", parentId);
					}
			 		/*if("2".equals(includeFlag)) {
						FuncTree ft = FuncTree.getInstance();
						TreeNode tn = ft.getTreeNode(parentId);
						if(tn!=null){
							List<String> parentIds = new QueryCache("select a.menuId  from SMenu a where a.parentId =:parentId").setParameter("parentId", parentId).listCache();
							ft.getRootNode().getAllChildren();
							List parentIds1 = ft.getListById(FuncTree.getInstance().getRootNode().getAllChildren());
							//List parentIds = ft.getListById(FuncTree.getInstance().getTreeNode(parentId).getAllChildren());
							parentIds.add(parentId);
							qc.setParameter("parentId", parentIds);
						}
					} else {
						qc.setParameter("parentId", parentId);
					}*/	
			 	}
				if(StringHelper.isNotEmpty(menuNameOrFuncId)){	
			 		qc.setParameter("menuNameOrFuncId", "%" + menuNameOrFuncId.trim() + "%");	
			 	}
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
	public String getMenuNameOrFuncId() {
		return menuNameOrFuncId;
	}

	public void setMenuNameOrFuncId(String menuNameOrFuncId) {
		this.menuNameOrFuncId = menuNameOrFuncId;
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

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
}
