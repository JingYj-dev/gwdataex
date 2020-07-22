package com.hnjz.apps.base.log.service;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.common.plugins.IDisposable;
import com.hnjz.common.plugins.Initializable;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;

import java.util.Map;


public class ServerEventLogger extends AbstractConfigurablePlugin implements
		Initializable, IDisposable {

	@Override
	protected void doConfig(Map<String, String> arg0) {

	}

	@Override
	public void initialize() {
	     new LogPart()
	     .setLogData("{action:'server starting!'}")
	     .setLogLevel(LogConstant.LOG_LEVEL_COMMON)
	     .setMemo("server starting at " + com.hnjz.util.DateTimeUtil.getDateTimeString())
	     .setOpType(LogConstant.LOG_TYPE_OTHERS)
	     .setResult("success").save();
	}

	@Override
	public void dispose() {
		new LogPart()
	     .setLogData("{action:'server stopping!'}")
	     .setLogLevel(LogConstant.LOG_LEVEL_COMMON)
	     .setMemo("server stopping at " + com.hnjz.util.DateTimeUtil.getDateTimeString())
	      .setOpType(LogConstant.LOG_TYPE_OTHERS)
	     .setResult("success").save();
	}

}
