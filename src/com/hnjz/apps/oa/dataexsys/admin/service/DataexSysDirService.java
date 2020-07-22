package com.hnjz.apps.oa.dataexsys.admin.service;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.StringHelper;
import com.westone.middleware.toolkit.trustService.resource.Resource;
import com.westone.middleware.toolkit.trustService.resource.ResourceProperty;

import java.util.HashMap;
import java.util.List;

public class DataexSysDirService {
	private void saveOrUpdateOrganization(DataexSysDir dept,String function) throws Exception {
		TransactionCache tx = null;
		try {
			tx = new TransactionCache();
			if (function.equals("save")) {
				tx.save(dept);
			}else{
				tx.update(dept);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
				
		
	}
	public Boolean saveOrganization(Resource resource) throws Exception {
		// TODO Auto-generated method stub
		DataexSysDir dept = new DataexSysDir();
		String sorgId = resource.getNo().replaceAll("[\\pP\\p{Punct}]", "");
		dept.setUuid(sorgId);
		if (resource.getStatus() == 0) {
			dept.setDirStatus("1");
		} else if (resource.getStatus() == 1) {
			dept.setDirStatus("0");
		} else {
			dept.setDirStatus("0");
		}
		
		dept.setDirName(resource.getName());
		List<ResourceProperty> properties1 = resource.getProperties();
		HashMap<String, ResourceProperty> properties = new HashMap<String, ResourceProperty>();
		
		for (ResourceProperty rp : properties1) {
			properties.put(rp.getOid(), rp);
		}
		
		if (properties.containsKey("1.2.156.10197.6.1.2.301.2.106")){
			dept.setParentId(properties.get("1.2.156.10197.6.1.2.301.2.106").getValue().replaceAll("[\\pP\\p{Punct}]", ""));
		}
		/*String sql = "from DataexSysDir where uuid =:uuid";
		QueryCache cache = new QueryCache(sql);
		DataexSysDir depT = (DataexSysDir) cache.setParameter("uuid", dept.getUuid()).uniqueResult();
		for (int i = 31; i > 0; i--) {
			String uintCode = (String) new QueryCache("select a.uuid from DataexSysDir a where a.dirOrg =:deptCode").setParameter("deptCode", (resource.getNo().replaceAll("[\\pP\\p{Punct}]", "").substring(i-2, i+1))).uniqueResult();
			if (StringHelper.isEmpty(uintCode)) {
				dept.setDirOrg(resource.getNo().replaceAll("[\\pP\\p{Punct}]", "").substring(i-2, i+1));
				break;
			}
		}*/
		DataexSysDir dataexdept = QueryCache.get(DataexSysDir.class,sorgId);
		if(dataexdept!= null){
			copyNotNullProperties(dept,dataexdept);
			this.saveOrUpdateOrganization(dataexdept,"update");
		}else {
			//code只保存不更新
			String code = getUnitCode(sorgId);
			dept.setDirOrg(code);
			this.saveOrUpdateOrganization(dept,"save");
		}
		return true;
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
			orgId = (String) new QueryCache("select a.uuid from DataexSysDir a where a.dirOrg =:unitCode").setParameter("unitCode",unitCode).uniqueResult();
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
	public static void copyNotNullProperties(DataexSysDir src,DataexSysDir dest) {
		if(StringHelper.isNotEmpty(src.getDirStatus())){dest.setDirStatus(src.getDirStatus());}
		if(StringHelper.isNotEmpty(src.getDirName())){dest.setDirName(src.getDirName());}
		if(StringHelper.isNotEmpty(src.getParentId())){dest.setParentId(src.getParentId());}
		if(StringHelper.isNotEmpty(src.getDirOrg())){dest.setDirOrg(src.getDirOrg());}
	}
}
