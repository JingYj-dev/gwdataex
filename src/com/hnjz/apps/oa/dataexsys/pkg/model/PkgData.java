package com.hnjz.apps.oa.dataexsys.pkg.model;

import com.hnjz.util.StringHelper;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;


public abstract class PkgData {
	public static final String HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String GW = "OFC";
	public static final String RECEIPT = "RET";
	
	public abstract String asXML();
	public abstract void load(Object obj) throws Exception;
	
	public static String pkgXml(String title,String value){
		if(title==null || title.equals("")){
			return "";
		}
		if(value==null || value.equals("")){
//			return "<"+title+"/>";
			return "";
		}else{
			return "<"+title+">"+value+"</"+title+">";
		}
	}
	
	public static Node selectSingleNode(Node node,String title) throws Exception{
		return node.selectSingleNode(title);
	}
	public static Node selectSingleNode(Node node,String title,boolean necc) throws Exception{
		Node target = node.selectSingleNode(title);
		if(necc && target==null){
			throw new Exception("["+title+"] undefined in ["+node.asXML()+"]");
		}
		return target;
	}
	public static String getStringValue(Node node,String title){
		Node target = node.selectSingleNode(title);
		if(target!=null){
			return target.getStringValue();
		}
		return null;
	}
	public static String getStringValue(Node node,String title,boolean necc) throws Exception{
		if(!necc)return getStringValue(node, title);
		Node target = node.selectSingleNode(title);
		if(target==null){
			throw new Exception("["+title+"] not define in "+node.asXML());
		}
		String value = target.getStringValue();
		if(value==null || "".equals(value)){
			throw new Exception("["+title+"] in "+node.asXML()+" is empty");
		}
		return value;
	}
	
	public static Node xmlToNode(String xml) throws DocumentException{
		if(StringHelper.isNotEmpty(xml)){
			String tmp = xml;
			if(xml.indexOf(HEAD)==-1){
				tmp = HEAD + xml;
			}
			try {
				return DocumentHelper.parseText(tmp).getRootElement();
			} catch (DocumentException e) {
				if(xml.indexOf(HEAD)==-1){
					tmp = HEAD + "<tmp>" + xml + "</tmp>";
				}
				try {
					return DocumentHelper.parseText(tmp).getRootElement();
				} catch (DocumentException e1) {
				}
				throw e;
			}
		}
		return null;
	}

}
