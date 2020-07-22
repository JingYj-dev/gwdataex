package com.hnjz.apps.base.security.service;

import com.hnjz.apps.base.security.model.SecParam;
import com.hnjz.db.query.QueryCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SecurityService {

private static Log log = LogFactory.getLog(SecurityService.class);
	
	public static String getParamValue(Integer paramId){
		if(paramId == null){
			return "";
		}
		SecParam item = QueryCache.get(SecParam.class, paramId);
		if(item != null){
			return item.getParamValue();
		}else{
			return "";
		}
		
	}
}
