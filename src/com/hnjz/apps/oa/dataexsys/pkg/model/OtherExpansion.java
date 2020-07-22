package com.hnjz.apps.oa.dataexsys.pkg.model;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

public class OtherExpansion extends DefaultExpansion {
	protected String other;
	
	public OtherExpansion(){}
	public OtherExpansion(String xml) throws Exception{
		super(xml);
		init();
	}
	public OtherExpansion(Object o) throws Exception{
		super(o);
		init();
	}
	
	public String asXML() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.asXML());
		sb.append(pkgXml("其他",getOther()));
		return sb.toString();
	}
	
	public String asXML(String receiverFlag) {
		StringBuffer sb = new StringBuffer();
		sb.append(super.asXML(receiverFlag));
		sb.append(pkgXml("其他",getOther()));
		return sb.toString();
	}
	

	
	protected void init() throws Exception{
		Node node = null;
		try {
			node = xmlToNode(this.xml);
			if(node != null && node instanceof Document){
				node = (Node) node.selectSingleNode("自定义属性");
			}
		} catch (DocumentException e) {
		}
		if(node!=null){
			try {
				super.init();
				Node otherNode = node.selectSingleNode("其他");
				if(otherNode!=null){
					other = otherNode.getText();
				}
			} catch (Exception e) {
				throw new Exception("error to load numexpansion:["+node.asXML()+"]",e);
			}
		}
	}
	public String getOther() {
		if(other==null)return "";
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
}
