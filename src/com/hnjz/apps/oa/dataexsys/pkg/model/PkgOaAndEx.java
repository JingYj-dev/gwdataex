package com.hnjz.apps.oa.dataexsys.pkg.model;

import org.dom4j.Document;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.List;

public class PkgOaAndEx extends PkgData {
	private Identity sender = new Identity();
	private List<Identity> receivers = new ArrayList<Identity>();
	private String fromMsgFlag;
	private String content;
	private List<Attach> attachs = new ArrayList<Attach>();
	private String remark;

	//内容的实例:公文、回执
	private PkgData contentInstance;

	public PkgOaAndEx(){}
	public PkgOaAndEx(Node node) throws Exception {
		load(node);
	}
	public PkgOaAndEx(String xml) throws Exception {
		Node node = xmlToNode(xml);
		load(node);
	}


	/**
	 * 加载node
	 * @param node node为/公文封装包/封装实体  或者document
	 * @throws Exception
	 */
	public void load(Object obj) throws Exception{
		//如果node是document,即含有"/公文封装包/封装实体"
		if(obj != null){
			Node node = null;
			if(obj instanceof Document){
				node = ((Document)obj).getRootElement();
			}else{
				node = (Node)obj;
			}
			node = (Node) node.selectSingleNode("/公文封装包/封装实体");
			if(node==null){
				throw new Exception("[公文封装包/封装实体] not define in "+node.asXML());
			}
			//错误描述
			String msg = "";
			try {
				Node senderNode = node.selectSingleNode("发文机关");
				if(senderNode==null){
					throw new Exception("[公文封装包/封装实体/发文机关] not define in "+node.asXML());
				}
				this.sender.load(senderNode);
				List<Node> receiverNodes = node.selectNodes("收文机关");
				if(receiverNodes==null || receiverNodes.size()==0){
					throw new Exception("[公文封装包/封装实体/收文机关] not define in "+node.asXML());
				}
				if(receiverNodes!=null){
					for(Node receiverNode : receiverNodes){
						receivers.add(new Identity(receiverNode));
					}
				}
				//公文信息或者回执信息
				Node gwNode = selectSingleNode(node,"公文信息");
				if(gwNode!=null){
					this.content = gwNode.asXML();
					this.contentInstance = new GwContent(gwNode);
				}else{
					this.fromMsgFlag = getStringValue(node, "报文标识");
					Node expNode = selectSingleNode(node, "短报文内容",true);
					if(expNode.getStringValue().equals(expNode.getText())){
						this.content = expNode.getText();
					}else{
						this.content = expNode.asXML();
					}
					Node receiptNode = selectSingleNode(node,"回执信息");
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
				List<Node> attachNodes = node.selectNodes("附件集/附件");
				if(attachNodes!=null){
					msg = "";
					for(Node attachNode : attachNodes){
						attachs.add(new Attach(attachNode));
					}
				}
				//备注
				this.remark = getStringValue(node, "备注");
			} catch (Exception e) {
				throw new Exception(msg,e);
			}
		}
	}

	public String asXML(){
		StringBuffer sb = new StringBuffer("");
		sb.append(HEAD);
		sb.append("<公文封装包>");
		sb.append("<封装实体>");
			sb.append("<发文机关>"+getSender().asXML()+"</发文机关>");
			for(Identity receiver : getReceivers()){
				sb.append("<收文机关>"+receiver.asXML()+"</收文机关>");
			}
			if(getContentInstance()!=null){
				if(getContentInstance() instanceof GwContent){
					sb.append(pkgXml("公文信息",getContentInstance().asXML()));
				} else if(getContentInstance() instanceof ReceiptContent){
					sb.append(pkgXml("回执信息",getContentInstance().asXML()));
					sb.append(pkgXml("报文标识",getFromMsgFlag()));
					ReceiptContent receiptContent = (ReceiptContent)getContentInstance();
					sb.append(pkgXml("短报文内容",receiptContent.getReceiptContent()));
				}
			}else{
				sb.append(pkgXml("公文信息",getContent()));
				sb.append(pkgXml("报文标识",getFromMsgFlag()));
				sb.append(pkgXml("短报文内容",getContent()));
			}
			if(getAttachs()!=null && getAttachs().size()>0){
				sb.append("<附件集>");
				for(Attach attach : getAttachs()){
					sb.append(pkgXml("附件",attach.asXML()));
				}
				sb.append("</附件集>");
			}
			sb.append(pkgXml("备注",getRemark()));
			sb.append("</封装实体>");
		sb.append("</公文封装包>");
		return sb.toString();
	}

	public Identity getSender() {
		return sender;
	}
	public void setSender(Identity sender) {
		this.sender = sender;
	}
	public List<Identity> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<Identity> receivers) {
		this.receivers = receivers;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public PkgData getContentInstance() {
		return contentInstance;
	}
	public void setContentInstance(PkgData contentInstance) {
		this.contentInstance = contentInstance;
	}
	
}
