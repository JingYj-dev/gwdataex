package com.hnjz.apps.base.log.model;

public interface LogConstant {
	/*
	 * 1、添加 
	 * 2、删除 
	 * 3、修改
	 * 4、列表查询 
	 * 5、单条查询  
	 * 6、登录  
	 * 7、注销   
	 * 8、打印  
	 * 9、上传  
	 * 10、下载 
	 * 11、其它
	*/
	int LOG_TYPE_UNKOWN = 0;
	int LOG_TYPE_ADD = 1;
	int LOG_TYPE_DELETE = 2;
	int LOG_TYPE_MODIFY = 3;
	int LOG_TYPE_QUERY = 4;
	int LOG_TYPE_GET = 5;
	int LOG_TYPE_LOGON = 6;
	int LOG_TYPE_LOGOFF = 7;	
	int LOG_TYPE_PRINT = 8;	
	int LOG_TYPE_UPLOAD = 9;
	int LOG_TYPE_DOWNLOAD = 10;
	int LOG_TYPE_OTHERS = 11;
	
	//事件类型:
	/*0.普通业务操作； 
	1.身份鉴别； 
	2.审计操作； 
	3.用户增加、删除； 
	4.授权更改； 
	5.三员的管理操作； 
	6.违规访问操作；  
	7.发文操作； 
	9.收文操作； 
	10.归档操作； 
	11.用户登录注销;
	12.其它可审计事件;
	13.系统安全事件
	14.公文交换
	*/
	/*
	int LOG_EVENT_BIZ = 0;
	int LOG_EVENT_EQUIP_EVENT = 1;
	int LOG_EVENT_AUDIT_SET = 2;
	int LOG_EVENT_USER_MAN = 3;
	int LOG_EVENT_USER_POWER = 4;
	int LOG_EVENT_ADMIN = 5;
	int LOG_EVENT_BREAK_RULE = 6;
	int LOG_EVENT_ID_AA = 7;
	int LOG_EVENT_ACL = 8;
	int LOG_EVENT_SEC_DATA_INOUT = 9;
	int LOG_EVENT_SEC_DATA_OPERATION = 10;
	int LOG_EVENT_OTHER_SEC_EVENT = 11;
	int LOG_EVENT_SPEC_AUDIT_EVENT = 12;
	*/
	
	//业务操作结果
	String RESULT_SUCCESS="success";
	String RESULT_ERROR = "error";
	
	//操作重要程度
	int LOG_LEVEL_COMMON = 1; //普通
	int LOG_LEVEL_IMPORTANT = 2; //重要
	int LOG_LEVEL_SEVERE = 3; //严重
}
