package com.hnjz.apps.base.theme.service;

import com.hnjz.db.query.QueryCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


public class ThemeService {
	private static Log log = LogFactory.getLog(ThemeService.class);
	public static int getThemeCount() {
		List<String> list = new QueryCache("select a.uuid from Theme a where a.openFlag = 1").list();
		if( null == list){
			return 0;
		}else{
			return list.size();
		}
	}
}
