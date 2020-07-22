package com.hnjz.apps.base.user.model;

public interface SUserEnvironment {
	
	int RESULT_CODE_UPDPWD = 2;//登录页修改密码
	String ACTIVE_STATUS_CREATE = "1";//ActiveStatus为系统管理员刚刚创建的状态
	String ACTIVE_STATUS_SUB = "2";//ActiveStatus为安全管理员授权后的状态
	String ACTIVE_STATUS_IN = "3";//ActiveStatus用户自己已经激活
	String OPEN_FLAG = "1";//用户的开启状态
	String STR_MAX_LOGIN = "5";//初设最大失败的登录次数
	String STR_OUT_DATE = "7";//初设最大修改密码期限
	String ACTION_UPDPWD = "updpwd";
}
