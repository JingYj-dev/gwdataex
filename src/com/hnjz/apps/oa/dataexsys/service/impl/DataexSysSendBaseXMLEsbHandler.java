package com.hnjz.apps.oa.dataexsys.service.impl;

import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.service.impl.dataexsys.DataexsysGwProcessor;
import com.hnjz.apps.oa.dataexsys.service.impl.dataexsys.DataexsysProcessor;
import com.hnjz.apps.oa.dataexsys.service.impl.dataexsys.DataexsysReceiptProcessor;
import com.hnjz.apps.oa.dataexsys.service.SendBaseXMLEsbHandler;
import com.hnjz.apps.oa.dataexsys.util.Ajax;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataexSysSendBaseXMLEsbHandler extends SendBaseXMLEsbHandler {
	private Map<String,DataexsysProcessor> processors = new LinkedHashMap<String,DataexsysProcessor>();
	
	@Override
	public String process(Object xml, ClientInfo clientInfo) {
		for (DataexsysProcessor process : processors.values()) {
			String result = process.process(xml, clientInfo);
			if (result != null) {
				return result;
			}
		}
		return Ajax.JSONResult(Constants.FAILURE,"current handler can not process request!");
	}
	
	public DataexsysProcessor getProcessor(Class<? extends DataexsysProcessor> clazz){
		return processors.get(clazz.getName());
	}

	public DataexSysSendBaseXMLEsbHandler register(DataexsysProcessor process) {
		processors.put(process.getClass().getName(),process);
		return this;
	}

	public DataexSysSendBaseXMLEsbHandler remove(DataexsysProcessor process) {
		processors.remove(process);
		return this;
	}

	protected void init() {
		this.register(new DataexsysGwProcessor())
			.register(new DataexsysReceiptProcessor())
			;
	}
	
	private static DataexSysSendBaseXMLEsbHandler instance = null;
	private DataexSysSendBaseXMLEsbHandler(){}
	public static DataexSysSendBaseXMLEsbHandler getInstance(){
		if(instance==null){
			instance = new DataexSysSendBaseXMLEsbHandler();
			instance.init();
		}
		return instance;
	}

}
