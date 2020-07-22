/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file OrgTree.java creation date: [Feb 18, 2014 10:01:45 AM] by Xingzhc
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.dict.service.QueryDict;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.core.model.tree.Tree;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;

import java.util.List;

public class OrgTree extends Tree {

	private static OrgTree instance = null;
	private OrgTree() {
	}
	
	public static synchronized OrgTree getInstance() {
		if (instance == null)
			instance = new OrgTree();
		instance.reloadTreeCache();
		return instance;
	}
	@Override
	protected Object getBindData(TreeNode node) {
		// TODO Auto-generated method stub
		return QueryDict.get(getTreeClass(), node.getNodeId());
	}

	@Override
	protected Class getTreeClass() {
		// TODO Auto-generated method stub
		return SOrg.class;
	}

	@Override
	protected void reloadTree() {
		// TODO Auto-generated method stub
		List nodes = new QueryCache("select a.uuid from SOrg a where a.delFlag='2' order by orderNum").list();
		nodes = QueryCache.idToObj(SOrg.class, nodes);
//		SOrg root = new SOrg();
//		root.setParentId(null);
//		root.setUuid("0");
//		root.setOrderNum(0);
//		nodes.add(root);
		super.reload(nodes);
	}

	@Override
	protected TreeNode transform(Object info) {
		// TODO Auto-generated method stub
		SOrg sOrg = (SOrg) info;
		TreeNode node = new TreeNode();
		node.setNodeId(sOrg.getUuid().toLowerCase());
		node.setParentId(sOrg.getParentId() == null ? "" : sOrg.getParentId().toString());
		node.setOrderNo(sOrg.getOrderNum() == null ? 0 : sOrg.getOrderNum());
		return node;
	}

	public static void main(String [] args) {
		OrgTree gt = OrgTree.getInstance();
		System.out.println(gt);
		System.out.println("-------------------根节点下的所有节点：");
		List<TreeNode> lst1 = gt.getRootNode().getAllChildren();
		System.out.println(lst1.size()+"=========");
		for (TreeNode item : lst1)
			System.out.println(item.getNodeId());
		System.out.println("-------------------根节点下一层的节点：");
		List<TreeNode> lst2 = gt.getRootNode().getChildren();
		System.out.println(lst2.size()+"===");
		for (TreeNode item : lst2)
			System.out.println(item.getNodeId());
		System.out.println("-------------------id=1技术部下层节点：");
		TreeNode tn = gt.getTreeNode("8a8d81be43b8f0200143b8f5113b0002");// 获取技术部所在组id="1"的树节点TreeNode对象
		List<TreeNode> lst3 = tn.getChildren();
		
		List<SOrg> orgs = gt.getList(lst3);
		for (SOrg org : orgs)
			System.out.println(org.getName());
//		System.out.println("-------------------id=1技术部下层节点id：");
//		List<Integer> groupIds = gt.getListById(lst3, true); // id类型String,Integer,
//																// true表示是Integer，省略或false表示String，
//		for (Integer id : groupIds)
//			System.out.println(id);
	}
}


