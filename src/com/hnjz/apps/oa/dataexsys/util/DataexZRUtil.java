package com.hnjz.apps.oa.dataexsys.util;

public interface DataexZRUtil {
	//交换系统
	//交换系统接收处理系统签收信息,返回值为方正xml
	public String getSignInfo(String xml, String sendId);
	//交换系统接收处理系统打印信息
	public String getPrintInfo(String xml, String sendId, String type);
	
}
