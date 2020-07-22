/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DictMan.java creation date: [Jan 13, 2014 15:21:03 PM] by liuzhb
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.dict.action;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictService;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.util.Ajax;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class AddDict extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(AddDict.class);
 
	private SDict dict = null;
	public Object getModel() {
		return dict;
	}
	public AddDict() {
		dict = new SDict();
	}
	public String adminGo() {
		IServiceResult<String> res=DictService.addDict(dict);
		result = Ajax.JSONResult(res.getResultCode(), res.getResultDesc());
		return res.toActionResult();
	}
 
	public SDict getDict() {
		return dict;
	}
	public void setDict(SDict dict) {
		this.dict = dict;
	}
}
