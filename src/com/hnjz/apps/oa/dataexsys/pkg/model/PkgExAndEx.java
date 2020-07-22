package com.hnjz.apps.oa.dataexsys.pkg.model;

import org.dom4j.Document;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PkgExAndEx extends PkgData {
	/*报文头*/
	private String msgFlag = "";
	private String protocolVersion = "Apache Axis 1.4";
	//报文发送方
	private Identity msgSender = new Identity();
	//报文接收方
	private Identity msgReceiver = new Identity();
	//保护类型:明文、数字签名、数据加密、数据加密和签名
	private String protectType = "明文";
	//报文属性  公文OFC  回执RET
	private String msgAttr;
	private Date sendTime = new Date();
	//密码算法
	private EncryptionConf encryptionConf = new EncryptionConf();
	private String msgRemark;
	private String transToken;
	/*报文体*/
	private String fromMsgFlag;
	private String content;
	private List<Attach> attachs = new ArrayList<Attach>();
	private Signature signature = new Signature();

	//内容的实例:公文、回执
	private PkgData contentInstance;

	public PkgExAndEx(){}
	public PkgExAndEx(Node node) throws Exception {
		load(node);
	}
	public PkgExAndEx(String xml) throws Exception {
		Node node = xmlToNode(xml);
		load(node);
	}
	public void load(Object obj) throws Exception{
		//如果node是document,即含有"报文传输信封"
		if(obj != null){
			Node node = null;
			if(obj instanceof Document){
				node = ((Document)obj).getRootElement();
			}else{
				node = (Node)obj;
			}
			if(!"报文传输信封".equals(node.getName())){
				throw new Exception("[报文传输信封] not define in "+node.asXML());
			}
			Node header = selectSingleNode(node,"报文头",true);
			try {
				this.msgFlag = getStringValue(header, "报文标识", true);
				this.protocolVersion = getStringValue(header, "协议版本号", true);
				this.msgSender.load(selectSingleNode(header,"报文发送方",true));
				this.msgReceiver.load(selectSingleNode(header,"报文接收方",true));
				this.protectType = getStringValue(header, "保护类型", true);
				this.msgAttr = getStringValue(header, "报文属性");
				this.sendTime = com.hnjz.util.DateUtil.getDate(getStringValue(header, "发出时间", true), "yyyy-MM-dd HH:mm:ss");
				this.encryptionConf.load(selectSingleNode(header, "密码算法"));
				this.msgRemark = getStringValue(header, "报文备注");
				this.transToken = getStringValue(header, "传输票据");
			} catch (Exception e) {
				throw new Exception("error to load header["+header.asXML()+"]",e);
			}
			Node body = selectSingleNode(node,"报文体",true);
			try {
				this.signature.load(selectSingleNode(body, "签名"));
				Node contentNode = selectSingleNode(body,"报文体内容/内容/公文信封",true);
				if(GW.equals(this.msgAttr)){
					Node gwNode = selectSingleNode(contentNode,"公文",true);
					this.content = gwNode.asXML();
					this.contentInstance = new GwContent(gwNode);
				}else if(RECEIPT.equals(this.msgAttr)){
					this.fromMsgFlag = getStringValue(contentNode, "报文标识", false);
					Node expNode = selectSingleNode(contentNode, "短报文内容",true);
					if(expNode.getStringValue().equals(expNode.getText())){
						this.content = expNode.getText();
					}else{
						this.content = expNode.asXML();
					}
					Node receiptNode = selectSingleNode(contentNode,"回执");
					if(receiptNode!=null){
						this.contentInstance = new ReceiptContent(receiptNode);
						this.content = ((ReceiptContent)contentInstance).getReceiptContent();
					}else{
						ReceiptContent receiptContent = new ReceiptContent();
						receiptContent.setDocId(this.fromMsgFlag);
						receiptContent.setReceiptContent(this.content);
						this.contentInstance = receiptContent;
					}
				}
				//附件集
				List<Node> attachNodes = body.selectNodes("报文体内容/附件");
				if(attachNodes!=null){
					for(Node attachNode : attachNodes){
						attachs.add(new Attach(attachNode));
					}
				}

			} catch (Exception e) {
				throw new Exception("error to load body["+body.asXML()+"]",e);
			}
		}
	}


	public String asXML() {
		StringBuffer sb = new StringBuffer();
		sb.append(HEAD);
		sb.append("<报文传输信封>");
		sb.append("<报文头>");
			sb.append(pkgXml("报文标识",getMsgFlag()));
			sb.append(pkgXml("协议版本号",getProtocolVersion()));
			//报文发送方
			sb.append("<报文发送方>");
			sb.append(msgSender.asXML());
			sb.append("</报文发送方>");
			//报文接收方
			sb.append("<报文接收方>");
			sb.append(msgReceiver.asXML());
			sb.append("</报文接收方>");
			sb.append(pkgXml("保护类型",getProtectType()));
			sb.append(pkgXml("报文属性",getMsgAttr()));
			sb.append(pkgXml("发出时间",com.hnjz.util.DateUtil.getDateStr(getSendTime(), "yyyy-MM-dd HH:mm:ss")));
			//密码算法
			if(encryptionConf!=null && encryptionConf.getSecretLevel()!=null
					&& encryptionConf.getSignatureArithmetic()!=null
					&& encryptionConf.getSecretLevel()!=null ){
				sb.append("<密码算法>");
				sb.append(encryptionConf.asXML());
				sb.append("</密码算法>");
			}


			sb.append(pkgXml("报文备注",getMsgRemark()));
			sb.append(pkgXml("传输票据",getTransToken()));
		sb.append("</报文头>");
		sb.append("<报文体>");
			sb.append("<报文体内容>");
				//<![CDATA[  ]]>
				sb.append("<内容>");
				sb.append("<公文信封>");
					sb.append(pkgXml("报文标识",getFromMsgFlag()));
					if(GW.equals(this.msgAttr)){
						if(getContentInstance()!=null){
							setContent(getContentInstance().asXML());
						}
						sb.append(pkgXml("公文",getContent()));
					}else if(RECEIPT.equals(this.msgAttr)){
						if(getContentInstance()!=null){
							if(getContent()==null || "".equals(getContent())){
								setContent(((ReceiptContent)contentInstance).getReceiptContent());
							}
							sb.append(pkgXml("回执",getContentInstance().asXML()));
						}
						sb.append(pkgXml("短报文内容",getContent()));
					}
				sb.append("</公文信封>");
				sb.append("</内容>");
				for(Attach attach : getAttachs()){
					sb.append(pkgXml("附件",attach.asXML()));
				}
			sb.append("</报文体内容>");
			if(signature!=null && signature.getSignatureBody()!=null){
				sb.append("<签名>");
				sb.append(signature.asXML());
				sb.append("</签名>");
			}
		sb.append("</报文体>");
		sb.append("</报文传输信封>");
		return sb.toString();
	}

	public String getMsgFlag() {
		return msgFlag;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public String getProtectType() {
		return protectType;
	}

	public String getMsgAttr() {
		return msgAttr;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public String getMsgRemark() {
		return msgRemark;
	}

	public String getTransToken() {
		return transToken;
	}

	public Identity getMsgSender() {
		return msgSender;
	}

	public Identity getMsgReceiver() {
		return msgReceiver;
	}

	public EncryptionConf getEncryptionConf() {
		return encryptionConf;
	}

	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public void setProtectType(String protectType) {
		this.protectType = protectType;
	}

	public void setMsgAttr(String msgAttr) {
		this.msgAttr = msgAttr;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public void setMsgRemark(String msgRemark) {
		this.msgRemark = msgRemark;
	}

	public void setTransToken(String transToken) {
		this.transToken = transToken;
	}

	public void setMsgSender(Identity msgSender) {
		this.msgSender = msgSender;
	}

	public void setMsgReceiver(Identity msgReceiver) {
		this.msgReceiver = msgReceiver;
	}

	public void setEncryptionConf(EncryptionConf encryptionConf) {
		this.encryptionConf = encryptionConf;
	}

	public String getFromMsgFlag() {
		return fromMsgFlag;
	}

	public void setFromMsgFlag(String fromMsgFlag) {
		this.fromMsgFlag = fromMsgFlag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Attach> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<Attach> attachs) {
		this.attachs = attachs;
	}

	public Signature getSignature() {
		return signature;
	}
	
	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public PkgData getContentInstance() {
		return contentInstance;
	}

	public void setContentInstance(PkgData contentInstance) {
		this.contentInstance = contentInstance;
	}

}
