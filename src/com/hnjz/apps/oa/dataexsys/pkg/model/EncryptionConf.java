package com.hnjz.apps.oa.dataexsys.pkg.model;

import org.dom4j.Node;

public class EncryptionConf extends PkgData {
	
	private String secretLevel;
	private String signatureArithmetic;
	private String encrytionArithmetic;
	
	public void load(Object obj) throws Exception{
		if(obj!=null && obj instanceof Node){
			Node node = (Node)obj;
			this.secretLevel = getStringValue(node, "保密分级");
			this.signatureArithmetic =  getStringValue(node, "签名算法");
			this.encrytionArithmetic =  getStringValue(node, "加密算法");
		}
	}
	
	public String asXML(){
		StringBuffer sb = new StringBuffer("");
		sb.append(pkgXml("保密分级",getSecretLevel()));
		sb.append(pkgXml("签名算法",getSignatureArithmetic()));
		sb.append(pkgXml("加密算法",getEncrytionArithmetic()));
		return sb.toString();
	}
	
	
	public String getSecretLevel() {
		return secretLevel;
	}
	public void setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
	}
	public String getSignatureArithmetic() {
		return signatureArithmetic;
	}
	public void setSignatureArithmetic(String signatureArithmetic) {
		this.signatureArithmetic = signatureArithmetic;
	}
	public String getEncrytionArithmetic() {
		return encrytionArithmetic;
	}
	public void setEncrytionArithmetic(String encrytionArithmetic) {
		this.encrytionArithmetic = encrytionArithmetic;
	}
}
