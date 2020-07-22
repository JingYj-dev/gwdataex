package com.hnjz.apps.oa.dataexsys.listener;

import com.hnjz.apps.oa.dataexsys.service.thread.DaemonWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BootstrapWorkerListener implements ServletContextListener {
	
	private static final Log _logger = LogFactory.getLog(BootstrapWorkerListener.class);

	private DaemonWorker worker = new DaemonWorker();
	
	public Log getLog() {
        return LogFactory.getLog(BootstrapWorkerListener.class);
    }

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		worker.dispose();
		if (_logger.isInfoEnabled()) {
			_logger.info("DaemonWorker destroyed...");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			worker.init();
		} catch (Exception e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[thread Initialized Exception. ]":e.getMessage(), e);
			}
		}
		if (_logger.isInfoEnabled()) {
			_logger.info("DaemonWorker initialized...");
		}
	}

}
