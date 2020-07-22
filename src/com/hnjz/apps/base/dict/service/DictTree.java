package com.hnjz.apps.base.dict.service;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.core.model.tree.Tree;
import com.hnjz.core.model.tree.TreeNode;

import java.util.List;

public class DictTree extends Tree {
	private static DictTree instance = null;
	private DictTree() {
	}
	protected Class<?> getTreeClass() {
		return SDict.class;
	}
	public static synchronized DictTree getInstance() {
		if (instance == null)
			instance = new DictTree();
		instance.reloadTreeCache();
		return instance;
	}
	public TreeNode transform(Object info) {
		SDict dict = (SDict) info;
		TreeNode node = new TreeNode();
		node.setNodeId(dict.getUuid().toString());
		node.setParentId(dict.getParentId() == null ? "" : dict.getParentId().toString());
		node.setOrderNo(dict.getOrderNum() == null ? 0 : dict.getOrderNum());
		return node;
	}
	public void reloadTree() {
		List nodes = new QueryDict("select a.uuid from SDict a ").list();
		nodes = QueryDict.idToObj(SDict.class, nodes);
//		SDict root = new SDict();
//		root.setParentId(null);
//		root.setUuid("0");
//		root.setOrderNum(0);
//		nodes.add(root);
		super.reload(nodes);
	}
	public Object getBindData(TreeNode node) {
		return QueryDict.get(getTreeClass(), node.getNodeId());
	}
	
	public static void main(String [] args) {
		DictTree gt = DictTree.getInstance();
		System.out.println("-------------------根节点下的所有节点：");
		List<TreeNode> lst1 = gt.getRootNode().getAllChildren();
		for (TreeNode item : lst1)
			System.out.println(item.getNodeId());
		System.out.println("-------------------根节点下一层的节点：");
		List<TreeNode> lst2 = gt.getRootNode().getChildren();
		for (TreeNode item : lst2)
			System.out.println(item.getNodeId());
		System.out.println("-------------------id=1技术部下层节点：");
		TreeNode tn = gt.getTreeNode("30dffbfb7e2088714f3fff0a775562db");// 获取技术部所在组id="1"的树节点TreeNode对象
		List<TreeNode> lst3 = tn.getChildren();
		List<SDict> groups = gt.getList(lst3);
		for (SDict group : groups)
			System.out.println(group.getName());
//		System.out.println("-------------------id=1技术部下层节点id：");
//		List<Integer> groupIds = gt.getListById(lst3, true); // id类型String,Integer,
//																// true表示是Integer，省略或false表示String，
//		for (Integer id : groupIds)
//			System.out.println(id);
	}
}
