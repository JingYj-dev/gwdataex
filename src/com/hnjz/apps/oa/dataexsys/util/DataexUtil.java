package com.hnjz.apps.oa.dataexsys.util;

import java.util.Map;

public interface DataexUtil {
	//处理系统
	//返回方正交换的fileId
	//public boolean updFileId(String uuid,String fileid,String startNum);
	public boolean updFileId(Map<String, String> map);
	//交换系统推送待签收公文信息接口
	public String getWaitDocInfo(String xml);
	//交换系统推送公文信息接口
	public String getDocInfo(String xml);
	//接收从中软交换来的正文信息
	public void getDocZip(String xml, String zwPath);
	//接收从方正交换转中软发来的打印信息
	public String getPrintInfo(String xml);
	//接收从中软交换系统发来的打印信息
	public String getZrPrintInfo(String xml);
	//接收从方正交换转中软发来的撤销信息
	public String getCancelInfo(String xml);
	
	//接收从交换系统发来的回执信息
	public String getReceiptInfo(String xml);
}
