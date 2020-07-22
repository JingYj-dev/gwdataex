package com.hnjz.apps.oa.dataexsys.listener;

import com.hnjz.apps.oa.dataexsys.service.thread.InitProducerComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DataexSysListenerProducer implements ServletContextListener {
	
	private static final Log _logger = LogFactory.getLog(DataexSysListenerProducer.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		InitProducerComponent.dispose();
		if (_logger.isInfoEnabled()) {
			_logger.info("DataexSysProduceDaemonWorker destroyed...");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			InitProducerComponent.init();
		} catch (Exception e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[thread Initialized Exception. ]":e.getMessage(), e);
			}
		}
		if (_logger.isInfoEnabled()) {
			_logger.info("DataexSysProduceDaemonWorker initialized...");
		}
	}

}
