package com.hnjz.apps.oa.dataexsys.service;

import com.hnjz.apps.oa.dataexsys.common.ClientInfo;

public abstract class SendBaseXMLEsbHandler {
	/**
	 * 处理发送公文的接口
	 * @param xml
	 * @param clientInfo
	 * @return
	 */
	public abstract String process(Object xml, ClientInfo clientInfo);
}
