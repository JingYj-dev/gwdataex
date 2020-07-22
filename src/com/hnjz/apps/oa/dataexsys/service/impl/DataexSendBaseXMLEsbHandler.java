package com.hnjz.apps.oa.dataexsys.service.impl;

import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.service.impl.dataex.DataexGwProcessor;
import com.hnjz.apps.oa.dataexsys.service.impl.dataex.DataexProcessor;
import com.hnjz.apps.oa.dataexsys.service.impl.dataex.DataexReceiptProcessor;
import com.hnjz.apps.oa.dataexsys.service.SendBaseXMLEsbHandler;
import com.hnjz.apps.oa.dataexsys.util.Ajax;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataexSendBaseXMLEsbHandler extends SendBaseXMLEsbHandler {
	private Map<String,DataexProcessor> processors = new LinkedHashMap<String,DataexProcessor>();

	@Override
	public String process(Object xml, ClientInfo clientInfo) {
		for (DataexProcessor process : processors.values()) {
			String result = process.process(xml, clientInfo);
			if (result != null) {
				return result;
			}
		}
		return Ajax.JSONResult(Constants.FAILURE,"current handler can not process request!");
	}
	
	public DataexProcessor getProcessor(Class<? extends DataexProcessor> clazz){
		return processors.get(clazz.getName());
	}

	public DataexSendBaseXMLEsbHandler register(DataexProcessor process) {
		processors.put(process.getClass().getName(),process);
		return this;
	}

	public DataexSendBaseXMLEsbHandler remove(DataexProcessor process) {
		processors.remove(process);
		return this;
	}

	protected void init() {
		this.register(new DataexGwProcessor())
			.register(new DataexReceiptProcessor())
			;
	}
	
	

	private static DataexSendBaseXMLEsbHandler instance = null;
	private DataexSendBaseXMLEsbHandler() {
	}
	public static DataexSendBaseXMLEsbHandler getInstance() {
		if (instance == null) {
			instance = new DataexSendBaseXMLEsbHandler();
			instance.init();
		}
		return instance;
	}

}
