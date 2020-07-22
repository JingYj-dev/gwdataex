package com.hnjz.apps.oa.dataexsys.service.expansion;

import com.hnjz.apps.oa.dataexsys.pkg.model.Expansion;
import com.hnjz.apps.oa.dataexsys.util.DataexsysConfiguration;

import java.lang.reflect.Constructor;

public class ExpansionFactory {
	
	public static Expansion getExpansion(String xml){
		try {
			String className = DataexsysConfiguration.getExpansionClass();
			Class<?> clazz = Class.forName(className);
	        Constructor<?> cons=clazz.getConstructor(new Class[]{xml.getClass()});
			return (Expansion) cons.newInstance(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Expansion();
	}
	public static Expansion getExpansion(Object xml){
		try {
			String className = DataexsysConfiguration.getExpansionClass();
			Class<?> clazz = Class.forName(className);
	        Constructor<?> cons=clazz.getConstructor(xml.getClass());
			return (Expansion) cons.newInstance(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Expansion();
	}
	
	public static void main(String[] args) {
		Expansion exp = getExpansion("<自定义属性><发件标识>1</发件标识></自定义属性>");
		System.out.println();
	}
}
