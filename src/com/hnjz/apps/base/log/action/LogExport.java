package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.service.LogExportService;
import com.hnjz.db.page.Page;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.user.action.GetUserSettings;
import com.hnjz.util.Messages;
import com.opensymphony.webwork.ServletActionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file LogExport.java creation date: [Jun 13, 2014 1:54:27 PM] by mazhh
 * http://www.css.com.cn
 */

public class LogExport extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetUserSettings.class);
	private final String funcid = "ACL_DIRLOGEXT";
	
	private String opName; //操作人姓名
	private String opType; //操作类型
	private Date opTime; //操作时间
	private String logLevel; //日志级别
	private String logType; //日志类型:业务 管理 安全
	private String opResult; //操作结果
	private Date beginDate;
	private Date endDate;
	private String funcId;
	private Page page;
	
	String fileName = "";
	String contentType = "";
	InputStream inputStream = null;
	String downloadFileName = "";
	
	public LogExport(){
		page = new Page();
		page.setCountField("a.logId");
		setFuncid(funcid);
		setContentType("application/octet-stream;charset=utf-8");
		fileName = "logExport.xls";
	}
	
	@Override
	protected String adminGo() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			this.inputStream = LogExportService.exportExcel(page, funcId, opName, opType, 
									      logLevel, logType, beginDate, endDate, this.getFileRealPath() + fileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			setMessage(Messages.getString("systemMsg.failed"));
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 得到用户自己指定的要保存的文件名
	 * @return String
	 */
	public String getDownloadFileName () {             
		try {                 
			downloadFileName = new String(fileName.getBytes(), "utf8");             
		} catch (UnsupportedEncodingException e) {                 
				e.printStackTrace();             
		}             
		return downloadFileName;         
	}     
	
	/**
	 * 得到文件所在文件夹的绝对路径
	 * @return String
	 */
	public String getFileRealPath() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String filePath = request.getSession().getServletContext().getRealPath(File.separator) + "LogExport" + File.separator;
		File pathFile = new File(filePath);
		if (!pathFile.exists()) {
			pathFile.mkdir();
		}
		return filePath;
	}
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
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

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getOpResult() {
		return opResult;
	}

	public void setOpResult(String opResult) {
		this.opResult = opResult;
	}

	public String getFuncid() {
		return funcid;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
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

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
}
