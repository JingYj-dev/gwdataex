package com.hnjz.apps.base.org.service;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.org.model.SUserOrg;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.StringHelper;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.db.query.QueryNoCache;
import com.hnjz.db.query.TransactionNoCache;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizationService {
	public void saveOrganization(SOrg org, TransactionCache tx) throws Exception {
		String orgId = org.getUuid();
		SOrg organization = QueryCache.get(SOrg.class, orgId);
		DataexSysDir dept = null;
		if(organization == null){
			organization = org;
			dept = new DataexSysDir();
			dept.setUuid(org.getUuid());
			dept.setParentId(org.getParentId());
			dept.setDirName(org.getName());
			dept.setDirOrg(org.getUnitCode());
			if("1".equals(org.getOpenFlag())){
				dept.setDirStatus("1");
			}else{
				dept.setDirStatus("0");
			}
			tx.save(dept);
			tx.save(organization);
		}else{
			dept = QueryCache.get(DataexSysDir.class, orgId);
			dept.setParentId(org.getParentId());
			dept.setDirName(org.getName());
			dept.setDirOrg(org.getUnitCode());
			if("1".equals(org.getOpenFlag())){
				dept.setDirStatus("1");
			}else{
				dept.setDirStatus("0");
			}
			organization.setName(org.getName());
			organization.setParentId(org.getParentId());
			organization.setOpenFlag(org.getOpenFlag());
			organization.setUnitCode(org.getUnitCode());
			organization.setCode(org.getCode());
			organization.setOrderNum(org.getOrderNum());
			tx.update(organization);
			tx.update(dept);
		}
	}
	@SuppressWarnings("unchecked")
	public void removeOrganization(String orgIds, TransactionCache tx) throws Exception {
		List<String> ids = StringHelper.strToList(orgIds);
		if(ids != null && ids.size()>0){
			for(String id : ids){
				List<String> idList = new QueryCache("select t.uuid from s_org t start with t.uuid =:orgId connect by prior t.uuid = t.parentid",true).setParameter("orgId", id).list();
				if(idList != null && idList.size()>0){
					List<SOrg> orgs = QueryCache.idToObj(SUserOrg.class, idList);
					List<String> userOrgIds = new QueryCache("select a.uuid from SUserOrg a where a.orgId in (:orgIds)").setParameter("orgIds", idList).list();
					List<SUserOrg> userOrgs = QueryCache.idToObj(SUserOrg.class, userOrgIds);
					tx.delete(userOrgs);
					for(SOrg org : orgs){
						org.setDelFlag("1");
						tx.update(org);
					}
				}
			}
		}
	}
	/**
	 * 获取机构三位唯一编码
	 * @param orguuid
	 * @return
	 */
	public static String getUnitCode(String orguuid){
		String unitCode = "";
		String orgId = "";
		for (int i = 31; i > 0; i--) {
			unitCode = orguuid.substring(i - 2, i + 1);
			orgId = (String) new QueryCache("select a.uuid from SOrg a where a.unitCode =:unitCode").setParameter("unitCode",unitCode).uniqueResult();
			if (StringHelper.isEmpty(orgId)) {
				break;
			}
		}
		return unitCode;
	}
	
	/**
	 * 复制src中的非空值到dest (可以提取到工具包)
	 * @param src
	 * @param dest
	 */
	public static void copyNotNullProperties(SOrg src,SOrg dest) {
		if(StringHelper.isNotEmpty(src.getDelFlag())){dest.setDelFlag(src.getDelFlag());}
		if(StringHelper.isNotEmpty(src.getOpenFlag())){dest.setOpenFlag(src.getOpenFlag());}
		if(StringHelper.isNotEmpty(src.getName())){dest.setName(src.getName());}
		if((src.getOrderNum()!=null&&!src.getOrderNum().equals(""))){dest.setOrderNum(src.getOrderNum());}
		if(StringHelper.isNotEmpty(src.getParentId())){dest.setParentId(src.getParentId());}
		if(StringHelper.isNotEmpty(src.getUnitCode())){dest.setUnitCode(src.getUnitCode());}
	}
	
	
	@SuppressWarnings("null")
	public void saveZzzwOrganization(Map<String,String> map, TransactionCache tx) throws Exception {
		String orgId = map.get("id");
		String name = map.get("name");
		String pid = map.get("pid");
		String code = map.get("code");
		String isActive = map.get("isActive");
		if("Y".equals(isActive)){
			isActive = "1";
		}else{
			isActive = "0";
		}
		String orderNum = map.get("orderNum");
		SOrg organization = QueryCache.get(SOrg.class, orgId);
		DataexSysDir dept = QueryCache.get(DataexSysDir.class, orgId);
		
		if(organization == null){
			SOrg org = new SOrg();
			org.setUuid(orgId);
			org.setName(name);
			org.setParentId(pid);
			if(StringHelper.isNotEmpty(orderNum)){org.setOrderNum(Integer.parseInt(orderNum));};
			org.setOpenFlag(isActive);
			org.setDelFlag("2");
			org.setCode(code);
			org.setUnitCode(code);
			tx.save(org);
		}else{
			organization.setName(name);
			organization.setParentId(pid);
			if(StringHelper.isNotEmpty(orderNum)){organization.setOrderNum(Integer.parseInt(orderNum));};
			organization.setOpenFlag(isActive);
			organization.setDelFlag("2");
			organization.setCode(code);
			organization.setUnitCode(code);
			tx.update(organization);
		}
		if(dept == null){
			dept = new DataexSysDir();
			dept.setUuid(orgId);
			dept.setParentId(pid);
			dept.setDirName(name);
			dept.setDirOrg(code);
			dept.setExnodeId("89128d815e73428f8726dc3b75201de8");
			if("1".equals(isActive)){
				dept.setDirStatus("1");
			}else{
				dept.setDirStatus("0");
			}
			tx.save(dept);
		}else{
			dept.setParentId(pid);
			dept.setDirName(name);
			dept.setDirOrg(code);
			dept.setExnodeId("89128d815e73428f8726dc3b75201de8");
			if("1".equals(isActive)){
				dept.setDirStatus("1");
			}else{
				dept.setDirStatus("0");
			}
			tx.update(dept);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveZzzwOrganizationNew(JSONArray array, TransactionNoCache tx) throws Exception {
		//List<String> idList = new QueryCache("select a.uuid from SOrg a").list();
		//重写查询方法
		List<SOrg> orgList = new QueryNoCache().getObjects(SOrg.class, "select a.* from s_org a ");
		List<DataexSysDir> dirList = new QueryNoCache().getObjects(DataexSysDir.class, "select a.* from dataex_sys_dir a ");
		Map<String,Integer> orgMap = new HashMap<String,Integer>();
		Map<String,Integer> dirMap = new HashMap<String,Integer>();
		List<SOrg> orgAddList = new ArrayList<SOrg>();
		List<DataexSysDir> dirAddList = new ArrayList<DataexSysDir>();
		int len = orgList.size();
		for(int i=0;i<len;i++){
			orgMap.put(orgList.get(i).getUuid(), i);
			dirMap.put(dirList.get(i).getUuid(), i);
		}
		String orgId = "";
		String name = "";
		String pid = "";
		String code = "";
		String isActive = "";
		String orderNum = "";
		SOrg org = null;
		DataexSysDir dept = null;
		JSONObject obj = null;
		for(int j=0;j<array.size();j++){
			obj = array.getJSONObject(j);
			orgId = obj.getString("id");
			name = obj.getString("name");
			pid = obj.getString("pid");
			code = obj.getString("code");
			isActive = obj.getString("isActive");
			if("Y".equals(isActive)){
				isActive = "1";
			}else{
				isActive = "0";
			}
			orderNum = obj.getString("orderNum");
			//处理org表
			if(orgMap.get(orgId) != null){
				org = orgList.get(orgMap.get(orgId));
				org.setName(name);
				org.setParentId(pid);
				if(StringHelper.isNotEmpty(orderNum)){org.setOrderNum(Integer.parseInt(orderNum));};
				org.setOpenFlag(isActive);
				org.setDelFlag("2");
				org.setCode(code);
				org.setUnitCode(code);
			}else{
				org = new SOrg();
				org.setUuid(orgId);
				org.setName(name);
				org.setParentId(pid);
				if(StringHelper.isNotEmpty(orderNum)){org.setOrderNum(Integer.parseInt(orderNum));};
				org.setOpenFlag(isActive);
				org.setDelFlag("2");
				org.setCode(code);
				org.setUnitCode(code);
				orgAddList.add(org);
			}
			//处理dataexsysdir表
			if(dirMap.get(orgId) != null){
				dept = dirList.get(dirMap.get(orgId));
				dept.setParentId(pid);
				dept.setDirName(name);
				dept.setDirOrg(code);
				dept.setExnodeId("89128d815e73428f8726dc3b75201de8");
				if("1".equals(isActive)){
					dept.setDirStatus("1");
				}else{
					dept.setDirStatus("0");
				}
			}else{
				dept = new DataexSysDir();
				dept.setUuid(orgId);
				dept.setParentId(pid);
				dept.setDirName(name);
				dept.setDirOrg(code);
				dept.setExnodeId("89128d815e73428f8726dc3b75201de8");
				if("1".equals(isActive)){
					dept.setDirStatus("1");
				}else{
					dept.setDirStatus("0");
				}
				dirAddList.add(dept);
			}
		}
		tx.updateList(orgList);
		tx.updateList(dirList);
		tx.saveList(orgAddList);
		tx.saveList(dirAddList);
	}
}
