package com.hnjz.apps.base.log.action;

import com.hnjz.apps.base.log.service.LogFxExportService;
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

public class LogFxExport extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(LogFxExport.class);
	private Integer fxDate;
	private String eventCodes;
	
	String fileName = "";
	String contentType = "";
	InputStream inputStream = null;
	String downloadFileName = "";
	
	public LogFxExport(){
		setContentType("application/octet-stream;charset=utf-8");
		fileName = "logFxExport.xls";
	}

	@Override
	protected String adminGo() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			this.inputStream = LogFxExportService.exportExcel(fxDate, eventCodes, this.getFileRealPath() + fileName);
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

	public Integer getFxDate() {
		return fxDate;
	}

	public void setFxDate(Integer fxDate) {
		this.fxDate = fxDate;
	}

	public String getEventCodes() {
		return eventCodes;
	}

	public void setEventCodes(String eventCodes) {
		this.eventCodes = eventCodes;
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
