package com.hnjz.apps.oa.dataexsys.service;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.db.query.QueryCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * 前置条件：同步交换目录
 * @author liyzh
 *
 */
public class SynchDataexDirService {
	
	private static final Log _logger = LogFactory.getLog(SynchDataexDirService.class);
	
	
	/**
	 * &lt;?xml version="1.0" encoding="UTF-8"?>
		&lt;dirs><br/>
			&lt;dir><br/>
				&lt;id>1&lt;/id><br/>
				&lt;pid>&lt;/pid><br/>
				&lt;code>1000&lt;/code><br/>
				&lt;name>最高机构&lt;/name><br/>
				&lt;status>1&lt;/status><br/>
			&lt;/dir><br/>
		&lt;/dirs>
	 * @Title: getDirService
	 * @Description: TODO(描述方法功能:得到交换目录接口)
	 * @return
	 * @throws Exception
	 */
	public String getDirService() throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<dirs>");
		
		List<String> dirIds = new QueryCache(" select a.uuid from DataexSysDir a ").list();
		List<DataexSysDir> dirs = QueryCache.idToObj(DataexSysDir.class, dirIds);
		if(dirs!=null && dirs.size()>0){
			for(DataexSysDir dir : dirs){
				sb.append("<dir>");
					sb.append("<id>"+dir.getUuid()+"</id>");
					sb.append("<pid>"+dir.getParentId()+"</pid>");
					sb.append("<code>"+dir.getDirOrg()+"</code>");
					sb.append("<name>"+dir.getDirName()+"</name>");
					sb.append("<status>"+dir.getDirStatus()+"</status>");
				sb.append("</dir>");
			}
		}
		sb.append("</dirs>");
		return sb.toString();
	}
	

}
