package com.hnjz.apps.oa.dataexsys.service.impl.dataexsys;

import com.hnjz.apps.oa.dataexsys.admin.model.*;
import com.hnjz.apps.oa.dataexsys.cas.QueueUtil;
import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.pkg.model.*;
import com.hnjz.apps.oa.dataexsys.service.expansion.ExpansionFactory;
import com.hnjz.apps.oa.dataexsys.service.expansion.ExpansionProcessorFactory;
import com.hnjz.apps.oa.dataexsys.service.expansion.IExpansionProcessor;
import com.hnjz.apps.oa.dataexsys.service.task.TaskHelper;
import com.hnjz.apps.oa.dataexsys.service.thread.DataexSysTaskHelper;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.apps.oa.dataexsys.util.Ajax;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import java.util.Date;
import java.util.List;

public class DataexsysReceiptProcessor extends DataexsysProcessor {
	private static final Log _logger = LogFactory.getLog(DataexsysReceiptProcessor.class);

	public String process(Object xml, ClientInfo clientInfo) {
		if(xml!=null && xml instanceof Document){
			Document doc = (Document)xml;
			PkgExAndEx pkg = null;
			try {
				pkg = new PkgExAndEx(doc);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if(pkg==null)return null;
			PkgData contentInstance = pkg.getContentInstance();
			if( !(contentInstance!=null && contentInstance instanceof ReceiptContent) ){
				return null;
			}
			//短报文是撤销
			//从队列中删除不执行,返回success
			//从数据库中删除sendstatus为3的数据,返回success
			//否则继续路由
			ReceiptContent receiptContent = (ReceiptContent)contentInstance;
			if("撤销".equals(receiptContent.getReceiptContent())){
				String docId = receiptContent.getDocId();
				if(StringHelper.isNotEmpty(docId)){
					//队列
					String resType = QueueUtil.QUEUE_DATAEXSYS;
					DataexSysTransTask res = QueueUtil.getDataFromQueueByDocId(resType, docId);
					if(res!=null){
						try {
							DataexSysTransContent  transContent = res.getContent();
							transContent.setPackType(Constants.MSG_FEEDBACK);
							transContent.setDocTitle("撤销");
							DataexSysTaskHelper.dataexsysFlag(pkg.getMsgReceiver(), pkg.getMsgSender(), transContent, Constants.ARRIVE, "");
						} catch (Exception e) {
							e.printStackTrace();
						}
						TaskHelper.clearMark(res,res.getContent());
						return Ajax.JSONResult(Constants.SUCCESS, "send success!");
					}
					//数据库
					List<DataexSysTransTask> tasks = QueueUtil.getPreResProcessList();
					if(tasks!=null && tasks.size()>0){
						for(DataexSysTransTask task : tasks){
							if(docId.equals(task.getContent().getDocId())){
								if(QueueUtil.delRes(task)){
									try {
										DataexSysTransContent  transContent = task.getContent();
										transContent.setPackType(Constants.MSG_FEEDBACK);
										transContent.setDocTitle("撤销");
										DataexSysTaskHelper.dataexsysFlag(pkg.getMsgReceiver(), pkg.getMsgSender(), transContent, Constants.ARRIVE, "");
									} catch (Exception e) {
										e.printStackTrace();
									}
									TaskHelper.clearMark(task,task.getContent());
									return Ajax.JSONResult(Constants.SUCCESS, "send success!");
								}
							}
						}
					}
				}
			}
			
			
			try {
				//校验发文机关合法性
				Identity sender = pkg.getMsgSender();
				String senderId =sender.getIdentityFlag();
				if (StringHelper.isEmpty(senderId)) {
					String msg = "sendOrg is empty:["+sender.asXML()+"]";
					throw new Exception(msg);
				}
				if (!TaskHelper.checkOrg(senderId)) {
					String msg = "sendOrg not in dataex dirs:["+sender.asXML()+"]";
					throw new Exception(msg);
				}
				//验签操作[防篡改功能]
				String signatureBody = pkg.getSignature().getSignatureBody();
				String content = pkg.getContent();
				if (StringHelper.isNotEmptyByTrim(signatureBody)) {
					DataexSysDir dir = TaskHelper.findDataexSysDir(sender);
					DataexSysNode exnode = QueryCache.get(DataexSysNode.class, dir.getExnodeId());
					if (!VerifyUtil.verify(Constants.getAlg(), exnode.getCertPath(), signatureBody, content)) {
						String msg = "[验签操作[防篡改功能]]:["+pkg.getSignature().asXML()+"]";
						throw new Exception(msg);
					}
				}
				//校验收文机关合法性
				Identity receiver = pkg.getMsgReceiver();
				String receiverId = receiver.getIdentityFlag();
				if (StringHelper.isEmpty(receiverId)) {
					String msg = "receiverOrg is empty:["+receiver.asXML()+"]";
					throw new Exception(msg);
				}
				if (!TaskHelper.checkOrg(receiverId)) {
					String msg = "receiverOrg not in dataex dirs:["+receiver.asXML()+"]";
					throw new Exception(msg);
				}
				//检查传输票据，即令牌token,判断其是否签到
				String token = pkg.getTransToken();
				List<String> lst = new QueryCache("select a.uuid from DataexSysSign a where a.token=:token").setParameter("token",token).list();
				if (lst.isEmpty()) {
					String msg = "[检查传输票据，即令牌token,判断其是否签到]:["+token+"]";
					throw new IllegalAccessException(msg);//[2、重新签到]
				}
				//更新签到表
				String uuid = lst.get(0);
				DataexSysSign sign = QueryCache.get(DataexSysSign.class, uuid);
				//判断签到超时
				if (Constants.isOpen()) {
					if (TaskHelper.checkSignTimeout(sign)) {
						String msg = "[判断签到超时]:["+token+"]";
						throw new IllegalAccessException(msg);//[2、重新签到]
					}
				}
				//2.入库
				//开启子线程入库
				TaskHelper.startDBSYSWorkerThread(pkg, this, clientInfo);
				return Ajax.JSONResult(Constants.SUCCESS, "send success!");
			} catch (IllegalAccessException e) {
				String msg = e.getMessage();
				_logger.error(msg);
				//TaskHelper.startFileWorkerThread(msg+"\n"+result);
				return Ajax.JSONResult(Constants.RESEND, msg);
			} catch (Exception e) {
				String msg = e.getMessage();
				_logger.error(msg);
				//TaskHelper.startFileWorkerThread(msg+"\n"+result);
				return Ajax.JSONResult(Constants.FAILURE, msg);
			}
		}
		return Ajax.JSONResult(Constants.FAILURE,"current handler can not process request!");
	}
	
	
	public Identity getSender(PkgData data) {
		PkgExAndEx pkg = (PkgExAndEx)data;
		return pkg.getMsgSender();
	}
	
	public DataexSysTransTask saveTransData(TransactionCache tx,PkgData data, ClientInfo clientInfo) throws Exception{
		PkgExAndEx pkg = (PkgExAndEx)data;
		String request = pkg.asXML();
		Identity sender = pkg.getMsgSender();
		//内容类型
		String contentType = pkg.getMsgAttr();//Constants.MSG_FEEDBACK

		//收文机关
		Identity receiver = pkg.getMsgReceiver();
		
		//2.入库
		//保存原始数据
		DataexSysTrans original = new DataexSysTrans();
		try {
			TaskHelper.setOriginalData(original, request, Constants.DATAEXSYS, contentType, clientInfo, sender);
		} catch (Exception e) {
			throw new Exception("[method(setOriginalData) type:Exception. ]");
		}

		//存储处理后公文内容
		DataexSysTransContent content = new DataexSysTransContent();
		setDataexSysTransContent(original, content, pkg);
		DataexSysTransAccount account = new DataexSysTransAccount();
		TaskHelper.updateInData(account, original, Constants.ACCOUNT_RECV, Constants.OP_SUCCESS, Constants._RECV_SUCCESS_MEMO);
		//保存处理后附件 ???
		//saveDataexSysDocAttach(content, tx, lstAttachment);
		//根据recvOrg查找接收方服务地址
		DataexSysDir targetInfo = TaskHelper.findDataexSysDir(receiver);
		DataexSysNode exnode = QueryCache.get(DataexSysNode.class, targetInfo.getExnodeId());
		List<DataexSysDirWs> ws = DataexSysTaskHelper.findDataexSysDirWs(exnode.getExnodeId(), Constants.WSTYPE_SEND);
		String dataServiceUrl = null;
		if (ws.size() > 0) {
			dataServiceUrl = ws.get(0).getDataServiceUrl();
		}
		tx.save(original);
		tx.save(account);
		tx.save(content);
		DataexSysTransTask task = new DataexSysTransTask();
		ReceiptContent receiptContent = (ReceiptContent) pkg.getContentInstance();
		OtherExpansion expansion = new OtherExpansion();
		expansion.setOther(receiptContent.getExpansion().asXML());
		IExpansionProcessor processer = ExpansionProcessorFactory.getProcessor();
		processer.unpkg(content, task, expansion,null);
		
		if (StringHelper.isEmptyByTrim(dataServiceUrl)) {
			//保存task
			TaskHelper.setDataexSysTransTask(task, content, Constants.OP_FAILURE, receiver, targetInfo);
			tx.save(task);				
			return null;
		} else {
			//保存task
			TaskHelper.setDataexSysTransTask(task, content, Constants.OP_SUCCESS, receiver, targetInfo);
			tx.save(task);
			return task;
		}
	}
	
	
	public static void setDataexSysTransContent(DataexSysTrans orginal, DataexSysTransContent transContent, PkgExAndEx pkg) throws Exception {
		ReceiptContent content = (ReceiptContent) pkg.getContentInstance();
		//发文机关
		Identity sender = pkg.getMsgSender();
		String senderId = sender.getIdentityFlag();
		String senderName = sender.getIdentityName();
		//回执信息
		Date receiptTime = content.getReceiptTime();
		String receiptContent = content.getReceiptContent();
		String receiptMemo = content.getReceiptMemo();
		//发件id
		String outboxId = content.getDocId();
		//备注
		OtherExpansion expansion = (OtherExpansion) ExpansionFactory.getExpansion(content.getExpansion().asXML()) ;
		transContent.setExpansion(expansion.getOther());
		//存储公文
		String uuid = UuidUtil.getUuid();
		transContent.setUuid(uuid);
		transContent.setTransId(orginal.getUuid());
		if(StringHelper.isNotEmpty(outboxId)){
			transContent.setDocId(outboxId);
		}
		transContent.setSendOrg(senderId);
		transContent.setSendOrgName(senderName);
		transContent.setSendOrgDesc(sender.getIdentityDesc());
		transContent.setPackType(orginal.getPackType());//报文类型：公文包--Constants.MSG_DATAPACK
		transContent.setRecvTime(orginal.getRecvTime());
		transContent.setDocTitle(receiptContent);
		transContent.setRemark(receiptMemo);
		transContent.setClientIp(orginal.getClientIp());
		transContent.setSendTime(receiptTime);
		transContent.setRecvStartTime(orginal.getRecvStartTime());
		transContent.setPackSize(orginal.getPackSize());
		transContent.setTransStatus(orginal.getTransStatus());
	}
	

}
