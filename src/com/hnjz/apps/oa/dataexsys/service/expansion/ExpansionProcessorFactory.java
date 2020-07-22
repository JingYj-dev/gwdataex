package com.hnjz.apps.oa.dataexsys.service.expansion;

import com.hnjz.apps.oa.dataexsys.util.DataexsysConfiguration;

import java.lang.reflect.Constructor;

public class ExpansionProcessorFactory {
	
	public static IExpansionProcessor getProcessor(){
		try {
			String className = DataexsysConfiguration.getExpansionProcessor();
//			String className = "com.hnjz.apps.oa.dataexsys.service.expansion.OtherExpansionProcessor";
			Class<?> clazz = Class.forName(className);
	        Constructor<?> cons=clazz.getConstructor(new Class[]{});
			return (IExpansionProcessor) cons.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new OtherExpansionProcessor();
	}
	
}
