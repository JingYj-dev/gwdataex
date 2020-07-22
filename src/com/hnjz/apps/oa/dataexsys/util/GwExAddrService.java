package com.hnjz.apps.oa.dataexsys.util;

import com.hnjz.util.StringHelper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

//机构用户所属交换箱查询服务
public class GwExAddrService {
	
    public static String getGwExAddr(String actionName,String trustNo ,String appId) throws Exception {

    	Document document = null;

        try {   			
        		//封装信息xml		
				document = DocumentHelper.createDocument();
				Element root = document.addElement("xml");
				//机构|用户
				Element actionNode = root.addElement("action");
				if (StringHelper.isNotEmpty(actionName)) {
					actionNode.addText(actionName);
				}
				//构/用户信任号全网唯一标识
				Element trustnoNode = root.addElement("trustno");
				if (StringHelper.isNotEmpty(trustNo)) {
					trustnoNode.setText(trustNo);
				}			
				//应用系统唯一编码
				Element appidNode = root.addElement("appid");
				if (StringHelper.isNotEmpty(appId)) {
					appidNode.setText(appId);
				}
				
				System.out.println(document.asXML());
				                    
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return document.asXML();
    }        
    
    public static void main(String[] args) throws Exception {
    	getGwExAddr("org_query","111","2222");
	}
}
