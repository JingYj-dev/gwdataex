package com.hnjz.apps.oa.dataexsys.pkg.model;

import org.dom4j.Node;

/**
 * 封装实体
 * @author fangwq
 *
 */
public class GwContent extends PkgData {
	//密级:秘密、机密、绝密、无
	private String security;
	//紧急程度：特急、加急、无
	private String emergency;
	private String sender;
	//多个收文机关，用逗号"，"隔开
	private String receiver;
	private String title;
	private String content;
	//扩展属性
	private String exp;
	//自定义属性
	private Expansion expansion = new Expansion(null);
	
	public GwContent(){}
	public GwContent(Node node) throws Exception {
		load(node);
	}
	public GwContent(String xml) throws Exception {
		Node node = xmlToNode(xml);
		load(node);
	}
	
	/**
	 * 加载node
	 * @param node node为/公文封装包/封装实体  或者document
	 * @throws Exception
	 */
	public void load(Object obj) throws Exception{
		if(obj!=null && obj instanceof Node){
			Node node = (Node)obj;
			//公文信息
			if(node!=null){
				this.security = getStringValue(node, "密级", true);
				this.emergency = getStringValue(node, "紧急程度", true);
				this.sender = getStringValue(node, "发文机关");
				this.receiver = getStringValue(node, "收文机关");
				this.title = getStringValue(node, "公文标题", true);
				this.content = getStringValue(node, "公文内容", true);
				this.exp = getStringValue(node, "扩展属性");
				Node expNode = selectSingleNode(node, "自定义属性");
				if(expNode!=null){
					if(expNode.getStringValue().equals(expNode.getText())){
						this.expansion.load(expNode.getText());
					}else{
						this.expansion.load(expNode);
					}
				}
			}
		}
	}
	
	public String asXML(){
		StringBuffer sb = new StringBuffer("");
		sb.append(pkgXml("密级",getSecurity()));
		sb.append(pkgXml("紧急程度",getEmergency()));
		sb.append(pkgXml("发文机关",getSender()));
		sb.append(pkgXml("收文机关",getReceiver()));
		sb.append(pkgXml("公文标题",getTitle()));
		sb.append(pkgXml("公文内容",getContent()));
		sb.append(pkgXml("扩展属性",getExp()));
		sb.append(pkgXml("自定义属性",getExpansion().asXML()));
		return sb.toString();
	}
	
	
	public String getSecurity() {
		return security;
	}
	public void setSecurity(String security) {
		this.security = security;
	}
	public String getEmergency() {
		return emergency;
	}
	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Expansion getExpansion() {
		return expansion;
	}
	public void setExpansion(Expansion expansion) {
		this.expansion = expansion;
	}


	public String getExp() {
		return exp;
	}


	public void setExp(String exp) {
		this.exp = exp;
	}
	
}
