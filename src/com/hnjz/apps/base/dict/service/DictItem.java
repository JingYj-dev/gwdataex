package com.hnjz.apps.base.dict.service;

import com.hnjz.db.query.QueryCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DictItem {
	private static Log log = LogFactory.getLog(DictItem.class);
	public static List<String> getSDictByParentId(String parentId){
			QueryCache qc = new QueryDict("select a.uuid from SDict a where a.parentId=:parentId").setParameter("parentId", parentId);
			List<String> li = qc.list();
			return li;
	}
}
