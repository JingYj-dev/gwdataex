package com.hnjz.apps.oa.dataexsys.service;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysSign;
import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.pkg.GWPackagesUtil;
import com.hnjz.apps.oa.dataexsys.service.thread.InitProducerComponent;
import com.hnjz.db.hibernate.ThreadLocalMap;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.apps.oa.dataexsys.util.Ajax;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * 前置条件：同步交换目录
 * @author liyzh
 *
 */
public class DataexService {
	
	private static final Log _logger = LogFactory.getLog(DataexService.class);
	
	//获取线程池
	private ExecutorService threadPool = InitProducerComponent._threadPool;//Executors.newFixedThreadPool(Constants.getThreadPoolSize());
	
	public DataexService() {
		initialize();
	}
	
	private void initialize() {
		if (threadPool == null) {
			InitProducerComponent.init();
			threadPool = InitProducerComponent._threadPool;
		}
	}
	
	
	/**
	 * [success、成功][failure、失败][resend、重新签到]
	 * @Title: sendBaseXMLEsbWebService
	 * @Description: TODO(描述方法功能:数据接收接口)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String sendBaseXMLEsbWebService(String message) throws Exception {
		Document doc = null;
		try {
			doc = GWPackagesUtil.parsePackage(message);
		} catch (Exception e) {
			String msg = "parse request error:"+e.getMessage();
			System.out.print(msg);
			_logger.error(msg);
			return Ajax.JSONResult(Constants.FAILURE, msg);
		}
		SendBaseXMLEsbHandler handler = SendBaseXMLEsbHandlerFactory.getHandler(doc);
		if(handler==null){
			String msg = "no handler to process request!";
			_logger.error(msg);
			return Ajax.JSONResult(Constants.FAILURE, msg);
		}
		ClientInfo clientInfo = (ClientInfo) ThreadLocalMap.get(Constants.KEY);
		return handler.process(doc, clientInfo);
	}


	/**
	 * [success、成功][failure、失败][resend、重新签到]
	 * @Title: sendBaseXMLEsbWebService
	 * @Description: TODO(描述方法功能:交换系统间传输报文接口)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String sendBaseXMLEsbWebService(String transCode, String message) throws Exception {
		Document doc = null;
		try {
			doc = GWPackagesUtil.parsePackage(message);
		} catch (Exception e) {
			String msg = "parse request error:"+e.getMessage();
			System.out.print(msg);
			_logger.error(msg);
			return Ajax.JSONResult(Constants.FAILURE, msg);
		}
		SendBaseXMLEsbHandler handler = SendBaseXMLEsbHandlerFactory.getHandler(doc);
		if(handler==null){
			String msg = "no handler to process request!";
			_logger.error(msg);
			return Ajax.JSONResult(Constants.FAILURE, msg);
		}
		ClientInfo clientInfo = (ClientInfo) ThreadLocalMap.get(Constants.KEY);
		return handler.process(doc, clientInfo);
	}

	/**
     * @Title: reqWebService
     * @Description: TODO(描述方法功能:交换握手接口)
     * @param identityId
     * @param randomVal
     * @param timeout
     * @param reqType
     * @return
     * @throws Exception
     */
    public String reqWebService(String unitId, String linkCode, String seconds, String reqType) throws Exception {
    	DataexSysSign sign = null;
    	Object id = new QueryCache("select a.uuid from DataexSysSign a where a.identityId=:identityId")
    		.setParameter("identityId",unitId).setMaxResults(1).uniqueResult();
    	if(id!=null && StringHelper.isEmpty(id.toString())){
    		sign = QueryCache.get(DataexSysSign.class, id.toString());
    	}
    	if (reqType.equals(Constants.BGN)) {
    		boolean newReq = sign==null;
    		String token = UuidUtil.getUuid();
    		if(newReq){
    			sign = new DataexSysSign();
    			String uuid = UuidUtil.getUuid();
    			sign.setUuid(uuid);
    		}
    		sign.setIdentityId(unitId);
    		sign.setRandomId(linkCode);
    		sign.setSignTime(new Date());
    		sign.setTimeOut(Integer.parseInt(seconds));
    		sign.setToken(token);
    		sign.setLastDataexTime(new Date());
    		TransactionCache tx = null;
    		try {
				tx = new TransactionCache();
				if(newReq){
					tx.save(sign);
				}else{
					tx.update(sign);
				}
				tx.commit();
			} catch (Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				String msg = e.getMessage();
				if (_logger.isErrorEnabled()) {
					_logger.error(msg, e);
				}
				return "<refuse>"+msg+"</refuse>";
			}
    	} else if (reqType.equals(Constants.END)) {
    		TransactionCache tx = null;
    		try {
    			if(sign==null){
    				throw new Exception(unitId+" not found.");
    			}
				tx = new TransactionCache();
				tx.delete(sign);
				tx.commit();
			} catch (Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				String msg = e.getMessage();
				if (_logger.isErrorEnabled()) {
					_logger.error(msg, e);
				}
				return "<refuse>"+msg+"</refuse>";
			}
    	}
    	return "<result><unitId>"+sign.getIdentityId()+"</unitId><linkCode>"+sign.getRandomId()+"</linkCode><transCode>"+sign.getToken()+"</transCode></result>";
    }

}
