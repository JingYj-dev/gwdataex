package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class DataexSysTask extends AbstractTask {
	
	private Map<String, Object> taskMap;
	
	public DataexSysTask(Map<String, Object> taskMap) {
		this.taskMap = taskMap;
	}
	
	private static final Log _logger = LogFactory.getLog(DataexSysTask.class);
	
	@Override
	protected void afterExecute() {
		if (_logger.isInfoEnabled()) {
			_logger.info("afterExecute()");
		}
	}
	@Override
	protected void beforeExecute() {
		if (_logger.isInfoEnabled()) {
			_logger.info("beforeExecute()");
		}
	}
	
	@Override
	public String run() {
		String ret = Constants.OP_FAILURE;
		try {
			ret = DataexSysTaskHelper.getDataexSysTaskHelper(taskMap).process();
		} catch (Exception e) {
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[method:process() exception. ]":e.getMessage(), e);
			}
		}
		return ret;
	}
	
	public static AbstractTask getAbstractTask(Map<String, Object> taskMap) {
		return new DataexSysTask(taskMap);
	}

}
