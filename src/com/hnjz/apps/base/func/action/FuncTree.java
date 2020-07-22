package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.core.model.tree.Tree;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;

import java.util.List;

public class FuncTree extends Tree {
	
	private static FuncTree instance = null;
	private FuncTree() {
	}
	protected Class getTreeClass() {
		return SFunc.class;
	}
	public static synchronized FuncTree getInstance() {
		if (instance == null)
			instance = new FuncTree();
		instance.reloadTreeCache();
		return instance;
	}
	public TreeNode transform(Object info) {
		SFunc sFunc = (SFunc) info;
		TreeNode node = new TreeNode();
		node.setNodeId(sFunc.getUuid().toLowerCase());
		node.setParentId(sFunc.getParentId() == null ? "" : sFunc.getParentId().toString());
		node.setOrderNo(sFunc.getOrderNum() == null ? 0 : sFunc.getOrderNum());
		return node;
	}
	public void reloadTree() {
		List nodes = new QueryCache("select a.uuid from SFunc a order by orderNum").list();
		nodes = QueryCache.idToObj(SFunc.class, nodes);
//		SFunc root = new SFunc();
//		root.setParentId(null);
//		root.setUuid("0");
//		root.setOrderNum(0);
//		nodes.add(root);
		super.reload(nodes);
	}
	public Object getBindData(TreeNode node) {
		return QueryCache.get(getTreeClass(), node.getNodeId());
	}
}
