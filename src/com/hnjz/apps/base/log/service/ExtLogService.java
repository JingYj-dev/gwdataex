package com.hnjz.apps.base.log.service;

public class ExtLogService extends LogService {
	protected   String getTableName() throws Exception {	  
		return "S_LOG_" + com.hnjz.util.DateTimeUtil.getLogDateString();
	}
}
