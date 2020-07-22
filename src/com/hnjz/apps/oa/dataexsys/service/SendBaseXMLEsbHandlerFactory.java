package com.hnjz.apps.oa.dataexsys.service;

import com.hnjz.apps.oa.dataexsys.service.impl.DataexSendBaseXMLEsbHandler;
import com.hnjz.apps.oa.dataexsys.service.impl.DataexSysSendBaseXMLEsbHandler;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class SendBaseXMLEsbHandlerFactory {
	public static SendBaseXMLEsbHandler getHandler(Document doc){
		if(doc==null)return null;
		Element root = doc.getRootElement();
		//标识[交换系统]
		Node dataexsys = root.selectSingleNode("报文头");
		//标识[处理系统]
		Node dataex = root.selectSingleNode("封装实体");
		if (dataexsys != null) {//发送系统类型：1、交换系统
			return DataexSysSendBaseXMLEsbHandler.getInstance();
		} else if (dataex != null) {//发送系统类型：2、处理系统
			return DataexSendBaseXMLEsbHandler.getInstance();
		}
		return null;
	}
}
