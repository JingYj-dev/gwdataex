package com.hnjz.apps.base.audit.service;

public interface AuditParameter {
	
	//是否开启日志
	int LOG_OPEN = 1;
	//日志上限
	int LOG_MAX = 2;
	//日志预警
	int LOG_WARN = 3;
	
	long DEFAULT_LOG_MAX = 1000000;
	long DEFAULT_LOG_WARN = 90;
}


