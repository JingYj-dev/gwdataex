package com.hnjz.apps.oa.dataexsys.pkg.model;

import com.hnjz.db.util.StringHelper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultExpansion extends Expansion {

	protected String outboxId;
	/**
	 * key:receiverFlag
	 * value:
	 * 		receiveFlag
	 * 		receiverName
	 * 		startNum
	 * 		endNum
	 */
	protected Map<String,Map<String,String>> nums = new HashMap<String,Map<String,String>>();

	public DefaultExpansion(){}
	public DefaultExpansion(String xml) throws Exception{
		super(xml);
		init();
	}
	public DefaultExpansion(Object o) throws Exception{
		super(o);
		init();
	}
	public DefaultExpansion addNum(Identity receiver, String startNum, String endNum) throws Exception{
		if(receiver==null)return this;
		Map<String,String> num = new HashMap<String,String>();
		num.put("receiveFlag", receiver.getIdentityFlag());
		num.put("receiverName", receiver.getIdentityName());
		num.put("startNum", startNum);
		num.put("endNum", endNum);
		nums.put(receiver.getIdentityFlag(), num);
		return this;
	}
	
	public String asXML() {
		StringBuffer sb = new StringBuffer();
		sb.append(pkgXml("发件标识",getOutboxId()));
		for(Map<String,String> num : getNums().values()){
			if(StringHelper.isNotEmpty(num.get("startNum"),num.get("endNum"))){
				sb.append("<份号>");
				sb.append(pkgXml("机关标识",num.get("receiveFlag")));
				sb.append(pkgXml("机关名称",num.get("receiverName")));
				sb.append(pkgXml("起始份号",num.get("startNum")));
				sb.append(pkgXml("终止份号",num.get("endNum")));
				sb.append("</份号>");
			}
		}
		return sb.toString();
	}
	
	public String asXML(String receiverFlag) {
		StringBuffer sb = new StringBuffer();
		sb.append(pkgXml("发件标识",getOutboxId()));
		Map<String,String> num = getNums().get(receiverFlag);
		if(num!=null){
			sb.append("<份号>");
			sb.append(pkgXml("机关标识",num.get("receiveFlag")));
			sb.append(pkgXml("机关名称",num.get("receiverName")));
			sb.append(pkgXml("起始份号",num.get("startNum")));
			sb.append(pkgXml("终止份号",num.get("endNum")));
			sb.append("</份号>");
		}
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
				this.outboxId = getStringValue(node, "发件标识");
				List<Node> numNodes = node.selectNodes("份号");
				if(numNodes!=null){
					for(Node numNode : numNodes){
						Map<String,String> num = new HashMap<String,String>();
						num.put("receiveFlag", getStringValue(numNode, "机关标识"));
						num.put("receiverName", getStringValue(numNode, "机关名称"));
						num.put("startNum", getStringValue(numNode, "起始份号"));
						num.put("endNum", getStringValue(numNode, "终止份号"));
						
						nums.put(getStringValue(numNode, "机关标识"),num);
					}
				}
			} catch (Exception e) {
				throw new Exception("error to load numexpansion:["+node.asXML()+"]",e);
			}
		}
	}
	
	
	public String getOutboxId() {
		if(outboxId==null)return "";
		return outboxId;
	}
	public void setOutboxId(String outboxId) {
		this.outboxId = outboxId;
	}
	public Map<String, Map<String, String>> getNums() {
		return nums;
	}
	public void setNums(Map<String, Map<String, String>> nums) {
		this.nums = nums;
	}
}
