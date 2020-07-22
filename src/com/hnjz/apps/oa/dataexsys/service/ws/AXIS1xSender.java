/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file AXIS2Sender.java creation date: [2014-7-2 下午02:27:09] by CSS
 * http://www.css.com.cn
 * 
 */

package com.hnjz.apps.oa.dataexsys.service.ws;

import com.hnjz.apps.oa.dataexsys.service.ISender;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;

/*import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;*/

/** 
 * <description>function information</descption>
 * 
 * @author CSS
 * @version $Id$
 */
public class AXIS1xSender implements ISender {
	private static final Log _log = LogFactory.getLog(AXIS1xSender.class);
	// 指定访问web服务的位置
	private String endpoint = "http://127.0.0.1:8080/gt3aipservice/gt3aipservice";
//	private String endpoint;
//	private String targetNameSpace = "http://service.dataex.oa.apps.css.com";
//	private String operationName = "receive";
	private String operationName = "";
	private Service service;
	private AXIS1xSender() throws AxisFault {
		// 创建服务
		service = new Service();
	}
	
	public AXIS1xSender(String endpoint, String operationName) throws AxisFault {
		this();
		this.endpoint = endpoint;
		this.operationName = operationName;
	}
	public String send(String xmlContent) throws Exception {
		// 创建调用
		Call call = (Call) service.createCall();
		// 设置调用服务来源
		call.setTargetEndpointAddress(endpoint);
		// 设置调用方法名
 		call.setOperationName(new QName(endpoint, operationName));
		// 调用
	 	Object ret = call.invoke(new Object[]{ xmlContent });
	 	if (_log.isDebugEnabled()) {
		 	_log.debug(ret);
	 	}
		return (String) ret;
	}
	public String request(Object[] objs) throws Exception {
		// 创建调用
		Call call = (Call) service.createCall();
		// 设置调用服务来源
		call.setTargetEndpointAddress(endpoint);
		// 设置调用方法名
 		call.setOperationName(new QName(endpoint, operationName));
		// 调用
	 	Object ret = call.invoke(objs);//new Object[]{ xmlContent }
	 	if (_log.isDebugEnabled()) {
		 	_log.debug(ret);
	 	}
		return (String) ret;
	}
	
	
	/*private RPCServiceClient serviceClient;
	
	public AXIS2Sender() throws AxisFault {
	//  使用RPC方式调用WebService          
		serviceClient = new RPCServiceClient();  
		Options options = serviceClient.getOptions();  
		//  指定调用WebService的URL  
		EndpointReference targetEPR = new EndpointReference(endpoint);  
		options.setTo(targetEPR);
		String action = operationName;
		options.setAction(action);
	}

	 (non-Javadoc)
	 * @see com.cxf.study.ISender#send(java.lang.String)
	 
	public String send(String xmlContent) throws Exception {
	
		Object[] requestParam = new Object[] { xmlContent };
		Class<?>[] responseParam = new Class[] { String.class };
		QName requestMethod = new QName(targetNameSpace, operationName);
		Object[] responseValue = serviceClient.invokeBlocking(requestMethod, requestParam, responseParam);
		return (String) responseValue[0];
	}
	
	public static void main(String[] args) {
		try {
			ISender sender = new AXIS2Sender();
			String xmlContent = "测试数据abcdef";
			sender.send(xmlContent);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

}
