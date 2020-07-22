package com.hnjz.apps.base.log.service;

import com.hnjz.common.plugins.impl.AbstractPlugin;
import com.hnjz.core.model.ILog;
import com.hnjz.core.plugins.base.ILogProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 异步批量存储服务
 * @author paladin
 */
@SuppressWarnings("all")
public class AsynLogService extends AbstractPlugin implements ILogProvider {
	private static Log log = LogFactory.getLog(AsynLogService.class);
	public void log(final ILog slog){
			 throw new RuntimeException("尚未实现");
	}
  
}
