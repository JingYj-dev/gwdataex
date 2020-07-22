package com.hnjz.apps.base.log.service;

import java.text.NumberFormat;

public class TjNumberFormat {
	
	public static String format(Double num){
		NumberFormat nf =  NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(num);
	}
}
