package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.oa.dataexsys.admin.model.*;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.common.HostInfo;
import com.hnjz.apps.oa.dataexsys.pkg.model.*;
import com.hnjz.apps.oa.dataexsys.service.expansion.ExpansionProcessorFactory;
import com.hnjz.apps.oa.dataexsys.service.expansion.IExpansionProcessor;
import com.hnjz.apps.oa.dataexsys.service.task.TaskHelper;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.apps.oa.dataexsys.service.ISender;
import com.hnjz.apps.oa.dataexsys.service.QueryService;
import com.hnjz.apps.oa.dataexsys.service.SenderProvider;
import com.hnjz.apps.oa.dataexsys.util.Base64Util;
import com.hnjz.apps.oa.dataexsys.util.DocFzUtil;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.net.InetAddress;
import java.util.*;

public class DataexSysTaskHelper {
	private static final Log _logger = LogFactory.getLog(DataexSysTaskHelper.class);
	
	private Map<String, Object> map;
	
	public DataexSysTaskHelper(Map<String, Object> map) {
		this.map = map;
	}
	
	
	public String process() throws Exception {
		String flag = Constants.FAILURE;
		DataexSysDir targetInfo = (DataexSysDir) map.get("targetInfo");
		String exnodeId = targetInfo.getExnodeId();
		if (StringHelper.isEmptyByTrim(exnodeId)) {
			return flag;
		}
		DataexSysNode exnode = QueryCache.get(DataexSysNode.class, exnodeId);
		String targetType = exnode.getExnodeType();
		List<DataexSysDirWs> ws = findDataexSysDirWs(exnodeId, null);//2代表发送,1代表握手，3代表hesssion 默认取发送接口
		if (ws == null || ws.size() == 0) {
			return flag;
		}
		List<DataexSysDirWs> sendws = findDataexSysDirWs(exnodeId, "2");
		if (sendws == null || sendws.size() == 0) {
			return flag;
		}
		String endpoint = sendws.get(0).getDataServiceUrl();
		DataexSysTransContent content = (DataexSysTransContent) map.get("gwContent");
		String[] recvOrg = (String[]) map.get("recvOrg");
		DataexSysTransTask task = (DataexSysTransTask) map.get("kqueue");
		if (content == null) {
			return flag;
		}
		//记录对账单表
		DataexSysTransAccount account = new DataexSysTransAccount();
		setDataexSysTransAccount(account, content, task, Constants.ACCOUNT_SEND, Constants.FAILURE, Constants._SEND_FAILURE_MEMO);
		//更新状态：任务运行中
//		workQueue.setSendStatus(Constants.kqueue_sended);
		TransactionCache tx = null;
		try {
			tx = new TransactionCache();
//			tx.update(workQueue);
			tx.save(account);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[update kqueue task error. ]":e.getMessage(), e);
			}
		}
		String gwContent = "";
		//方正交换不需要gwContent
		if(Constants.MSG_DATAPACK.equals(content.getPackType()) && !"3".equals(targetType)){
			//做I/O操作读取存储在文件系统的公文信息
			Integer nServerId = Integer.parseInt(content.getServerId());
			String filepath = AttachItem.filePath(nServerId);
			String file = filepath + content.getDocAddress();
			gwContent = Base64Util.encodeBase64File(file);
		}
		String senderId = content.getSendOrg();
		//写发送公文的逻辑
		flag = send(targetType, senderId, endpoint, ws, recvOrg, task, content, gwContent);		
		//写dataex_sys_transtask任务表
		//写dataex_sys_trans_account对账单表
		//回写更新dataex_sys_trans表[完成时间和操作状态]
		if (flag.equals(Constants.SUCCESS)) {//这里商定[success]为成功
			task.setSendStatus(Constants.kqueue_success);
			task.setEndTime(Calendar.getInstance().getTime());
			task.setResendTimes(task.getResendTimes() + 1);
			DataexSysTrans in = QueryCache.get(DataexSysTrans.class, content.getTransId());
			in.setTransStatus(Constants.kqueue_success);
			in.setSendTime(Calendar.getInstance().getTime());
			account.setOpStatus(Constants.OP_SUCCESS);
			account.setMemo(Constants._SEND_SUCCESS_MEMO);
			account.setOpTime(in.getSendTime());
			content.setSendTime(in.getSendTime());
			content.setTransStatus(in.getTransStatus());
			updateOpLog(task, in, account, content);
		} else {
			task.setSendStatus(Constants.kqueue_failure);
			task.setEndTime(Calendar.getInstance().getTime());
			task.setResendTimes(task.getResendTimes() + 1);
			DataexSysTrans in = QueryCache.get(DataexSysTrans.class, content.getTransId());
			in.setTransStatus(Constants.kqueue_failure);
			in.setSendTime(Calendar.getInstance().getTime());
			//更新任务队列
//			workQueue.setSendStatus(Constants.kqueue_failure);
//			workQueue.setUpdateTime(new Date());
//			workQueue.setSendTimes(workQueue.getSendTimes() + 1);
			account.setOpStatus(Constants.OP_FAILURE);
			account.setMemo(Constants._SEND_FAILURE_MEMO);
			account.setOpTime(in.getSendTime());
			content.setSendTime(in.getSendTime());
			content.setTransStatus(in.getTransStatus());
			updateOpLog(task, in, account, content);
		}
		return flag;
	}
	//发送公文逻辑
	private String send(String targetType, String senderId, String endpoint, List<DataexSysDirWs> ws, String[] recvOrg, DataexSysTransTask task, DataexSysTransContent transContent, String gwContent) throws Exception {
		System.out.print("交换类型..."+targetType+"..."+transContent.getDocTitle());
		String flag = Constants.FAILURE;
		String msg = "";
		//发文机关
		Identity sender = new Identity();
		sender.setIdentityFlag(transContent.getSendOrg());
		sender.setIdentityName(transContent.getSendOrgName());
		sender.setIdentityDesc(transContent.getSendOrgDesc());
		//收文机关
		Identity receiver = new Identity();
		receiver.setIdentityFlag(recvOrg[0]);
		receiver.setIdentityName(recvOrg[1]);
		receiver.setIdentityDesc(recvOrg[2]);
		//如果收文机关为占号（即机关名称为空号），则直接返回failure
		if(recvOrg[1].contains("空号")){
			dataexsysFlag(receiver, sender, transContent,Constants.INTERRUPT, "该机构单位不存在！");
			return flag;
		}
		if (targetType.equals(Constants.DATAEXSYS)) {
			PkgExAndEx pkg = new PkgExAndEx();
			//发起握手
			//产生随机数
			String randomVal = UuidUtil.getUuid();
			//组装请求数据
			Object[] objs = new Object[]{ senderId, randomVal, "0", Constants.BGN  };
			try {
				ISender client = SenderProvider.getSender(endpoint, getEntity(ws).get("request").getMethodName());
				//请求调用
				String ret = client.request(objs);
				Node node = PkgData.xmlToNode(ret);
				Node resultNode = node.selectSingleNode("/result");
				Node refuseNode = node.selectSingleNode("/refuse");
				if(refuseNode!=null){
					if (_logger.isInfoEnabled()) {
						_logger.info("[request signature(ret is null). ]\n" + ret);
					}
				}else if(resultNode!=null){
					//判断返回值前半部分是否与[随机数]randomVal相等
					String prefix = PkgData.getStringValue(resultNode, "linkCode");
					if (prefix.equals(randomVal)) {//prefix.equals(randomVal)
						//获取令牌
						String token = PkgData.getStringValue(resultNode, "transCode");
						buildDataHeader(pkg,sender,receiver, transContent, token, randomVal, recvOrg);
						buildDataBody(pkg, sender, receiver, task,gwContent);
						msg = pkg.asXML();
//						FileUtil.getFileFromBytes(msg.getBytes(), "D:/pkg_toex.txt");
						//获取令牌成功后发送请求数据
						client = SenderProvider.getSender(endpoint, getEntity(ws).get("send").getMethodName());//Constants.DATAEX_SYS_OPERATIONNAME
						String retdata = client.send(msg);//webservice不会返回null值
						if (StringHelper.isEmptyByTrim(retdata)) {
							if (_logger.isInfoEnabled()) {
								_logger.info("[调用webservice服务失败. ]\n" + retdata);
							}
						}
						try {
							retdata = (String) JSONObject.fromObject(retdata).get("result");
						} catch (Exception e) {
							throw new Exception("[调用webservice服务返回的数据格式不对. ]\n");
						}
						if (retdata.equals(Constants.SUCCESS)) {//交换系统间约定返回[0代表成功][1代表失败][2代表重发]
							flag = Constants.SUCCESS;
						} else if (retdata.equals(Constants.RESEND)) {
							if (_logger.isInfoEnabled()) {
								_logger.info("[请重新签到. ]\n" + retdata);
							}
						} else if (retdata.equals(Constants.FAILURE)) {
							if (_logger.isInfoEnabled()) {
								_logger.info("[发送报文失败. ]\n" + retdata);
							}
						} else {
							if (_logger.isInfoEnabled()) {
								_logger.info("[调用webservice服务失败. ]\n" + retdata);
							}
						}
					} else {
						if (_logger.isInfoEnabled()) {
							_logger.info("[request signature(randomVal is not equal). ]");
						}
					}
				}
			} catch (Exception e) {
				dataexsysFlag(receiver, sender, transContent, Constants.INTERRUPT, e.getMessage());
				if (_logger.isErrorEnabled()) {
//					_logger.error("[请重新签到. ]");
					_logger.error("[调用webservice服务失败. ]", e);
				}
				//修改
				//throw e;
			}
		} else if (targetType.equals(Constants.GWSYS)) {
			PkgOaAndEx pkg = new PkgOaAndEx();
			pkg.setSender(sender);
			List<Identity> receivers = new ArrayList<Identity>();
			receivers.add(receiver);
			pkg.setReceivers(receivers);
			List<Attach> attachs = getAttach(transContent);
			pkg.setAttachs(attachs);
			PkgData content = getContent(sender, receiver, task, gwContent);
			pkg.setContentInstance(content);
			msg = pkg.asXML();
			
//			FileUtil.getFileFromBytes(msg.getBytes(), "D:/pkg_tooa.txt");
			try {
				ISender client = SenderProvider.getSender(endpoint, getEntity(ws).get("send").getMethodName());//Constants.DATAEX_OPERATIONNAME
				String ret = client.send(msg);
				if (ret.equals(Constants.SUCCESS)) {//交换系统间约定返回[0代表成功][1代表失败][2代表重发]
					flag = Constants.SUCCESS;
					dataexsysFlag(receiver, sender, transContent, Constants.ARRIVE, "");
				}else{
					dataexsysFlag(receiver, sender, transContent,Constants.INTERRUPT, "receiver cannot process the request.");
				}
			} catch (Exception e) {
				dataexsysFlag(receiver, sender, transContent, Constants.INTERRUPT, e.getMessage());
				if (_logger.isErrorEnabled()) {
					_logger.error("[invoke target:"+ recvOrg[0] + "," + recvOrg[1] +" failure. ");
				}
			}
		}else if (targetType.equals("3")){
			//发往方正交换,如果发送失败则最后一次返回发送失败的回执
			int recendTimes = task.getResendTimes();
			if("撤销".equals(transContent.getDocTitle())){
				//发送撤销信息
				System.out.print("方正撤销开始。。。");
				String resultStr = DocFzUtil.sendFzCancelXml(endpoint,sender, receiver, task,transContent);
				if (StringHelper.isNotEmpty(resultStr)) {	
					Document redocument = DocumentHelper.parseText(resultStr);
					Element reroot = redocument.getRootElement();
					String code = reroot.element("code").getText();
					if ("0".equals(code)) {
						flag = Constants.SUCCESS;
						dataexsysFlag(receiver, sender, transContent, Constants.ARRIVE, "");
					}else if ("1".equals(code) && recendTimes == 4) {
						dataexsysFlag(receiver, sender, transContent,Constants.INTERRUPT, "方正交换接收失败！");
					}
				}else if(recendTimes == 4){
					dataexsysFlag(receiver, sender, transContent,Constants.INTERRUPT, "调用方正交换服务失败！");
				}				
			}else {
				//发送公文
				String resultStr = DocFzUtil.sendFzXml(endpoint,sender, receiver, task, transContent, gwContent);
				String sendfileid = "";
				if (StringHelper.isNotEmpty(resultStr)) {	
					Document redocument = DocumentHelper.parseText(resultStr);
					Element reroot = redocument.getRootElement();
					String code = reroot.element("code").getText();
					if ("0".equals(code)) {
						sendfileid = reroot.element("sendfileid").getText();
						DocFzUtil.saveFzRelation(sendfileid, sender,receiver, transContent);//保存对应关系
						DocFzUtil.updSuccessData(sendfileid, transContent.getDocId(),sender,task.getStartNum());//更新发件箱数据保存方正发文id
						flag = Constants.SUCCESS;
						//dataexsysFlag(receiver, sender, transContent, Constants.ARRIVE, "");
					}else if ("1".equals(code) && recendTimes == 4) {
						dataexsysFlag(receiver, sender, transContent,Constants.INTERRUPT, "方正交换服务接收失败！");
					}
				}else if(recendTimes == 4){
					dataexsysFlag(receiver, sender, transContent,Constants.INTERRUPT, "调用方正交换服务失败！");
				}
			}					
		}
		return flag;
	}
	
	public static void dataexsysFlag(Identity sender, Identity receiver, DataexSysTransContent transContent,String receiptContent,String receiptMemo) throws Exception{
		try {
			//如果短报文为交换的路由状态,则不再进行回执
			boolean isSysReceipt = false;
			if(Constants.MSG_FEEDBACK.equals(transContent.getPackType())){
				String expansion = transContent.getExpansion();
				if(expansion!=null){
					String[] exps = expansion.split("@");
					//公文或回执
					if(exps.length>0 && (Constants.MSG_DATAPACK.equals(exps[0])
							||Constants.MSG_FEEDBACK.equals(exps[0]) )){
						isSysReceipt = true;
					}
				}
			}
			if(isSysReceipt)return;
			DataexSysDir targetInfo = TaskHelper.findDataexSysDir(receiver);
			DataexSysNode exnode = QueryCache.get(DataexSysNode.class, targetInfo.getExnodeId());
			String targetType = exnode.getExnodeType();
			List<DataexSysDirWs> ws = DataexSysTaskHelper.findDataexSysDirWs(exnode.getExnodeId(),"2");//发送
			if (ws!=null && ws.size() > 0) {
				String endpoint = ws.get(0).getDataServiceUrl();
				if (targetType.equals(Constants.DATAEXSYS)) {
					List<DataexSysDirWs> dw = DataexSysTaskHelper.findDataexSysDirWs(exnode.getExnodeId(),null);
					if(dw != null && dw.size()>0){
						PkgExAndEx pkg = new PkgExAndEx();
						//产生随机数
						String randomVal = UuidUtil.getUuid();
						//组装请求数据
						Object[] objs = new Object[]{ sender.getIdentityFlag(), randomVal, "0", Constants.BGN  };
						ISender client = SenderProvider.getSender(endpoint, getEntity(dw).get("request").getMethodName());
						//请求调用
						String ret = client.request(objs);
						Node node = PkgData.xmlToNode(ret);
						Node resultNode = node.selectSingleNode("/result");
						Node refuseNode = node.selectSingleNode("/refuse");
						if(refuseNode!=null){
							if (_logger.isInfoEnabled()) {
								_logger.info("[request signature(ret is null). ]\n" + ret);
							}
						}else if(resultNode!=null){
							//判断返回值前半部分是否与[随机数]randomVal相等
							String prefix = PkgData.getStringValue(resultNode, "linkCode");
							if (prefix.equals(randomVal)) {//prefix.equals(randomVal)
								//获取令牌
								String token = PkgData.getStringValue(resultNode, "transCode");
								buildDataHeader(pkg,sender,receiver, transContent, token, randomVal, new String[]{receiver.getIdentityFlag()});
								pkg.setMsgAttr(Constants.MSG_FEEDBACK);
								ReceiptContent content = new ReceiptContent();
								content.setDocId(transContent.getDocId());
								content.setReceiptContent(receiptContent);
								content.setReceiptMemo(receiptMemo);
								content.setReceiptTime(new Date());
								IExpansionProcessor processer = ExpansionProcessorFactory.getProcessor();
								String exp = transContent.getExpansion();
								transContent.setExpansion(transContent.getPackType()+"@"+transContent.getDocTitle());
								Expansion expansion = processer.pkg(transContent, null, null);
								transContent.setExpansion(exp);
								content.setExpansion(expansion);
								pkg.setContentInstance(content);
								client = SenderProvider.getSender(endpoint, getEntity(dw).get("send").getMethodName());
								client.send(pkg.asXML());
							}
						}
					}
				} else if (targetType.equals(Constants.GWSYS)) {
					PkgOaAndEx pkg = new PkgOaAndEx();
					pkg.setSender(sender);
					List<Identity> receivers = new ArrayList<Identity>();
					receivers.add(receiver);
					pkg.setReceivers(receivers);
					pkg.setFromMsgFlag(transContent.getDocId());
					ReceiptContent content = new ReceiptContent();
					content.setDocId(transContent.getDocId());
					content.setReceiptContent(receiptContent);
					content.setReceiptMemo(receiptMemo);
					content.setReceiptTime(new Date());
					IExpansionProcessor processer = ExpansionProcessorFactory.getProcessor();
					String exp = transContent.getExpansion();
					transContent.setExpansion(transContent.getPackType()+"@"+transContent.getDocTitle());
					Expansion expansion = processer.pkg(transContent, null, null);
					transContent.setExpansion(exp);
					content.setExpansion(expansion);
					pkg.setContentInstance(content);
					ISender client = SenderProvider.getSender(endpoint, getEntity(ws).get("send").getMethodName());
					client.send(pkg.asXML());
				}	
			}
		} catch (Exception e) {
			_logger.error(" receipt pkg["+transContent.getDocId()+"] "+receiptContent+":"+receiptMemo+" failure. ");
		}
	}
	
	public static List<DataexSysDirWs> findDataexSysDirWs(String dirId, String type) {//接口类型：1、握手   2、发送 
		StringBuffer sb = new StringBuffer("select a.wsId from DataexSysDirWs a where a.dirId=:dirId");
		if(StringHelper.isNotEmpty(type)){
			sb.append(" and a.wsType=:wsType");
		}
		QueryCache qc = new QueryCache(sb.toString()).setParameter("dirId",dirId);
		if(StringHelper.isNotEmpty(type)){
			qc.setParameter("wsType", type);
		}
		List ids = qc.list();
		
		return QueryCache.idToObj(DataexSysDirWs.class, ids);
	}
	
	public static Map<String, DataexSysDirWs> getEntity(List<DataexSysDirWs> ws) {
		Map<String, DataexSysDirWs> mapdata = new HashMap<String, DataexSysDirWs>();
		if(ws!=null){
			for(DataexSysDirWs dirWs : ws){
				if(StringHelper.isNotEmpty(dirWs.getWsType())){
					if(dirWs.getWsType().equals(Constants.WSTYPE_REQ)){
						mapdata.put("request", dirWs);
					}else if(dirWs.getWsType().equals(Constants.WSTYPE_SEND)){
						mapdata.put("send", dirWs);
					}
				}
			}
		}
		return mapdata;
	}
	
	private static void buildDataHeader(PkgExAndEx pkg,Identity sender, Identity receiver, DataexSysTransContent transContent, String token, String identityId, String[] recvOrg) {
		pkg.setMsgSender(sender);
		pkg.setMsgReceiver(receiver);
		pkg.setSendTime(new Date());
		pkg.setTransToken(token);
		pkg.setMsgFlag(identityId);
		EncryptionConf encryptionConf = pkg.getEncryptionConf();
//		encryptionConf.setSecretLevel("");
//		encryptionConf.setSignatureArithmetic("");
//		encryptionConf.setEncrytionArithmetic("");
		if(transContent!=null){
			//备注信息
			String remarkInfo = transContent.getRemark();
			pkg.setMsgRemark(remarkInfo);
			if(Constants.MSG_DATAPACK.equals(transContent.getPackType())){
				pkg.setMsgAttr(Constants.MSG_DATAPACK);
			} else if(Constants.MSG_FEEDBACK.equals(transContent.getPackType())){
				pkg.setMsgAttr(Constants.MSG_FEEDBACK);
			}
			List<Attach> attachs = getAttach(transContent);
			pkg.setAttachs(attachs);
		}
	}
	
	private static void buildDataBody(PkgExAndEx pkg, Identity sender,
			Identity receiver, DataexSysTransTask task,String gwContent) {
		Signature signature = pkg.getSignature();
//		signature.setSignatureBody("");
//		signature.setSignatureName("");
//		signature.setSignatureTime(null);
		PkgData content = getContent(sender, receiver, task, gwContent);
		if(content!=null){
			pkg.setContentInstance(content);
		}
	}
	
	private static PkgData getContent(Identity sender, Identity receiver, DataexSysTransTask task, String gwContent){
		if(task==null)return null;
		DataexSysTransContent transContent = task.getContent();
		PkgData content = null;
		if(Constants.MSG_DATAPACK.equals(transContent.getPackType())){
			content = getGwContent(sender, receiver, task, gwContent);
		} else if(Constants.MSG_FEEDBACK.equals(transContent.getPackType())){
			content = getReceiptContent(sender, receiver, task);
		}
		return content;
	}
	
	private static ReceiptContent getReceiptContent(Identity sender, Identity receiver, DataexSysTransTask task) {
		DataexSysTransContent transContent = task.getContent();
		ReceiptContent content = new ReceiptContent();
		content.setDocId(transContent.getDocId());
		content.setReceiptContent(transContent.getDocTitle());
		content.setReceiptMemo(transContent.getRemark());
		content.setReceiptTime(transContent.getSendTime());
		IExpansionProcessor processer = ExpansionProcessorFactory.getProcessor();
		Expansion expansion = processer.pkg(transContent, task, null);
		content.setExpansion(expansion);
		return content;
	}
	
	private static GwContent getGwContent(Identity sender, Identity receiver, DataexSysTransTask task, String gwContent) {
		DataexSysTransContent transContent = task.getContent();
		String emergency = transContent.getDocEmergency();
		String security = transContent.getDocSecurity();
		String title = transContent.getDocTitle();
		
		GwContent content = new GwContent();
		content.setContent(gwContent);
		content.setEmergency(emergency);
		content.setSecurity(security);
		content.setTitle(title);
//		content.setSender(transContent.getSendOrgName());
//		content.setReceiver(transContent.getReceivOrgName());
		content.setSender(sender.getIdentityName());
		content.setReceiver(receiver.getIdentityName());
		
		IExpansionProcessor processer = ExpansionProcessorFactory.getProcessor();
		Expansion expansion = processer.pkg(transContent, task, null);
		content.setExpansion(expansion);
		
		return content;
	}
	private static List<Attach> getAttach(DataexSysTransContent transContent){
		List<Attach> attachs = new ArrayList<Attach>();
		List<DataexSysDocAttach> attachments = QueryService.getAttacByTransContent(transContent.getUuid());
		if(attachments!=null){
			for (DataexSysDocAttach docAttach : attachments) {
				Attach attach = new Attach();
				attach.setName(docAttach.getFileName());
				attach.setExt(docAttach.getExtName());
				attach.setFlag(docAttach.getUuid());
				attach.setSize(docAttach.getFileSize() + "");
				attach.setOrderBy(docAttach.getOrderBy());
				try {
					attach.setContentPath(Integer.parseInt(docAttach.getServerId()), docAttach.getAttachUrl());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				attachs.add(attach);
			}
		}
		return attachs;
	}
	
	private void updateOpLog(DataexSysTransTask task, DataexSysTrans in, DataexSysTransAccount account, DataexSysTransContent content) {
    	TransactionCache tx = null;
		try {
			tx = new TransactionCache();
			tx.update(task);
			tx.update(in);
			tx.update(account);
			tx.update(content);
			tx.commit();
			if(Constants.kqueue_success.equals(task.getSendStatus())){
				TaskHelper.clearMark(task,content);
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[update trans data error. ]":e.getMessage(), e);
			}
		}
    }
	private void updateFailure(DataexSysTransTask task, DataexSysTrans in) {
    	TransactionCache tx = null;
		try {
			tx = new TransactionCache();
			tx.update(task);
			tx.update(in);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[update trans data error. ]":e.getMessage(), e);
			}
		}
    }
	private void setDataexSysTransAccount(DataexSysTransAccount account, DataexSysTransContent content, DataexSysTransTask task, String opType, String opStatus, String memo) throws Exception {
		String uuid = UuidUtil.getUuid();
		account.setUuid(uuid);
		account.setOpType(opType);
		account.setRelId(task.getUuid());
		account.setMsgType(task.getPackType());
		account.setOpTime(Calendar.getInstance().getTime());//task.getEndTime()说明：成功则为task的完成时间，失败则为操作时间
		account.setOrgCode(task.getTargetOrg());
		account.setOpStatus(opStatus);
		InetAddress netAddress = HostInfo.getInetAddress();
		account.setServerName(TaskHelper.findSendSys(task.getReceiver()));
		account.setServerIp(HostInfo.getHostIP(netAddress));
		account.setMemo(memo);
		account.setTransId(content.getTransId());
		account.setSendId(HostInfo.getHostName(netAddress));
	}

	public static DataexSysTaskHelper getDataexSysTaskHelper(Map<String, Object> map) {
		return new DataexSysTaskHelper(map);
	}
	
	public static void main(String[] args) {
		DictMan dictID = new DictMan();
		System.out.println(dictID.getDictType("dataexsys_d_transtatus", "3").getName());
	}

}
