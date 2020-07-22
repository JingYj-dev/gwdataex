package com.hnjz.apps.oa.dataexsys.pkg.model;

import org.dom4j.Node;

import java.util.Date;

public class Signature extends PkgData {
	
	private String signatureBody;
	private String signatureName;
	private Date signatureTime;
	
	public Signature(){}
	public Signature(Object obj) throws Exception{
		load(obj);
	}
	
	public void load(Object obj) throws Exception{
		if(obj!=null && obj instanceof Node){
			Node node = (Node)obj;
			try {
				this.signatureBody = getStringValue(node, "签名体", true);
				this.signatureName = getStringValue(node, "签名者", true);
				this.signatureTime =  com.hnjz.util.DateUtil.getDate(getStringValue(node, "签名时间", true), "yyyy-MM-dd HH:mm:ss");
			} catch (Exception e) {
				throw new Exception("error to load signature:["+node.asXML()+"]",e);
			}
		}
	}
	
	public String asXML(){
		StringBuffer sb = new StringBuffer("");
		sb.append(pkgXml("签名体",getSignatureBody()));
		sb.append(pkgXml("签名者",getSignatureName()));
		sb.append(pkgXml("签名时间",com.hnjz.util.DateUtil.getDateStr(getSignatureTime(), "yyyy-MM-dd HH:mm:ss")));
		return sb.toString();
	}
	
	
	public String getSignatureBody() {
		return signatureBody;
	}
	public void setSignatureBody(String signatureBody) {
		this.signatureBody = signatureBody;
	}
	public String getSignatureName() {
		return signatureName;
	}
	public void setSignatureName(String signatureName) {
		this.signatureName = signatureName;
	}
	public Date getSignatureTime() {
		return signatureTime;
	}
	public void setSignatureTime(Date signatureTime) {
		this.signatureTime = signatureTime;
	}
}
