package com.hnjz.apps.oa.accept.model;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;

import java.lang.reflect.InvocationTargetException;

public class DataexSysTransContentVo extends DataexSysTransContent {
	public void init(DataexSysTransContent content){
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(this, content);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private String sendId;
	private String startNum;
	private String endNum;

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getStartNum() {
		return startNum;
	}

	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}

	public String getEndNum() {
		return endNum;
	}

	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}
	
	

}
