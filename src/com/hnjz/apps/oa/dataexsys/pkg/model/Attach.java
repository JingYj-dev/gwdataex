package com.hnjz.apps.oa.dataexsys.pkg.model;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.oa.dataexsys.util.Base64Util;
import org.dom4j.Node;

public class Attach extends PkgData {
	private String name;
	private String ext;
	private String flag;
	private String size;
	private String content;
	private String orderBy;
	
	public Attach(){}
	public Attach(Object obj) throws Exception{
		load(obj);
	}
	
	public void setContentPath(String filePath) throws Exception {
		this.content = Base64Util.encodeBase64File(filePath);
	}
	public void setContentPath(Integer serverId,String realPath) throws Exception {
		String filePath = AttachItem.filePath(serverId)+realPath;
		setContentPath(filePath);
	}
	
	public void load(Object obj) throws Exception{
		if(obj!=null && obj instanceof Node){
			Node node = (Node)obj;
			try {
				this.name =  getStringValue(node, "附件名称", true);
				this.ext =  getStringValue(node, "扩展名", true);
				this.flag =  getStringValue(node, "附件标识");
				this.size =  getStringValue(node, "附件大小");
				this.content =  getStringValue(node, "附件内容", true);
				this.orderBy = getStringValue(node, "附件排序");
			} catch (Exception e) {
				throw new Exception("error to load attach:["+node.asXML()+"]",e);
			}
		}
	}
	
	public String asXML() {
		StringBuffer sb = new StringBuffer("");
		//sb.append("<附件>");
			sb.append(pkgXml("附件名称",getName()));
			sb.append(pkgXml("扩展名",getExt()));
			sb.append(pkgXml("附件标识",getFlag()));
			sb.append(pkgXml("附件大小",getSize()));
			sb.append(pkgXml("附件内容",getContent()));
			sb.append(pkgXml("附件排序", getOrderBy()));
		//sb.append("</附件>");
		return sb.toString();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
