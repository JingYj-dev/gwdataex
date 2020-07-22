package com.hnjz.apps.base.log.model;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.QueryDict;
import com.hnjz.util.Md5Util;
import com.hnjz.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;

public class LogDict {

	private static Log log = LogFactory.getLog(LogDict.class);
	
	public static String getDictType(String table, String dictId) {
		List listIds = StringHelper.strToList(dictId);
		Iterator<String> iterStr = listIds.iterator();
		String names = "";
		while(iterStr.hasNext()){
			String dictIdStr = iterStr.next().trim();
			names += QueryDict.get(SDict.class, Md5Util.MD5Encode(table + dictIdStr)).getName() + ",";
		}
		if (names.length() > 0 ) names = names.substring(0, names.length()-1);
		return names;
	}
}
