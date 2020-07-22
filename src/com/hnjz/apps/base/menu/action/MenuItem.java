package com.hnjz.apps.base.menu.action;

import com.hnjz.apps.base.menu.model.SMenu;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class MenuItem {

	private static Log log = LogFactory.getLog(MenuItem.class);
	
	public static JSONObject getRootMenu() throws JSONException{
		MenuTree gt = MenuTree.getInstance();
		TreeNode root = gt.getRootNode();
		SMenu item = QueryCache.get(SMenu.class, root.getNodeId());
		if(item == null)
			return null;
		JSONObject one = new JSONObject();
		one.put("id", item.getMenuId());
		one.put("name", item.getMenuName());
		one.put("pId", item.getParentId());
		one.put("isParent", true);
		one.put("open", true);
		return one;
	}

	public static JSONArray getSubMenu1(String parentId) throws JSONException{
		MenuTree gt = MenuTree.getInstance();
		TreeNode tn = gt.getTreeNode(parentId);
		List<TreeNode> lst = tn.getChildren();
		JSONArray menuTree = new JSONArray();
		JSONObject one;
		for (TreeNode node : lst){
			SMenu item = QueryCache.get(SMenu.class, node.getNodeId());
			if(item != null){
				one = new JSONObject();
				one.put("id", node.getNodeId());
				one.put("name", item.getMenuName());
				one.put("pId", item.getParentId());
				one.put("isParent", !node.isLeaf());
				menuTree.add(one);
			}
		}
		return menuTree;
	}
	
	public static JSONArray getSubMenu(String parentId) throws JSONException{
		JSONArray jsonArray = new JSONArray();
		JSONObject one;
		if(StringHelper.isEmpty(parentId)){
			one = new JSONObject();
			one.put("id", "0");
			one.put("name", "系统菜单");
			one.put("pId", "0");
			one.put("sysId","1001");
			one.put("isParent", true);
			one.put("chkDisabled",true);
			one.put("open", true);
			jsonArray.add(one);
			/*List<SSys> sysList = SysItem.getSystems();
			if(sysList != null) 
				for(SSys item : sysList) {
					one = new JSONObject();
					one.put("id", item.getUuid());
					one.put("name", item.getName());
					one.put("pId", "0");
					one.put("sysId",item.getUuid());
					one.put("isParent", true);
					one.put("chkDisabled",true);
					one.put("open", true);
					jsonArray.add(one);
				}*/
		}else{
			List<SMenu> lst = getMenus(parentId);
			List ids;
			if(lst != null) 
				for(SMenu item : lst) {
					one = new JSONObject();
					one.put("id", item.getMenuId());
					one.put("name", item.getMenuName());
					one.put("sysId", item.getSysId());
					one.put("pId", item.getParentId());
					//one.put("url", item.getUrl());
					one.put("funcId",item.getFuncId());
					ids = getMenuIds(item.getMenuId());
					if(ids == null || ids.size() < 1)
						one.put("isParent", false);
					else
						one.put("isParent", true);
					jsonArray.add(one);
				}
		}
		
		return jsonArray;
	}
	
	public static List getMenus(String parentId){
		QueryCache qc = getMenuIdsQC(parentId);
		List list = qc.list();
		return QueryCache.idToObj(SMenu.class, list);
	}
	private static QueryCache getMenuIdsQC(String parentId) {
		return new QueryCache("select a.menuId from SMenu a where a.parentId=:parentId order by a.orderNum")
		.setParameter("parentId", parentId);
	}
	public static List getMenuIds(String parentId){
		QueryCache qc = getMenuIdsQC(parentId);
		return qc.listCache();
	}
}
