package com.hnjz.apps.oa.dataexsys.service.impl.dataex;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataexReceiptProcessor extends DataexProcessor {
	private static final Log _logger = LogFactory.getLog(DataexReceiptProcessor.class);
	
	public String process(Object xml, ClientInfo clientInfo) {
		if(xml!=null && xml instanceof Document){
			Document doc = (Document)xml;
			PkgOaAndEx pkg = null;
			try {
				pkg = new PkgOaAndEx(doc);
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
							DataexSysTransContent transContent = res.getContent();
							transContent.setPackType(Constants.MSG_FEEDBACK);
							transContent.setDocTitle("撤销");
							DataexSysTaskHelper.dataexsysFlag(pkg.getReceivers().get(0), pkg.getSender(),transContent , Constants.ARRIVE, "");
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
										DataexSysTaskHelper.dataexsysFlag(pkg.getReceivers().get(0), pkg.getSender(), transContent, Constants.ARRIVE, "");
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
			
			String request = doc.asXML();
			try {
				//发文机关
				Identity sender = pkg.getSender();
				String senderId = sender.getIdentityFlag();
				//校验发文机关合法性
				if (StringHelper.isEmpty(senderId)) {
					String msg = "sendOrg is empty:["+sender.asXML()+"]";
					throw new Exception(msg);
				}
				if (!TaskHelper.checkOrg(senderId)) {
					String msg = "sendOrg not in dataex dirs:["+sender.asXML()+"]";
					throw new Exception(msg);
				}
				//收文机关
				List<Identity> receivers = pkg.getReceivers();
				if(receivers==null || receivers.size()==0){
					String msg = "receiverOrg is empty.";
					throw new Exception(msg);
				}
				Identity receiver = receivers.get(0);
				String receiverId = receiver.getIdentityFlag();
				//校验收文机关合法性
				if (StringHelper.isEmpty(receiverId)) {
					String msg = "receiverOrg is empty:["+receiver.asXML()+"]";
					throw new Exception(msg);
				}
				if (!TaskHelper.checkOrg(receiverId)) {
					String msg = "receiverOrg not in dataex dirs:["+receiver.asXML()+"]";
					throw new Exception(msg);
				}
				//2.入库
				//保存原始数据
				DataexSysTrans original = new DataexSysTrans();
				try {
					TaskHelper.setOriginalData(original, request, Constants.GWSYS, Constants.MSG_FEEDBACK, clientInfo, sender);
				} catch (Exception e) {
					throw new Exception("[method(setOriginalData) type:Exception. ]");
				}
				//开启事务
				TransactionCache tx = null;
				try {
					tx = new TransactionCache();
					tx.save(original);
					tx.commit();
					//开启子线程入库
					TaskHelper.startDBWorkerThread(pkg, this, original);
				} catch (Exception e) {
					if (tx != null) {
						tx.rollback();
					}
					String msg = "[save trans data error. ]";
					throw new Exception(msg);
				}
			} catch (Exception e) {
				String msg = e.getMessage();
				_logger.error(msg);
				//TaskHelper.startFileWorkerThread(msg+"\n"+request);
				return Ajax.JSONResult(Constants.FAILURE, msg);
			}
			return Ajax.JSONResult(Constants.SUCCESS, "send success!");
		}
		return null;
	}
	
	public Identity getSender(PkgData data) {
		PkgOaAndEx pkg = (PkgOaAndEx)data;
		return pkg.getSender();
	}
	
	public List<DataexSysTransTask> saveTransData(TransactionCache tx, DataexSysTrans original, PkgData data) throws Exception {
		//多条收文机关，循环添加到task任务里
		List<DataexSysTransTask> lst = new ArrayList<DataexSysTransTask>();
		PkgOaAndEx pkg = (PkgOaAndEx)data;
		if(pkg.getReceivers()!=null && pkg.getReceivers().size()>0)
		{
			for (Identity receiver: pkg.getReceivers()) {
				//收文机关
				//Identity receiver = pkg.getReceivers().get(0);
				//存储处理后公文内容
				DataexSysTransContent content = new DataexSysTransContent();
				saveDataexSysTransContent(original, content, pkg);
				
				//说明：分解处理系统发送的公文包，根据收文机关分解为多个任务
				//根据recvOrg查找接收方服务地址
				DataexSysDir targetInfo = TaskHelper.findDataexSysDir(receiver);
				DataexSysNode exnode = QueryCache.get(DataexSysNode.class, targetInfo.getExnodeId());
				List<DataexSysDirWs> ws = DataexSysTaskHelper.findDataexSysDirWs(exnode.getExnodeId(), Constants.WSTYPE_SEND);
				String dataServiceUrl = null;
				if (ws.size() > 0) {
					dataServiceUrl = ws.get(0).getDataServiceUrl();
				}
				DataexSysTransTask task = new DataexSysTransTask();
				ReceiptContent receiptContent = (ReceiptContent) pkg.getContentInstance();
				OtherExpansion expansion = new OtherExpansion();
				expansion.setOther(receiptContent.getExpansion().asXML());
				IExpansionProcessor processer = ExpansionProcessorFactory.getProcessor();
				processer.unpkg(content, task, expansion,null);
				
				
				if (StringHelper.isNotEmptyByTrim(dataServiceUrl)) {//考虑的应用场景：导出公文包，为单机使用
					TaskHelper.setDataexSysTransTask(task, content, Constants.OP_SUCCESS, receiver, targetInfo);
					lst.add(task);
				}else{
					TaskHelper.setDataexSysTransTask(task, content, Constants.OP_FAILURE, receiver, targetInfo);
				}
				tx.save(content);
				tx.save(task);
			}	
		}
		return lst;
	}
	
	protected static void saveDataexSysTransContent(DataexSysTrans orginal, DataexSysTransContent transContent, PkgOaAndEx pkg) throws Exception {
		ReceiptContent content = (ReceiptContent)pkg.getContentInstance();
		//发文机关
		Identity sender = pkg.getSender();
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
