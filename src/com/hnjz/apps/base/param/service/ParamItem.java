/*
 * Created on 2006-7-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hnjz.apps.base.param.service;

import com.hnjz.apps.base.param.model.SParam;
import com.hnjz.db.query.QueryCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Administrator TODO To change the template for this generated Attach
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class ParamItem {
	private static Log log = LogFactory.getLog(ParamItem.class);
	public static SParam get(String id) {
		return QueryCache.get(SParam.class, id);
	}
	
}
