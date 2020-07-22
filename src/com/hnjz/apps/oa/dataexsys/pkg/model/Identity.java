package com.hnjz.apps.oa.dataexsys.pkg.model;

import org.dom4j.Node;

public class Identity extends PkgData {
	
	private String identityFlag;
	private String identityName;
	private String identityDesc;
	
	public Identity(){}
	public Identity(Object obj) throws Exception{
		load(obj);
	}
	
	public void load(Object obj) throws Exception{
		if(obj!=null && obj instanceof Node){
			Node node = (Node)obj;
			this.identityFlag = getStringValue(node, "身份标识", true);
			this.identityName = getStringValue(node, "身份名称", true);
			this.identityDesc = getStringValue(node, "身份描述");
		}
	}
	
	public String asXML(){
		StringBuffer sb = new StringBuffer("");
		sb.append(pkgXml("身份标识",getIdentityFlag()));
		sb.append(pkgXml("身份名称",getIdentityName()));
		sb.append(pkgXml("身份描述",getIdentityDesc()));
		return sb.toString();
	}
	
	
	public String getIdentityFlag() {
		return identityFlag;
	}
	public void setIdentityFlag(String identityFlag) {
		this.identityFlag = identityFlag;
	}
	public String getIdentityName() {
		return identityName;
	}
	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}
	public String getIdentityDesc() {
		return identityDesc;
	}
	public void setIdentityDesc(String identityDesc) {
		this.identityDesc = identityDesc;
	}
}
