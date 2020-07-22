package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.service.LogStatExportService;
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

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file LogExport.java creation date: [7 21, 2014 3:30:27 PM] by shiwl
 * http://www.css.com.cn
 */

public class LogStatExport extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(LogStatExport.class);

	private String beginDate;
	private String endDate;
	
	String fileName = "";
	String contentType = "";
	InputStream inputStream = null;
	String downloadFileName = "";
	
	public LogStatExport(){
		setContentType("application/octet-stream;charset=utf-8");
		fileName = "logStatExport.xls";
	}
	
	@Override
	protected String adminGo() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			this.inputStream = LogStatExportService.exportExcel(beginDate, endDate, this.getFileRealPath() + fileName);
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

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	
	
	

}
