/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file OrgTree.java creation date: [Feb 18, 2014 10:01:45 AM] by mazhh
 * http://www.css.com.cn
 */

package com.hnjz.apps.oa.dataexsys.admin.service;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.core.model.tree.Tree;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;

import java.util.List;

public class DataexSysDirTree extends Tree {

	private static DataexSysDirTree instance = null;
	private DataexSysDirTree() {
	}
	
	public static synchronized DataexSysDirTree getInstance() {
		if (instance == null) {
			instance = new DataexSysDirTree();
		}
		instance.reloadTreeCache();
		//instance.reloadTree();
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object getBindData(TreeNode node) {
		return QueryCache.get(getTreeClass(), node.getNodeId());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class getTreeClass() {
		return DataexSysDir.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reloadTree() {
		List nodes = new QueryCache("select a.uuid from DataexSysDir a ").list();
		nodes = QueryCache.idToObj(DataexSysDir.class, nodes);
		DataexSysDir root = new DataexSysDir();
		root.setParentId(null);
		root.setUuid("0");
		nodes.add(root);
		super.reload(nodes);
	}

	@Override
	protected TreeNode transform(Object info) {
		DataexSysDir sysDir = (DataexSysDir)info;
		TreeNode node = new TreeNode();
		node.setNodeId(sysDir.getUuid().toLowerCase());
		node.setParentId(sysDir.getParentId() == null ? "" : sysDir.getParentId().toString());
		//node.setOrderNo(sysDir.getOrderNum() == null ? 0 : sysDir.getOrderNum());
		return node;
	}
}


