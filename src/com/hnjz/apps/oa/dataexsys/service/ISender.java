/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file ISender.java creation date: [2014-6-27 下午05:40:09] by CSS
 * http://www.css.com.cn
 * 
 */

package com.hnjz.apps.oa.dataexsys.service;

public interface ISender {
	
	/**
	* @Title: send
	* @Description: TODO(处理系统向交换系统发送数据)
	* @param xmlContent
	* @return
	* @throws Exception
	*/
	String send(String xmlContent) throws Exception;
	
	/**
	 * @Title: request
	 * @Description: TODO(描述方法功能:向交换系统请求握手方法)
	 * @param objs
	 * @return
	 * @throws Exception
	 */
	String request(Object[] objs) throws Exception;

}
