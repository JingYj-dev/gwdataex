package com.hnjz.apps.base.security.model;

public interface SecurityParam {
	
	//密码强度
	int PASSWORD_LEVEAL = 1;
	//最大登录失败次数
	int MAX_LOGIN_FAIL = 2;
	//登录方式
	int LOGIN_STYLE = 3;
	//初始密码
	int INIT_PASSWORD = 4;
	//待激活密码过期时间
	int OUTDATE_PASSWORD = 5;
	//激活日期
	int ACTIVE_TIME = 6;
	//session超时
	int SESSION_TIMEOUT = 7;
	//下载MD5验证
	int MD5_CHECK = 8;
	
	long DEFAULT_SESSION_TIMEOUT = 600;
}


