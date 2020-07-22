package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.service.AuditLogExportService;
import com.hnjz.db.page.Page;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.webwork.ServletActionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file LogExport.java creation date: [Jay 21, 2014 9:30:27 PM] by shiwl
 * http://www.css.com.cn
 */

public class AuditLogExport extends AdminAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(OpLogExport.class);
	
	private String operName;
	private Integer operType;
	private String opObjType;
	private Date beginDate;
	private Date endDate;
	private String funcId;
	private Integer eventType;
	private Page page;
	
	String fileName = "";
	String contentType = "";
	InputStream inputStream = null;
	String downloadFileName = "";
	
	public AuditLogExport(){
		page = new Page();
		page.setCountField("a.logId");
		setContentType("application/octet-stream;charset=utf-8");
		fileName = "auditLogExport.xls";
	}

	@Override
	protected String adminGo() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			this.inputStream = AuditLogExportService.exportExcel(page, funcId, operName, operType, 
									      opObjType,eventType, beginDate, endDate, this.getFileRealPath() + fileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			setMessage(Messages.getString("systemMsg.failed"));
			this.inputStream = new ByteArrayInputStream("文件不存在".getBytes());
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	public String getFileRealPath() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String filePath = request.getSession().getServletContext().getRealPath(File.separator) + "OpLogExport" + File.separator;
		File pathFile = new File(filePath);
		if (!pathFile.exists()) {
			pathFile.mkdir();
		}
		return filePath;
	}
	

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public String getOpObjType() {
		return opObjType;
	}

	public void setOpObjType(String opObjType) {
		this.opObjType = opObjType;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getFuncid() {
		return funcid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDownloadFileName() {
		try {                 
			downloadFileName = new String(fileName.getBytes(), "utf8");             
		} catch (UnsupportedEncodingException e) {                 
				e.printStackTrace();             
		}             
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}
	

}