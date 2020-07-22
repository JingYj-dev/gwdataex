package com.hnjz.apps.base.menu.action;

import com.hnjz.apps.base.dict.service.QueryDict;
import com.hnjz.apps.base.menu.model.SMenu;
import com.hnjz.core.model.tree.Tree;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;

import java.util.List;

public class MenuTree extends Tree{

	private static MenuTree instance = null;
	private MenuTree() {
	}
	
	public static synchronized MenuTree getInstance() {
		if (instance == null)
			instance = new MenuTree();
		instance.reloadTreeCache();
		return instance;
	}

	@Override
	protected Object getBindData(TreeNode node) {
		return QueryDict.get(getTreeClass(), node.getNodeId());
	}

	@Override
	protected Class getTreeClass() {
		return SMenu.class;
	}

	@Override
	protected void reloadTree() {
		List nodes = new QueryCache("select a.menuId from SMenu a order by orderNum").list();
		nodes = QueryCache.idToObj(SMenu.class, nodes);
		SMenu root = new SMenu();
		root.setParentId(null);
		root.setMenuId("0");
		root.setOrderNum(0);
		nodes.add(root);
		super.reload(nodes);
	}

	@Override
	protected TreeNode transform(Object info) {
		SMenu sMenu = (SMenu)info;
		TreeNode node = new TreeNode();
		node.setNodeId(sMenu.getMenuId().toLowerCase());
		node.setParentId(sMenu.getParentId() == null ? "" :sMenu.getParentId().toString());
		node.setOrderNo(sMenu.getOrderNum()  == null ? 0 :sMenu.getOrderNum());
		return node;
	}
}
