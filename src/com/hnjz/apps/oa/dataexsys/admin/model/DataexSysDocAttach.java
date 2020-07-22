package com.hnjz.apps.oa.dataexsys.admin.model;


public class DataexSysDocAttach {
	
	// Fields
	private String uuid; //附件ID
	private String contentId; //内容ID
	private String docId; //公文ID
	private String fileName; //附件名称
	private Integer fileSize; //附件大小
	private String extName; //扩展名
	private String attachMar; //附件标识
	private String attachUrl; //附件地址
	private String serverId; //服务器ID
	private String orderBy; //附件排序字段
	
	// Constructors

	/** default constructor */
	public DataexSysDocAttach() {
	}

	/** minimal constructor */
	public DataexSysDocAttach(String uuid) {
		this.uuid = uuid;
	}

	// Property accessors
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public String getAttachMar() {
		return attachMar;
	}

	public void setAttachMar(String attachMar) {
		this.attachMar = attachMar;
	}

	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
