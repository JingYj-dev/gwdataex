
package com.hnjz.apps.oa.accept.action;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.base.common.upload.DownloadFile;
import com.hnjz.db.query.QueryCache;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.common.Constants;

import java.io.File;

public class DownloadDocOfd extends DownloadFile {
	private static final long serialVersionUID = 1L;
	private String uuid;
	public DownloadDocOfd() {
	}
	public void setFileName() throws Exception{
		DataexSysTransContent content = QueryCache.get(DataexSysTransContent.class, uuid);
		if(content != null){
			String filePath =AttachItem.filePath(Integer.parseInt(Constants.RECV_SYS_DIR_ID));
			fileName = filePath + content.getDocAddress();
			downloadFileName = content.getDocTitle()+".ofd";
		}
		File file = new File(fileName);
		if(file == null || !file.exists() || !file.isFile()){
			String msg = "文件不存在!";
			downloadFileName = "文件不存在";  
			throw new Exception(msg);
		}
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
}
