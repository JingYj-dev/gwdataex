package com.hnjz.apps.oa.dataexsys.admin.service;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransParam;
import com.hnjz.db.query.QueryCache;

public class DataexSysTransParamService {
	public static String getParamValue(Integer paramId){
		if(paramId == null) {
			return "";
		}
		DataexSysTransParam item = QueryCache.get(DataexSysTransParam.class, paramId);
		if (item != null) {
			return item.getParamValue();
		} else {
			return "";
		}
		
	}
}
