package com.hnjz.util;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.util.Json;
import com.opensymphony.webwork.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

public class LogUtil {
	public static LogPart get(Class clazz,String uuid){
		LogPart lp = new LogPart();	
		lp.setOpObjType(clazz.getName());
		lp.setOpObjId(uuid);
		lp.setOpType(LogConstant.LOG_TYPE_GET);
		lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
		lp.save();
		return lp;
	}
	public static LogPart dir(Class clazz){
		LogPart lp = new LogPart();	
		lp.setOpObjType(clazz.getName());
		HttpServletRequest request = ServletActionContext.getRequest();
		if(request!=null){
			lp.setLogData(Json.object2json(request.getParameterMap()));
		}
		lp.setOpType(LogConstant.LOG_TYPE_QUERY);
		lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
		lp.save();
		return lp;
	}
}
