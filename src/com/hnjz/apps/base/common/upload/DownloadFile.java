package com.hnjz.apps.base.common.upload;

import com.hnjz.core.model.IServiceResult;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * <action name="downloadTemplateFile" class="com.hnjz.apps.information.template.action.DownloadTemplateFile" caption="上传模板文件">
        <result name="success" type="stream">      
            <param name="contentType">application/octet-stream</param>      
            <param name="inputName">inputStream</param>      
            <param name="contentDisposition">attachment;filename="${downloadFileName}"</param>      
            <param name="bufferSize">4096</param>      
        </result>
       <result name="error" type="dispatcher">
			<param name="location">doError.action</param>
		</result>    
	</action>
 * @author  FangWQ
 * @version v0.1  
 * @since   2014-5-26 下午04:49:12
 */
public class DownloadFile extends AdminAction {
	private static Log log = LogFactory.getLog(DownloadFile.class);
	public String uuid;
	public String fileName;
	public String downloadFileName;
	public DownloadFile(){}
	
	/**
	 * 设置当前下载文件的绝对路径
	 * @override应该被重写
	 * @throws Exception
	 */
	protected void setFileName() throws Exception{
	}
	
	@Override
	protected String adminGo() {
		try {
			setFileName();
		} catch (Exception e) {
			String msg = e.getMessage();
			//log.error(msg,e);
			result = Ajax.xmlResult(IServiceResult.RESULT_ERROR, msg);
			fileName = "文件不存在.txt";
			return Action.SUCCESS;
		}
		return Action.SUCCESS;
	}
	
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	/**
	 * 获取文件名称
	 * @return
	 */
	public String getDownloadFileName () {
		if(StringHelper.isEmpty(downloadFileName)){
			downloadFileName = fileName.substring(fileName.lastIndexOf("/")+1);
		}
        try {     
        	HttpServletRequest request = ServletActionContext.getRequest();
        	if(StringHelper.isNotEmpty(request.getHeader("User-Agent"))){
				if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)
					downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");// firefox浏览器
				else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)
					downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");// IE浏览器
				else if (request.getHeader("User-Agent").toUpperCase().indexOf("Mozilla") > 0)
					downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");// chrome
				else
					downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");// other
			}
        } catch (UnsupportedEncodingException e) {     
        	//log.error(e);     
        }     
        return downloadFileName;     
    }
	/**
	 * 获取文件流
	 * @return
	 * @throws Exception
	 */
	public InputStream getInputStream () {     
        InputStream is = null;     
        try {
			is = new OnceFileInputStream(fileName);
		} catch (FileNotFoundException e) {
			try {
				is = new ByteArrayInputStream("文件不存在!".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				log.error("文件不存在!",e1);
			}
			//log.error("文件不存在!",e);
		}  
        return is;     
    }
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}


