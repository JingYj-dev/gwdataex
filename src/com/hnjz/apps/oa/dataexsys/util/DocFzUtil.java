package com.hnjz.apps.oa.dataexsys.util;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.base.org.action.OrgItem;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.base.user.model.SUser;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexFzRelation;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDocAttach;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.apps.oa.dataexsys.service.QueryService;
import com.hnjz.apps.oa.dataexsys.service.task.TaskHelper;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//公文发送工具类

public class DocFzUtil {
	 
	 //发送公文
	 public static String sendFzXml(String endpoint,Identity sender, Identity receiver, DataexSysTransTask task, DataexSysTransContent transContent, String gwContent) {
		 String resultStr = "";
		 Document document = null;
			try {
    			
        		//封装公文信息xml		
				document = DocumentHelper.createDocument();
				Element root = document.addElement("archive");
				Element actionNode = root.addElement("action");
				actionNode.addText("sendArchive");
				//发送者
				Element senderNode = root.addElement("sender");
				Element send_objectNode = senderNode.addElement("send_object");
				send_objectNode.setText("org");//发件主体，固定值：org
				
				Element trustorgnoNode = senderNode.addElement("trustorgno");//发送主体机构信任号
				Element trustorgnameNode = senderNode.addElement("trustorgname");//发送主体机构名称
				Element trustusernoNode = senderNode.addElement("trustuserno");//发文经办人唯一编码
				Element trustusernameNode = senderNode.addElement("trustusername");//发文经办人姓名
				Element sendappidNode = senderNode.addElement("appid");//应用系统（发送方）唯一编码	
				
				//从省委发出的公文都是以办公厅名义发出，但经过方正时需要还原为真实的部门
				//String sendCode = sender.getIdentityFlag();
				//String sendorgId = (String) new  QueryCache("select a.uuid from SOrg a where a.code=:code").setParameter("code", sendCode).uniqueResult();
				//SOrg sOrg = OrgItem.getOrg(sendorgId);//对应机关
				//查询发送机构对应系统appid
				//String sendappid = DictMan.getDictName("system_appid", sendCode);
				String sendUserId = transContent.getExpansion();
				SUser sUser = QueryCache.get(SUser.class, sendUserId);
				SOrg sendOrg = OrgItem.getOrg(sUser.getOrganId());//实际机构
				String sendappid = TaskHelper.findDataexSysAppid(sendOrg.getUnitCode());
				System.out.println("发文机构appid为：===="+sendappid);
				/*if(StringHelper.isEmpty(sendappid)){
					sendappid = DictMan.getDictName("system_appid", sendOrg.getUnitCode());
					System.out.println("获取的发送机构appid为："+sendCode +"---"+sendappid);
				}*/
				
				if (sendOrg != null) {
					trustorgnoNode.setText(sendOrg.getRealOrgId()==null?"":sendOrg.getRealOrgId());
					trustorgnameNode.setText(sendOrg.getOrgName()==null?"":sendOrg.getOrgName());
					sendappidNode.setText(sendappid);
				}
				if (sUser != null) {
					trustusernoNode.setText(sUser.getRealUserId()==null?"":sUser.getRealUserId());
					trustusernameNode.setText(sUser.getRealName()==null?"":sUser.getRealName());
				}

				//接收单位
				Element receiversNode = root.addElement("receivers");
				Element orgsNode = receiversNode.addElement("orgs");

				Element orgNode = orgsNode.addElement("org");//接收的单位
				Element orgnoNode = orgNode.addElement("trustorgno");//接收机构信任号
				Element orgnameNode = orgNode.addElement("trustorgname");//接收机构名称 
				Element countNode = orgNode.addElement("count");//打印份数
				Element pnumNode = orgNode.addElement("num");//打印份号区间
				Element recappidNode = orgNode.addElement("appid");//应用系统（接收方）唯一编码	
				
				String recvCode = receiver.getIdentityFlag();
				String recvorgId = (String) new  QueryCache("select a.uuid from SOrg a where a.unitCode=:unitCode").setParameter("unitCode", recvCode).uniqueResult();
				SOrg recvOrg = OrgItem.getOrg(recvorgId);
				//查询接收机构对应系统appid
				//String recvappid = DictMan.getDictName("system_appid", recvCode);
				String recvappid = TaskHelper.findDataexSysAppid(recvCode);
				System.out.println("获取的接收机构appid为："+recvCode+"===="+recvappid);
				int startnum = Integer.parseInt(task.getStartNum());
				int endNum = Integer.parseInt(task.getEndNum());
				String count = String.valueOf(endNum-startnum+1);
				String num = task.getStartNum() +"-"+task.getEndNum();				
				if (recvOrg != null) {
					orgnoNode.setText(recvOrg.getRealOrgId()==null?"":recvOrg.getRealOrgId());
					orgnameNode.setText(recvOrg.getOrgName()==null?"":recvOrg.getOrgName());
					countNode.setText(count);
					pnumNode.setText(num);
					recappidNode.setText(recvappid);			
				}
				
				
				//公文信息
				Element paramsNode = root.addElement("archiveparameters");
				//公文标题
				Element titleNode = paramsNode.addElement("title");
				titleNode.setText(transContent.getDocTitle());
				//文号信息;中软文号信心为空，所以传方正个默认
				Element numNode = paramsNode.addElement("num");
				Element typeNode = numNode.addElement("type");//文号的种类
				typeNode.setText("2");
				Element prefixNode = numNode.addElement("prefix");//文号前缀
				prefixNode.setText("厅文");
				Element symbolNode = numNode.addElement("symbol");//年份
				symbolNode.setText("〔2017〕");
				Element serial_numberNode = numNode.addElement("serial_number");//编号
				serial_numberNode.setText("1");
				
				//公文密级
				Element secrecy_gradeNode = paramsNode.addElement("secrecy_grade");
				String secrecyLevel = transContent.getDocSecurity();
				if("机密".equals(secrecyLevel)){
					secrecy_gradeNode.setText("0");
				}else if("秘密".equals(secrecyLevel)){
					secrecy_gradeNode.setText("1");
				}else{
					secrecy_gradeNode.setText("2");
				}
				//保密期限
				Element effect_dateNode = paramsNode.addElement("effect_date");
				effect_dateNode.setText("");
				//紧急程度
				Element urgency_gradeNode = paramsNode.addElement("urgency_grade");
				urgency_gradeNode.setText(transContent.getDocEmergency());
				//主题词
				Element captionNode = paramsNode.addElement("caption");
				captionNode.setText("");
				//关键字
				Element keywordNode = paramsNode.addElement("keyword");
				keywordNode.setText("");
				//文种；非空传默认
				Element dispatch_typeNode = paramsNode.addElement("dispatch_type");
				dispatch_typeNode.setText("厅文");
				//签发人
				Element sendersNode = paramsNode.addElement("sender");
				sendersNode.setText("");

				//发布层次
				Element sendscopeNode = paramsNode.addElement("sendscope");
				sendscopeNode.setText("");
				//是否加盖电子印章
				Element issignNode = paramsNode.addElement("issign");
				issignNode.setText("false");
				//联系方式
				Element contact_phoneNode = paramsNode.addElement("contact_phone");
				contact_phoneNode.setText("");

				//主送部门
				Element deptmNode = paramsNode.addElement("deptm");
				deptmNode.setText("");
				//抄送部门
				Element deptcNode = paramsNode.addElement("deptc");
				deptcNode.setText("");
				//签发领导
				Element issuerNode = paramsNode.addElement("issuer");
				issuerNode.setText("");
				//签发日期
				Element issue_dateNode = paramsNode.addElement("issue_date");
				issue_dateNode.setText("");
				//经办人
				Element operatorNode = paramsNode.addElement("operator");
				operatorNode.setText("");
				//联系方式
				Element contactNode = paramsNode.addElement("contact");
				contactNode.setText("");
				//发文说明
				Element sendcaptionNode = paramsNode.addElement("sendcaption");
				sendcaptionNode.setText("");
				
				//正文及附件
				//文件信息,正文一个，附件可多个
				Element filesNode = root.addElement("files");
				//String zwInfo = gwContent;
				String zwPath = task.getContentId();
				String dir = AttachItem.filePath(Integer.parseInt(DocConstants.FILE_ID));
				String ofdName = task.getPacketUrl(); 
				String ofdFile = dir+ ofdName;
				File file = new File(ofdFile);
				if (file.exists()) {
					//下载附件到公文交换路径
					DocAttachUtil.downDocAttach(ofdFile,ofdName,zwPath);
					//增加正文节点
					Element fileNode = filesNode.addElement("file");
					Element filenameNode = fileNode.addElement("filename");
					filenameNode.setText(ofdName);
					Element filetypeNode = fileNode.addElement("filetype");
					filetypeNode.setText("document");
				} 
								
				//下载附件
				List<DataexSysDocAttach> attachments = QueryService.getAttacByTransContent(transContent.getUuid());
				for (DataexSysDocAttach fwAttach : attachments) {
					int serverId = Integer.parseInt(fwAttach.getServerId());
					String gwAttachPath = AttachItem.filePath(serverId) + fwAttach.getAttachUrl();
					//下载附件到公文交换路径
					DocAttachUtil.downDocAttach(gwAttachPath,fwAttach.getFileName(),zwPath);
					//增加附件节点
					Element fileNode = filesNode.addElement("file");
					Element filenameNode = fileNode.addElement("filename");
					filenameNode.setText(fwAttach.getFileName());
					Element filetypeNode = fileNode.addElement("filetype");
					filetypeNode.setText("attachment");
				}
				
				String fzXml = document.asXML();				
				//下载公文 正文，附件进行压缩打包
				int serverId = Integer.parseInt(DocConstants.DOWN_DIR_ID);
				String gwexFilesPath = AttachItem.filePath(serverId)+zwPath;
				DocAttachUtil.zipDocAttach(gwexFilesPath,zwPath);
					
				
				//找到公文zip包
				String fileName = DocConstants.DOCZIPNAME+".zip"; //公文压缩包名称
	            int nServerId = Integer.parseInt(DocConstants.SEND_DIR_ID);//发送公文zip包路径  
	            String zipFile = AttachItem.filePath(nServerId)+zwPath+"/" +fileName;
	                 
				//先调用发送机构用户所属交换箱查询接口
				//查询发送机构对应系统appid
	            System.out.println(fzXml);
//				String exDataUrl = DictMan.getDictName("docdataex_address", "1");
				String exAddrXml = GwExAddrService.getGwExAddr("org_query", sendOrg.getRealOrgId(), sendappid);
				String exAddrStr  = DocDataexUtil.docExc(endpoint, exAddrXml);
				String url = "";
				//解析返回信息
				if (StringHelper.isNotEmpty(exAddrStr)) {
					Document redocument = DocumentHelper.parseText(exAddrStr);
					Element root1 = redocument.getRootElement();
					Element jhxNode = root1.element("jhx");
					url = jhxNode.element("url").getText();		
				}
				
				//调用公文接口
				if (StringHelper.isNotEmpty(url)) {		
					resultStr  = DocDataexUtil.sendDoc(url+"/ArchiveHandleServlet", fzXml,zipFile);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultStr;
	}    
     
	public static void saveFzRelation(String fzSendFileId,Identity sender,Identity receiver, DataexSysTransContent transContent){
			DataexFzRelation fzr = new DataexFzRelation();
			String uuid = UuidUtil.getUuid();
			fzr.setUuid(uuid);
			fzr.setFzSendFileId(fzSendFileId);
			fzr.setSendUnitCode(sender.getIdentityFlag());
			fzr.setSendUnitName(sender.getIdentityName());
			fzr.setSendUnitRemark(sender.getIdentityDesc());
			fzr.setZrSendFileId(transContent.getDocId());
			fzr.setDocTitle(transContent.getDocTitle());
			fzr.setReceiverUnitCode(receiver.getIdentityFlag());
			TransactionCache tx = null;
			try {
				tx = new TransactionCache();
				tx.save(fzr);
				tx.commit();
			} catch (Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				e.printStackTrace();
			} 
		}
	
	   //发送成功更新数据
		public static void updSuccessData(String fzSendFileId,String outboxId,Identity sender,String startNum){
			try {
				//组装回执对象
				Map<String,String> map = new HashMap<String,String>();
				map.put("sendField", fzSendFileId);
				map.put("outboxid", outboxId);
				map.put("starNum", startNum);
				map.put("receiptContent", Constants.ARRIVE);
				map.put("receiptName", sender.getIdentityDesc());
				//获取处理系统hession接口对象
				DataexUtil dataexUtil = ServiceUtil.getDataexUtil(sender);
				dataexUtil.updFileId(map); 
				map = null;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
				
		//发送撤销信息
		 public static String sendFzCancelXml(String endpoint,Identity sender, Identity receiver, DataexSysTransTask task, DataexSysTransContent transContent) {
			 String resultStr = "";
			 Document document = null;
				try {
					//封装公文信息xml		
					document = DocumentHelper.createDocument();
					Element root = document.addElement("archive");
					Element actionNode = root.addElement("action");
					actionNode.addText("rePrintStatus");			
					DataexFzRelation fr = ServiceUtil.getFzRelationByGwId(transContent.getDocId(),receiver.getIdentityFlag());//查询关联表
					Element sendfileidNode = root.addElement("sendfileid");//发文唯一id
					if (fr != null) {
						sendfileidNode.addText(fr.getFzSendFileId());
					}					

					//发送机构id
					String sendCode = sender.getIdentityFlag();
					String sendorgId = (String) new  QueryCache("select a.uuid from SOrg a where a.unitCode=:unitCode").setParameter("unitCode", sendCode).uniqueResult();
					String sendRealOrgId = OrgItem.getOrg(sendorgId).getRealOrgId();
					//String sendappid = DictMan.getDictName("system_appid", sender.getIdentityFlag());
					String sendappid = TaskHelper.findDataexSysAppid(sender.getIdentityFlag());
					//接收机构id
					String recvorgId = (String) new  QueryCache("select a.uuid from SOrg a where a.unitCode=:unitCode").setParameter("unitCode", receiver.getIdentityFlag()).uniqueResult();
					String recvRealOrgId = OrgItem.getOrg(recvorgId).getRealOrgId();
					//String recvappid = DictMan.getDictName("system_appid", receiver.getIdentityFlag());
					String recvappid = TaskHelper.findDataexSysAppid(receiver.getIdentityFlag());
					Element appidNode = root.addElement("appid");//接收机构系统唯一编码
					appidNode.addText(recvappid);
					
					Element printsNode = root.addElement("prints");//打印信息
					Element printNode = printsNode.addElement("print");
					
					Element trustorgnoNode = printNode.addElement("trustorgno");//接收机构信任号
					trustorgnoNode.addText(recvRealOrgId);
					Element trustorgnameNode = printNode.addElement("trustorgname");//接收机构名称
					trustorgnameNode.addText(receiver.getIdentityName());
					Element numNode = printNode.addElement("num");//num用来传递撤销标识
					numNode.addText("撤销");
					
					String cancelXml = document.asXML();					
					System.out.print(cancelXml);				
					//先调用发送机构用户所属交换箱查询接口
//					String exDataUrl = DictMan.getDictName("docdataex_address", "1");
					String exAddrXml = GwExAddrService.getGwExAddr("org_query", sendRealOrgId, sendappid);//根据发送机构查询
					String exAddrStr  = DocDataexUtil.docExc(endpoint, exAddrXml);
					String url = "";
					//解析返回信息
					if (StringHelper.isNotEmpty(exAddrStr)) {
						Document redocument = DocumentHelper.parseText(exAddrStr);
						Element root1 = redocument.getRootElement();
						Element jhxNode = root1.element("jhx");
						url = jhxNode.element("url").getText();		
					}
					//调用方正接口
					if (StringHelper.isNotEmpty(url)) {
						resultStr  =  DocDataexUtil.docExc(url+"/ArchiveHandleServlet", cancelXml);
					}
				}  catch (Exception e) {
					e.printStackTrace();
				}

				return resultStr;
		} 
}
