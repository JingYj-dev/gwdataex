/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file AddSysAction.java creation date: [2014-1-8 09:30:20] by mazhh
 * http://www.css.com.cn
 */
package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.service.impl.DataexSendBaseXMLEsbHandler;
import com.hnjz.apps.oa.dataexsys.service.impl.dataex.DataexImpProcessor;
import com.hnjz.apps.oa.dataexsys.service.impl.dataex.DataexProcessor;
import com.hnjz.util.Messages;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;

/** 
 * <p>导入公文包</p>
 * 
 */
public class AddOfficialDocPackSys extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AddOfficialDocPackSys.class);
	private final String funcid = "OA_DATAEXSYS_ADDOFFICIALDOCPACKSYS";
	private String attachmentid;
	
	public AddOfficialDocPackSys() { 
		setFuncid(funcid);
	}
	
	protected String adminGo() {
		try {
			if(StringHelper.isEmpty(attachmentid)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;
			}
			ClientInfo clientInfo = new ClientInfo();
			clientInfo.setRequest(ServletActionContext.getRequest());
			clientInfo.setStartTime(Calendar.getInstance().getTime());
			DataexProcessor processor = DataexSendBaseXMLEsbHandler.getInstance().getProcessor(DataexImpProcessor.class);
			if(processor==null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, "no processor to do.");		
				return Action.ERROR;
			}
			String flag = processor.process(attachmentid, clientInfo);
			if(flag==null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, "no processor to do.");		
				return Action.ERROR;
			}
			flag = (String) JSONObject.fromObject(flag).get("result");
			if (Constants.SUCCESS.equals(flag)) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
				return Action.SUCCESS;
			} else {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.failed"));		
				return Action.ERROR;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));		
			return Action.ERROR;
		}
	}
	
	public String getAttachmentid() {
		return attachmentid;
	}

	public void setAttachmentid(String attachmentid) {
		this.attachmentid = attachmentid;
	}
}
