package com.hnjz.apps.base.audit.service;

import com.hnjz.apps.base.audit.model.AuditParam;
import com.hnjz.db.query.QueryCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuditParamService {

private static Log log = LogFactory.getLog(AuditParamService.class);
	
	public static String getParamValue(Integer paramId){
		if(paramId == null){
			return "";
		}
		AuditParam item = QueryCache.get(AuditParam.class, paramId);
		if(item != null){
			return item.getParamValue();
		}else{
			return "";
		}
		
	}
}
