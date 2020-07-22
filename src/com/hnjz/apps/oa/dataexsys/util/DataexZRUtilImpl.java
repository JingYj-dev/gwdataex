package com.hnjz.apps.oa.dataexsys.util;

import com.caucho.hessian.server.HessianServlet;

public class DataexZRUtilImpl extends HessianServlet implements DataexZRUtil {

	@Override
	public String getSignInfo(String xml, String sendId) {
		String resultStr = "";
		try {
			resultStr = SendFzUtil.sendXml(xml, sendId);
		} catch (Exception e) {
			return "";
		}
		return resultStr;
	}

	@Override
	public String getPrintInfo(String xml, String sendId,String type) {
		String resultStr = "";
		try {
			resultStr = SendFzUtil.sendPrintXml(xml, sendId, type);
		} catch (Exception e) {
			return "";
		}
		return resultStr;
	}

	

}
