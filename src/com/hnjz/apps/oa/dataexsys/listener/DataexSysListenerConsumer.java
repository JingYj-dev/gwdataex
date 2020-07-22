package com.hnjz.apps.oa.dataexsys.listener;

import com.hnjz.apps.oa.dataexsys.service.thread.DataexSysDaemonWorker;
import com.hnjz.apps.oa.dataexsys.service.thread.DataexSysGcWorkerThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DataexSysListenerConsumer implements ServletContextListener {
	
	private static final Log _logger = LogFactory.getLog(DataexSysListenerConsumer.class);

	private DataexSysDaemonWorker worker = new DataexSysDaemonWorker();
	private DataexSysGcWorkerThread gcWorkerThread = new DataexSysGcWorkerThread();
	
//	public Log getLog() {
//        return LogFactory.getLog(DataexSysListenerConsumer.class);
//    }

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		worker.dispose();
		gcWorkerThread.dispose();
		if (_logger.isInfoEnabled()) {
			_logger.info("DataexSysConsumerDaemonWorker destroyed...");
			_logger.info("GcWorkerThread destroyed...");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (_logger.isInfoEnabled()) {
			_logger.info("DataexSysConsumerDaemonWorker initialized...");
			_logger.info("GcWorkerThread initialized...");
		}
		try {
			worker.init();
			gcWorkerThread.init();
		} catch (Exception e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[thread Initialized Exception. ]":e.getMessage(), e);
			}
		}
	}

}
