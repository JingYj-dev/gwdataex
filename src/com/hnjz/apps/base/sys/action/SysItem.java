/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file sysItem.java creation date: [2014-1-17 15:06:05] by jiadw
 * http://www.css.com.cn
 */

package com.hnjz.apps.base.sys.action;

import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;

import java.util.List;

/** 
 * <p>description</p>
 * 
 * @author jiadw
 * @version $Id: SysItem.java 261 2014-01-20 07:15:14Z xingzhc $
 */

public class SysItem {
	public static List<SSys> getSystems(){
		return QueryCache.idToObj(SSys.class, new QueryCache("select a.uuid from SSys a").listCache());
	}
	public static List<SSys> getOpenSystems(){
		return QueryCache.idToObj(SSys.class, new QueryCache("select a.uuid from SSys a where a.openFlag='1' ").listCache());
	}
	public static SSys getSystem(String sysId){
		SSys sys = null;
		if(StringHelper.isEmpty(sysId))
			sys = new SSys();
		else
			sys = QueryCache.get(SSys.class, sysId); 
		return sys;
	}
}
