
package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.base.common.upload.DownloadFile;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;

import java.io.File;

public class DownloadDirCert extends DownloadFile {
	private static final long serialVersionUID = 1L;
	private String uuid;
	public DownloadDirCert() {
	}
	public void setFileName() throws Exception{
		DataexSysNode exnode = QueryCache.get(DataexSysNode.class, uuid);
		if(exnode != null){
			fileName = exnode.getCertPath();
			if(StringHelper.isNotEmpty(fileName)) {
				downloadFileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());
			}
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
