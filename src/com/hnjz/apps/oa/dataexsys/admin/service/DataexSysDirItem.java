package com.hnjz.apps.oa.dataexsys.admin.service;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file orgItem.java
 * http://www.css.com.cn
 */

public class DataexSysDirItem {
	
	private static Log log = LogFactory.getLog(DataexSysDirItem.class);
	/**
	 * 获取目录树根节点
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getRootDataexSysDir() throws JSONException{
		DataexSysDirTree gt = DataexSysDirTree.getInstance();
		TreeNode root = gt.getRootNode();
		DataexSysDir item = QueryCache.get(DataexSysDir.class, root.getNodeId());
		if(item == null) {
			return null;
		}
		JSONObject one = new JSONObject();
		one.put("id", item.getUuid());
		one.put("name", item.getDirName());
		one.put("pId", item.getParentId());
		one.put("isParent", true);
		one.put("open", true);
		return one;
	}
	
	/**
	 * 获得子目录，封装成jsonArray
	 * @param parentId
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray getSubDataexSysDir(String parentId) throws JSONException {
		DataexSysDirTree gt = DataexSysDirTree.getInstance();
		TreeNode tn = null;
		if(StringHelper.isEmpty(parentId)){
			tn = gt.getRootNode();
		} else {
			tn = gt.getTreeNode(parentId);
		}		
		List<TreeNode> lst = tn.getChildren();
		JSONArray dataexSysDirTree = new JSONArray();
		JSONObject one = null;
		for (TreeNode node : lst){
			DataexSysDir item = QueryCache.get(DataexSysDir.class, node.getNodeId());
			if(item != null){
				one = new JSONObject();
				one.put("id", node.getNodeId());
				one.put("name", item.getDirName());
				one.put("pId", item.getParentId());
				one.put("isParent", !node.isLeaf());
				dataexSysDirTree.add(one);
			}
		}
		return dataexSysDirTree;
	}
	
	//以下为新增页面获取目录树-------------start-------------------------------//
	public static JSONArray getDataexSysDirRadio(String dataexSysDirId) throws JSONException{
		DataexSysDirTree gt = DataexSysDirTree.getInstance();
		List<TreeNode> lst = new ArrayList<TreeNode>();
		lst.add(gt.getRootNode());
		lst.addAll(gt.getRootNode().getAllChildren());
		JSONObject one;
		JSONArray dataexSysDirTree = new JSONArray();
		for (TreeNode o : lst) {
			DataexSysDir item = QueryCache.get(DataexSysDir.class, o.getNodeId());
			if(item == null) {
				continue;
			}
			one = new JSONObject();
			one.put("id", item.getUuid());
			one.put("name", item.getDirName());
			one.put("pId", item.getParentId());
			if(o.getChildren() == null || o.getChildren().size() == 0) {
				one.put("isParent", false);
			} else {
				one.put("isParent", true);
			}
			if(dataexSysDirId != null && dataexSysDirId.equals(item.getUuid())) {
				one.put("checked", true);
			}
			dataexSysDirTree.add(one);
		}
		return dataexSysDirTree;
	}
	
	public static JSONObject getRootDataexSysDirRadio(String parentId, String dataexSysDirId) throws JSONException {
		DataexSysDir item = QueryCache.get(DataexSysDir.class, parentId);
		if(item == null) {
			return null;
		}
		JSONObject one = new JSONObject();
		one.put("id", item.getUuid());
		one.put("name", item.getDirName());
		one.put("pId", item.getParentId());
		one.put("isParent", true);
		if(dataexSysDirId != null && dataexSysDirId.equals(item.getUuid())){
			one.put("checked", true);
		}
		one.put("open", true);
		return one;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray getSubDataexSysDirRadio(String parentId, String dataexSysDirId) throws JSONException{
		List<?> uuids = getDataexSysDirIds(parentId);
		List<DataexSysDir> lst = QueryCache.idToObj(DataexSysDir.class, uuids);
		JSONArray dataexSysDirTree = new JSONArray();
		JSONObject one;
		List<?> ids = null;
		if(lst != null)
			for(DataexSysDir item : lst) {
				one = new JSONObject();
				one.put("id", item.getUuid());
				one.put("name", item.getDirName());
				one.put("pId", item.getParentId());
				ids = getDataexSysDirIds(item.getUuid());
				if(ids == null || ids.size() < 1) {
					one.put("isParent", false);
				} else {
					one.put("isParent", true);
				}
				if(dataexSysDirId != null && dataexSysDirId.equals(item.getUuid())) {
					one.put("checked", true);
				}
				dataexSysDirTree.add(one);
			}
		return dataexSysDirTree;
	}
	
	public static List<?> getDataexSysDirIds(String parentId){
		QueryCache qc = new  QueryCache("select a.uuid from DataexSysDir a where a.parentId =:parentId and a.dirStatus='1'")
		.setParameter("parentId", parentId);
		return qc.list();
	}
	
	//以上为新增页面获取目录树-------------end-------------------------------//
	
	/**
	 * 根据dirOrg机构编码获取DataexSysDir对象
	 * @author mazhh on Jun 30, 2014 11:32:20 AM
	 * @param dirOrg
	 * @return
	 */
	public static DataexSysDir getDirByDirOrg(String dirOrg){
		DataexSysDir dir = null;
		try {
			QueryCache qc = new QueryCache(" from DataexSysDir a where a.dirStatus='1' and a.dirOrg=:dirOrg").setParameter("dirOrg", dirOrg);
			dir = (DataexSysDir)qc.uniqueResult();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return dir;
	}
	
	/**
	 * 根据机构名称得到多个机构dirOrg
	 * @param orgName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getDirOrgsByName(String dirName) {
		List<String> orgList = null;
		QueryCache qc = new QueryCache("select a.dirOrg from DataexSysDir a where a.dirName like :dirName ").setParameter("dirName", "%" + dirName + "%");
		orgList = qc.list();
		String orgs = "";
		if (orgList != null && orgList.size() > 0) {
			for (String org : orgList) {
				orgs += "'" + org + "',";
			}
		}
		orgs = orgs.length() > 0 ? orgs.substring(0, orgs.length() - 1) : "''";
		return orgs;
	}
	
	/**
	 * 得到所有的目录机构编码
	 * @param orgName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getDirOrgs() {
		List<String> orgList = null;
		QueryCache qc = new QueryCache("select a.dirOrg from DataexSysDir a where a.dirStatus = '1' ");
		orgList = qc.list();
		return orgList;
	}
	
	/**
	 * 根据流水内容ID在任务表中得到对应的接收机关名称
	 * @param contentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getDirNamesByContentId(String contentId) {
		String orgNames = "";
		List<String> orgNameList = null;
		QueryCache qc = new QueryCache("select a.targetOrgName from DataexSysTransTask a where a.contentId = :contentId ").setParameter("contentId", contentId);
		orgNameList = qc.list();
		if (orgNameList != null && orgNameList.size() > 0) {
			for (String recvOrg : orgNameList) {
				orgNames += recvOrg + ",";
			}
			orgNames = StringHelper.isNotEmpty(orgNames) ? orgNames.substring(0, orgNames.length() - 1) : "";
		}
		return orgNames;
	}
}
