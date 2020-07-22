package com.hnjz.apps.oa.dataexsys.service;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDocAttach;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.db.query.QueryCache;

import java.util.List;

public class QueryService {
	public static List<DataexSysDocAttach> getAttacByTransContent(String transContentId){
		List<String> uuids = new QueryCache("select a.uuid from DataexSysDocAttach a where a.contentId =:contentId").setParameter("contentId", transContentId).list();
		List<DataexSysDocAttach> attachments = QueryCache.idToObj(DataexSysDocAttach.class, uuids);
		return attachments;
	}
	public static DataexSysTransContent getContentByTransId(String transId) {
    	String id = (String)new QueryCache(" select a.uuid from DataexSysTransContent a where a.transId =:transId ").
    		setParameter("transId", transId).setMaxResults(1).uniqueResult();
    	DataexSysTransContent dstc = QueryCache.get(DataexSysTransContent.class, id);
    	if (dstc != null) {
    		String docTitle = dstc.getDocTitle();
        	if (docTitle.contains("newLine")) {
        		dstc.setDocTitle(docTitle.replace("newLine", "<br/>"));
    		}
		}
    	return dstc;
    }
    
    public static DataexSysTransContent getContentByContentId(String contentId) {
    	DataexSysTransContent dstc = QueryCache.get(DataexSysTransContent.class, contentId);
    	return dstc;
    }
}
