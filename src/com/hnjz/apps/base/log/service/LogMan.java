/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file LogMan.java creation date: [Jan 3, 2014 2:31:03 PM] by liuzhb
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.log.service;

import com.hnjz.apps.base.audit.service.AuditParamService;
import com.hnjz.apps.base.audit.service.AuditParameter;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.log.model.SLog;
import com.hnjz.core.model.DefaultActionLog;
import com.hnjz.core.model.IUser;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.FunctionManager;
import com.hnjz.webbase.WebBaseUtil;
import com.hnjz.webbase.webwork.WebworkUtil;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogMan {
	private static Log _log = LogFactory.getLog(LogMan.class);
	 
	public static String getTable(){
		//Integer month = Calendar.getInstance().getTime().getMonth() + 1;
		//String table = "S_LOG_" + month;
		String table = "S_LOG";
		return table;
	}
	
	public static SLog getCurrentLogPart(){
		SLog log=new SLog();
		log.setLogId(UuidUtil.getUuid());
		 //设置用户信息
		 IUser  user = WebBaseUtil.getCurrentUser();
		 if(user!=null){
			 log.setOpId(String.valueOf(user.getUserId()));
			 log.setOpName(user.getRealName());
			 log.setOperatorType(user.getType());
		 }
		ActionContext ac= WebworkUtil.getActionContext();
		if(ac!=null && ac.getActionInvocation()!=null){
			ActionProxy proxy=ac.getActionInvocation().getProxy();
			String packageName = proxy.getConfig().getPackageName();	  
			String actionName = proxy.getActionName();
			String actionId = packageName+"/"+actionName;
			log.setFuncId(actionId);
			FunctionManager mgr = FunctionManager.getFunctionManager();
			DefaultActionLog func=mgr.getActionLog(actionId); 
			if(func == null){		
				//记录系统日志失败
				_log.warn("无效的Action配置，无法记录日志.");
			}else{
				if(StringHelper.isNotEmpty(func.getSystemId()))
					log.setSysid(func.getSystemId());
				if (func.getType() != null)
					log.setOpType(Integer.valueOf(func.getType()));
				//设置操作名称
				log.setLogData(func.getActionDesc());		
				try {
					if(StringHelper.isNotEmptyByTrim(func.getEventType()))
						log.setEventType(Integer.parseInt(func.getEventType()));
				} catch (NumberFormatException e) {					 
					e.printStackTrace();
				}		
				log.setPackageName(func.getPackageName());		
			}
			try {
				//设置服务器IP和主机名
		 		 
		 			java.net.InetAddress addr=java.net.InetAddress.getLocalHost();
		 			log.setServerIp(addr.getHostAddress());
		 			log.setServerName(addr.getHostName());
		 		 	
			} catch (Exception e) {					 
				e.printStackTrace();
			}		
			 
		}		
		return log;
	}
/**
	 * 添加日志
	 * @author liuzhb on Jan 6, 2014 4:59:48 PM
	 * @param sLog
	 * @return
 */
	 public static boolean addLog(SLog sLog) {		 	
			try {
				AddLogThread addLogThread = new AddLogThread(sLog);
				addLogThread.start();
				return true;
			} catch (Exception ex) {
				_log.error(ex.getMessage(), ex);
				return false;
			}
	 }

 public static boolean addLogPart(LogPart logpart) {
	 //日志记录开关
	  String logSwitch = AuditParamService.getParamValue(AuditParameter.LOG_OPEN);
	  if(logSwitch==null)
		  logSwitch="true";
	  if("true".equals(logSwitch))
		try {
			SLog sLog = getCurrentLogPart();
		 	sLog.setLogData(logpart.getLogData());
		 	sLog.setLogLevel(logpart.getLogLevel());
		 	sLog.setOpObjId(logpart.getOpObjId());
		 	sLog.setOpObjType(logpart.getOpObjType());
		 	sLog.setRelObjType(logpart.getRelObjType());
		 	sLog.setRelObjId(logpart.getRelObjId());
		 	sLog.setOpType(logpart.getOpType());
		 	sLog.setOpTime(java.util.Calendar.getInstance().getTime());
		 	sLog.setOpIp(WebworkUtil.getRemoteAddress());
		 	sLog.setOpType(logpart.getOpType());
		 	sLog.setMemo(logpart.getMemo());
		 	if(StringHelper.isNotEmptyByTrim(logpart.getResult()))
		 		sLog.setResult(logpart.getResult());
			AddLogThread addLogThread = new AddLogThread(sLog);
			addLogThread.start();
			return true;
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
			return false;
		}
		return false;
}

	/**
	 * 获取日志分页列表数据
	 * @author liuzhb on Jan 6, 2014 5:00:03 PM
	 */
	public static Page dirLog(Page page) {
		try {
			StringBuffer sb = new StringBuffer(" select a.uuid from SLog a ");
			QueryCache qc = new QueryLog(sb.toString());
			page = qc.pageCache(page);
			page.setResults(QueryLog.idToObj(SLog.class, page.getResults()));
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
		return page;
	}
	
	/**
	 * 获取单条日志对象，无缓存；
	 * 通过QueryLog.get(SLog.class, uuid)方法也可以获取日志对象，数据自动缓存
	 * @author liuzhb on Jan 6, 2014 5:02:53 PM
	 * @param uuid
	 * @return
	 */
	public static SLog getLog(String uuid) {
		SLog sLog = null;
		try {
			QueryCache qc = new QueryLog(" from SLog a where a.uuid=:uuid").setParameter("uuid", uuid);
			sLog = (SLog)qc.uniqueResult();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
		return sLog;
	}
	
	/*public static String convertToHtml (String content) throws Exception{
		JSONObject jo = JSONObject.fromObject(content);
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss"}));
		String className = jo.getString("className");
		Object tmp = (Object)JSONObject.toBean(jo, Class.forName(className));
		String str = (String)ReflectionUtil.invokeMethod(tmp, "getLogInfo", new Object[]{});
		return str;
	}*/
	
	public static void main(String[] args) {
		try {
			//添加SLog
			/*SLog sLog = new SLog();
			sLog.setUuid(UuidUtil.getUuid());
			sLog.setOperId("99");
			sLog.setOperName("admin");
			//			sLog.setOperIp("10.13.1.143");
			sLog.setOperType("1");
			//			sLog.setLogLevel("1");
			sLog.setLogDate(Calendar.getInstance().getTime());
			String content = Json.object2json(sLog);
			sLog.setContent(content);*/
			//addLog(sLog);
			
			//获取SLog对象并进行简析
//			sLog = getLog("8a8d81d443b8e9ff0143b8ea008b0000");
//			sLog = QueryLog.get(SLog.class, "8a8d81d443b8e9ff0143b8ea008b0000");
//			SLog tmp = null;
//			System.out.println(sLog.getContent());
			
//			JSONObject jo = JSONObject.fromObject(sLog.getContent());
//			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss"}));
//			
//			String className = jo.getString("className");
//			System.out.println(className);
//			tmp = (SLog)JSONObject.toBean(jo, Class.forName(className));
//			
//	        String str = (String)ReflectionUtil.invokeMethod(tmp, "getLogInfo", new Object[]{});
//			System.out.println(convertToHtml(sLog.getContent()));
			
			//获取日志列表
//			Page page = new Page();
//			page.setCurrentPage(1); //设置当前页
//			page.setCountField("a.uuid"); //设置求和字段
//			page.setOrderString("a.logDate");//设置排序字段
//			page.setOrderFlag(Page.OEDER_DESC);//设置排序方式
//			page.setPageSize(20);//设置每页记录数，不记录则取配置类参数
//			page = dirLog(page);
//			System.out.println("页数:"+page.getTotalPages());
//			System.out.println("记录总数:"+page.getTotalRows());
//			for (SLog log : (List<SLog>)page.getResults())
//				System.out.println(log.getUuid());
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
