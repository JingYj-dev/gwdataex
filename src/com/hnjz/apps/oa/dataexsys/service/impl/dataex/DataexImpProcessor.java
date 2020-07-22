package com.hnjz.apps.oa.dataexsys.service.impl.dataex;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysAttachment;
import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.pkg.GWPackagesUtil;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.FileUtil;
import com.hnjz.apps.oa.dataexsys.util.Ajax;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

public class DataexImpProcessor extends DataexGwProcessor {
	private static final Log _logger = LogFactory.getLog(DataexImpProcessor.class);

	@Override
	public String process(Object xml, ClientInfo clientInfo) {
		if(xml!=null && xml instanceof String){
			String uuid = xml.toString();
			//根据Trans表uuid获取交换原始报文
	    	DataexSysAttachment attachment = QueryCache.get(DataexSysAttachment.class, uuid);
	    	if(attachment==null){return null;}
	    	Integer nServerId = Integer.parseInt(attachment.getServerId());
			String filepath = AttachItem.filePath(nServerId);
			filepath += attachment.getPath();
			Document doc = null;
			try {
				String request = new String(FileUtil.getBytesFromFile(filepath), "UTF-8");
				doc = GWPackagesUtil.parsePackage(request);
			} catch (Exception e) {
				String msg = e.getMessage();
				_logger.error(msg);
				return Ajax.JSONResult(Constants.FAILURE, msg);
			}
			return super.process(doc, clientInfo);
		}
		return null;
	}

}
