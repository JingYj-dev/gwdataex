/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file AddSysAction.java creation date: [2014-1-8 09:30:20] by Xingzhc
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.sys.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.log.service.LogMan;
import com.hnjz.apps.base.sys.model.SSys;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * <p>添加系统</p>
 * 
 */
public class AddSys extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(AddSys.class);
	private SSys item;
	
	
	public AddSys() { 
		this.item = new SSys();
	}
	
	protected String adminGo() {
		LogPart lp=new LogPart();		
		lp.setOpObjType(SSys.class.getName());
		lp.setOpType(LogConstant.LOG_TYPE_ADD);
		lp.setRelObjId("");
		lp.setRelObjType("");
		lp.setLogData(Json.object2json(item));
		lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
		try {
			if(StringHelper.isEmpty(item.getSysId())||StringHelper.isEmpty(item.getName())||StringHelper.isEmpty(item.getOpenFlag())||StringHelper.isEmpty(item.getUrl())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				//lp.setResult(LogConstant.RESULT_ERROR);				
				return Action.ERROR;
			}
			
			Object uuid = new QueryCache("select a.uuid from SSys a where a.sysId = :sysId ")
				.setParameter("sysId",item.getSysId()).setMaxResults(1).uniqueResult();
			if(uuid != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("sysmgrMsg.idExist"));				 
				//lp.setResult(LogConstant.RESULT_ERROR);
				return Action.ERROR;
			}			
			TransactionCache tx = null;
			tx = new TransactionCache();
			tx.save(item);
			
			tx.commit();			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.getUuid());
				lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.setOpObjId(item.getUuid());
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));	
			lp.setOpObjId(item.getUuid());
			lp.setResult(LogConstant.RESULT_ERROR);
			return Action.ERROR;
		}finally{			
			LogMan.addLogPart(lp);		
		}
	}
	
	public SSys getItem() {
		return item;
	}

	public void setItem(SSys item) {
		this.item = item;
	}

	public Object getModel() {
		return item;
	}
	public static void main(String[] args) {
		AddSys as = new AddSys();
		SUser g = new SUser();
		g.setUuid("1dd6d1b6f4624e4ce830408415681576");
		g.setLoginName("xingzhc");
		g.setRealName("xingzhc");
		g.setEmail("xingzhc@css.com.cn");
		g.setPassword("css523");
		as.setSUser(g);
		SSys s = new SSys();
		s.setName("信息系统5557");
		s.setOpenFlag("1");
		s.setRemark("sysNames");
		s.setUrl("http://baidu.com");
		as.setItem(s);
		
//		SLog sLog = new SLog();
//		sLog.setUuid(UuidUtil.getUuid());
//		sLog.setOperId("99");
//		sLog.setOperName("admin");
//		sLog.setOperIp("10.13.1.143");
//		sLog.setOperType("1");
////		sLog.setLogLevel("1");
//		sLog.setLogDate(Calendar.getInstance().getTime());
//		String content = JSONObject.fromObject(sLog, Ajax.getJsonConfig()).toString();
//		sLog.setContent(content);
//		LogMan.addLog(sLog);
		as.adminGo();
	}
}
