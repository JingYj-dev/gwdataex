package com.hnjz.apps.oa.dataexsys.pkg.model;

import com.hnjz.util.DateUtil;
import org.dom4j.Node;

import java.util.Date;

public class ReceiptContent extends PkgData {
	
	private String docId;
	private String receiptContent;
	private Date receiptTime;
	private String receiptMemo;
	//扩展属性
	private String exp;
	//自定义属性
	private Expansion expansion = new Expansion(null);
	
	public ReceiptContent(){}
	public ReceiptContent(Node node) throws Exception {
		load(node);
	}
	public ReceiptContent(String xml) throws Exception {
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
			try {
				this.docId = getStringValue(node, "公文标识", true);
				this.receiptContent = getStringValue(node, "回执内容", true);
				this.receiptMemo = getStringValue(node, "回执意见", false);
				this.receiptTime = DateUtil.getDate(getStringValue(node, "回执时间", true), "yyyy-MM-dd HH:mm:ss");
				this.exp = getStringValue(node, "扩展属性");
				Node expNode = selectSingleNode(node, "自定义属性");
				if(expNode!=null){
					if(expNode.getStringValue().equals(expNode.getText())){
						this.expansion.load(expNode.getText());
					}else{
						this.expansion.load(expNode);
					}
				}
			} catch (Exception e) {
				throw new Exception("error to load hzinfo:["+node.asXML()+"]",e);
			}
		}
	}
	
	public String asXML() {
		StringBuffer sb = new StringBuffer();
		sb.append(pkgXml("公文标识",getDocId()));
		sb.append(pkgXml("回执内容",getReceiptContent()));
		sb.append(pkgXml("回执意见",getReceiptMemo()));
		sb.append(pkgXml("回执时间",DateUtil.getDateStr(getReceiptTime(), "yyyy-MM-dd HH:mm:ss")));
		sb.append(pkgXml("扩展属性",getExp()));
		sb.append(pkgXml("自定义属性",getExpansion().asXML()));
		return sb.toString();
	}
	
	
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getReceiptContent() {
		return receiptContent;
	}
	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}
	public Date getReceiptTime() {
		return receiptTime;
	}
	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}
	public String getReceiptMemo() {
		return receiptMemo;
	}
	public void setReceiptMemo(String receiptMemo) {
		this.receiptMemo = receiptMemo;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public Expansion getExpansion() {
		return expansion;
	}
	public void setExpansion(Expansion expansion) {
		this.expansion = expansion;
	}

}
