package com.hnjz.apps.oa.dataexsys.service;

import com.hnjz.apps.oa.dataexsys.service.ws.AXIS1xSender;
import org.apache.axis.AxisFault;

public class SenderProvider {
	public static ISender getSender(String endpoint, String operationName) throws AxisFault {
		return new AXIS1xSender(endpoint, operationName);
	}
}
