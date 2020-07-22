package com.hnjz.apps.oa.dataexsys.service.impl.dataex;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.oa.dataexsys.admin.model.*;
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
import com.hnjz.util.FileUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.apps.oa.dataexsys.util.Ajax;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import sun.misc.BASE64Decoder;

import java.util.ArrayList;
import java.util.List;

public class DataexGwProcessor extends DataexProcessor {
	private static final Log _logger = LogFactory.getLog(DataexGwProcessor.class);

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
			PkgData content = pkg.getContentInstance();
			if( !(content!=null && content instanceof GwContent) ){
				return null;
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
				for(Identity receiver : receivers){
					String receiverId = receiver.getIdentityFlag();
					//校验收文机关合法性
					if (StringHelper.isEmpty(receiverId)) {
						String msg = "receiverOrg is empty:["+receiver.asXML()+"]";
						throw new Exception(msg);
					}
					if(receiverId.length()>3){
						receiverId = receiverId.substring(0, 3);
					}
					if (!TaskHelper.checkOrg(receiverId)) {
						String msg = "receiverOrg not in dataex dirs:["+receiver.asXML()+"]";
						throw new Exception(msg);
					}
				}
				//2.入库
				//保存原始数据
				DataexSysTrans original = new DataexSysTrans();
				try {
					TaskHelper.setOriginalData(original, request, Constants.GWSYS, Constants.MSG_DATAPACK, clientInfo, sender);
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
			} catch (Exception e1) {
				String msg = e1.getMessage();
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
		PkgOaAndEx pkg = (PkgOaAndEx)data;
		GwContent content = (GwContent)pkg.getContentInstance();
		//收文机关
		List<Identity> receivers = pkg.getReceivers();
		//存储处理后公文内容
		DataexSysTransContent transContent = new DataexSysTransContent();
		saveDataexSysTransContent(original, transContent, pkg);
		//保存处理后附件信息
		TaskHelper.saveDataexSysDocAttach(transContent, tx, pkg.getAttachs());
		List<DataexSysTransTask> lst = new ArrayList<DataexSysTransTask>();
		//说明：分解处理系统发送的公文包，根据收文机关分解为多个任务
		for (Identity receiver : receivers) {
			String oldReceitveId = receiver.getIdentityFlag();
			//根据recvOrg查找接收方服务地址
			if(receiver.getIdentityFlag().length()>3){
				receiver.setIdentityFlag(receiver.getIdentityFlag().substring(0, 3));
			}
			String receiverId = receiver.getIdentityFlag();
			DataexSysDir targetInfo = TaskHelper.findDataexSysDir(receiver);
			DataexSysNode exnode = QueryCache.get(DataexSysNode.class, targetInfo.getExnodeId());
			List<DataexSysDirWs> ws = DataexSysTaskHelper.findDataexSysDirWs(exnode.getExnodeId(), Constants.WSTYPE_SEND);
			String dataServiceUrl = null;
			if (ws.size() > 0) {
				dataServiceUrl = ws.get(0).getDataServiceUrl();
			}
			DataexSysTransTask task = new DataexSysTransTask();
			
			Expansion expansion = ExpansionFactory.getExpansion(content.getExpansion().asXML()) ;
			IExpansionProcessor processer = ExpansionProcessorFactory.getProcessor();
			//processer.unpkg(transContent, task, expansion,new String[]{receiverId});
			processer.unpkg(transContent, task, expansion,new String[]{oldReceitveId});
			if (StringHelper.isNotEmptyByTrim(dataServiceUrl)) {//考虑的应用场景：导出公文包，为单机使用
				TaskHelper.setDataexSysTransTask(task, transContent, Constants.OP_SUCCESS, receiver, targetInfo);
				lst.add(task);
			}else{
				TaskHelper.setDataexSysTransTask(task, transContent, Constants.OP_FAILURE, receiver, targetInfo);
			}
			tx.save(task);					
		}
		tx.save(transContent);
		return lst;
	}
	
	private static void saveDataexSysTransContent(DataexSysTrans orginal, DataexSysTransContent transContent, PkgOaAndEx pkg) throws Exception {
		GwContent content = (GwContent)pkg.getContentInstance();
		//公文信息
		String jjcd = content.getEmergency();
		String gwmj = content.getSecurity();
		String gwTitle = content.getTitle();
		String gwContent = content.getContent();
		//发文机关
		Identity sender = pkg.getSender();
		String senderId = sender.getIdentityFlag();
		String senderName = sender.getIdentityName();
		//备注信息
		String remarkInfo = pkg.getRemark();
		//发件id
		String outboxId = "";
		OtherExpansion expansion = (OtherExpansion) ExpansionFactory.getExpansion(content.getExpansion().asXML()) ;
		outboxId = expansion.getOutboxId();
		transContent.setDocId(outboxId);
		transContent.setExpansion(expansion.getOther());
		//存储公文
		String uuid = UuidUtil.getUuid();
		String fileName = uuid + Constants.GW_CONTENT_SUFFIX;
		Integer nServerId = Integer.parseInt(Constants.RECV_SYS_DIR_ID);
		String filepath = AttachItem.filePath(nServerId);
		String sFile = filepath + fileName;
		FileUtil.mkfile(sFile);
		FileUtil.getFileFromBytes(new BASE64Decoder().decodeBuffer(gwContent), sFile);
		
		transContent.setUuid(uuid);
		transContent.setTransId(orginal.getUuid());
		transContent.setSendOrg(senderId);
		transContent.setSendOrgName(senderName);
		transContent.setSendOrgDesc(sender.getIdentityDesc());
		transContent.setReceivOrgName(content.getReceiver());
		transContent.setPackType(orginal.getPackType());//报文类型：公文包--Constants.MSG_DATAPACK
		transContent.setDocTitle(gwTitle);
		transContent.setDocSecurity(gwmj);
		transContent.setDocEmergency(jjcd);
		transContent.setRecvTime(orginal.getRecvTime());
		transContent.setDocAddress(fileName);
		transContent.setServerId(Constants.RECV_SYS_DIR_ID);
		transContent.setRemark(remarkInfo);
		transContent.setClientIp(orginal.getClientIp());
		transContent.setSendTime(orginal.getSendTime());
		transContent.setRecvStartTime(orginal.getRecvStartTime());
		transContent.setPackSize(orginal.getPackSize());
		transContent.setTransStatus(orginal.getTransStatus());
	}
}
