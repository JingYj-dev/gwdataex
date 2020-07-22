package com.hnjz.apps.oa.dataexsys.service.thread;


public abstract class AbstractTask {
	
	public AbstractTask() {}
	
	/**
	 * @Title: run
	 * @Description: TODO(描述方法功能:提供运行方法)
	 * @return
	 */
	protected abstract String run();
	
	public String execute() {
		beforeExecute();
		String ret = run();
		afterExecute();
		return ret;
	}

	/**
	 * @Title: beforeExecute
	 * @Description: TODO(描述方法功能:run方法运行前执行)
	 */
	protected void beforeExecute() { }
	
	/**
	 * @Title: afterExecute
	 * @Description: TODO(描述方法功能:run方法运行后执行)
	 */
	protected void afterExecute() { }

}
