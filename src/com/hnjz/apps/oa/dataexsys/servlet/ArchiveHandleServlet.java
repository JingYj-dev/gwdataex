package com.hnjz.apps.oa.dataexsys.servlet;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.base.org.action.OrgItem;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexFzRelation;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.oa.dataexsys.util.Base64Util;
import com.hnjz.apps.oa.dataexsys.util.DataexUtil;
import com.hnjz.apps.oa.dataexsys.util.DocConstants;
import com.hnjz.apps.oa.dataexsys.util.ServiceUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

//接收文件交换系统推送公文信息
public class ArchiveHandleServlet extends HttpServlet {
		
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException { 
		doPost(request, response);		 
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//封装返回信息
		Document redocument = DocumentHelper.createDocument();
		Element reroot = redocument.addElement("result");
		Element codeNode = reroot.addElement("code");
		String result = "";//action返回结果
		try {
			request.setCharacterEncoding("UTF-8");
			String reStr = "";

			//方正交换系统调用
			String xmlStr = request.getParameter("xmlstr");
			if (StringHelper.isEmpty(xmlStr)) {
				reStr = "传入xml信息为空，请确认";	
			}
			if (StringHelper.isNotEmpty(reStr)) {
				response.setContentType("text/html;charset=utf-8");
				response.setCharacterEncoding("UTF-8");
				OutputStream out = response.getOutputStream();
				out.write(reStr.getBytes("UTF-8"));
				out.flush();
				out.close();
				return;
			}

			//解析xml，根据action进行各种处理与回执
//			xmlStr = new String(xmlStr.getBytes("ISO8859-1"),"UTF-8");
			System.out.println("方正交换开始推送数据。。。。。");
			System.out.println(xmlStr);
			Document document = DocumentHelper.parseText(xmlStr);
			Element root = document.getRootElement();
			//action
			String actionName = root.element("action").getText();
			if (StringHelper.isNotEmpty(actionName)) {
								
				//根据不同action返回不同信息
				//推送待签收公文
				if ("pushwaitrecarchmsg".equals(actionName)) {	
					String trustorgno = "";
					Element receiversNode = root.element("receivers");
					Element orgsNodes = receiversNode.element("orgs");
					List orgNodes = orgsNodes.elements("org");
					for (Iterator iterator = orgNodes.iterator(); iterator.hasNext();) {
						Element orgElm = (Element) iterator.next();
						trustorgno = orgElm.element("trustorgno").getText();	
						if (StringHelper.isNotEmpty(trustorgno)&&trustorgno.contains("-")) {
							trustorgno = trustorgno.replaceAll("-", "");
						}
					}
					SOrg recvOrg = OrgItem.getOrg(trustorgno);
					if (recvOrg != null) {
						Identity recver = new Identity();
						recver.setIdentityFlag(recvOrg.getUnitCode());
						recver.setIdentityName(recvOrg.getOrgName());
						recver.setIdentityDesc("");
						DataexUtil dataexUtil = ServiceUtil.getDataexUtil(recver);//根据接收机构获取对应处理系统hession接口对象
						result = dataexUtil.getWaitDocInfo(xmlStr);
					}

				}
				//推送签收公文
				if ("pushArchive".equals(actionName)) {
					String trustorgno = "";
					DataexUtil dataexUtil = null;
					Element receiversNode = root.element("receivers");
					Element orgNode = receiversNode.element("org");
					if (orgNode !=null) {
						trustorgno = orgNode.element("trustorgno").getText();
						if (StringHelper.isNotEmpty(trustorgno)&&trustorgno.contains("-")) {
							trustorgno = trustorgno.replaceAll("-", "");
						}
					}
					SOrg recvOrg = OrgItem.getOrg(trustorgno);
					if (recvOrg != null) {
						Identity recver = new Identity();
						recver.setIdentityFlag(recvOrg.getUnitCode());
						recver.setIdentityName(recvOrg.getOrgName());
						recver.setIdentityDesc("");
						dataexUtil = ServiceUtil.getDataexUtil(recver);//根据接收机构获取对应处理系统hession接口对象
					}
					
					//先输出公文zip包，编码后发给处理系统					
					//利用正文uuid名称作为获取压缩包路径
					Element gwfiles = root.element("files");
					String zwPath = "";
					if (gwfiles != null ) {
						List fileNodes = gwfiles.elements("file");
						for (Iterator iterator = fileNodes.iterator(); iterator.hasNext();) {
							Element fileElm = (Element) iterator.next();
							String filename = fileElm.element("filename").getText();
							String filetype = fileElm.element("filetype").getText();
							if ("document".equals(filetype)) {
								//为正文文件，只能有一个
								zwPath = filename.replace(".ofd", "");
							}						
						}			
					}
					
					// 读取request的输入流
					String filename = DocConstants.DOCZIPNAME+".zip";
					DataInputStream is = new DataInputStream(request.getInputStream());
					int zipserverId = Integer.parseInt(DocConstants.RECV_DIR_ID);
					String zipFilePath = AttachItem.filePath(zipserverId) +zwPath+"/"+filename;//输出zip文件路径
					File file = new File(zipFilePath);
					if (file.exists()) {
						file.delete();
					} else {
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
					}
					FileOutputStream os = new FileOutputStream(file);
					byte[] data = new byte[1024];
					int count = -1;
					while ((count = is.read(data, 0, 1024)) != -1) {
						os.write(data, 0, count);
					}
					os.flush();
					data = null;
					is.close();
					os.close();
					
					String zipContent = "";//zip压缩包base64码
					File zipFile = new File(zipFilePath);
					if (zipFile.exists()){
						zipContent = Base64Util.encodeBase64File(zipFilePath);
					}
					//推送zip码给处理系统
					dataexUtil.getDocZip(zipContent,zwPath);
					//推送公文xml给处理系统
					result = dataexUtil.getDocInfo(xmlStr);

				}
								
				//公文回执状态更新
				if ("sendArchiveStatus".equals(actionName)) {
					ExecutorService service = Executors.newFixedThreadPool(5);
					ReceiptThread thread = new ReceiptThread(root,xmlStr);					
					FutureTask<String> task = (FutureTask<String>) service.submit(thread);
					try {						
						result = task.get();
						System.out.print(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}								
				//公文打印信息反馈
				if ("rePrintStatus".equals(actionName)) {									
					Element printsNode = root.element("prints");
					List printNodes = printsNode.elements("print");
					for (Iterator iterator = printNodes.iterator(); iterator.hasNext();) {
						Element printElm = (Element) iterator.next();
						String num = printElm.element("num").getText();	
						//撤销采用打印自定义接口
						if ("撤销".equals(num)) {
							DataexUtil dataexUtil = null;
							String trustorgno = printElm.element("trustorgno").getText();
							if (StringHelper.isNotEmpty(trustorgno)&&trustorgno.contains("-")) {
								trustorgno = trustorgno.replaceAll("-", "");
							}		
							SOrg recvOrg = OrgItem.getOrg(trustorgno);
							if (recvOrg != null) {
								Identity recver = new Identity();
								recver.setIdentityFlag(recvOrg.getUnitCode());
								recver.setIdentityName(recvOrg.getOrgName());
								recver.setIdentityDesc("");
								dataexUtil = ServiceUtil.getDataexUtil(recver);//根据接收机构获取对应处理系统hession接口对象
							}
							result = dataexUtil.getCancelInfo(xmlStr);//调处理撤销公文接口
						}else {
							String sendfileid = root.element("sendfileid").getText();
							DataexFzRelation fr = ServiceUtil.getFzRelation(sendfileid);//查询关联表
							Identity sender = new Identity();
							sender.setIdentityFlag(fr.getSendUnitCode());
							sender.setIdentityName(fr.getSendUnitName());
							sender.setIdentityDesc(fr.getSendUnitRemark());
							DataexUtil dataexUtil = ServiceUtil.getDataexUtil(sender);//根据发文id获取处理系统hession接口对象	
							result = dataexUtil.getPrintInfo(xmlStr);//调处理打印反馈更新接口
						}
					}
				}
				
				if (StringHelper.isNotEmpty(result)) {
					response.setContentType("application/xml;charset=utf-8");
					response.setCharacterEncoding("UTF-8");
					OutputStream out = response.getOutputStream();
					out.write(result.getBytes("UTF-8"));
					out.flush();
					out.close();
					return;
				}
	
			}						
		} catch (Exception e) {
			e.printStackTrace();
			//封装操作失败返回信息
			codeNode.addText("1");
			Element descriptionNode = reroot.addElement("description");
			descriptionNode.addText("操作失败");
			result = redocument.asXML();
			if (StringHelper.isNotEmpty(result)) {
				response.setContentType("application/xml;charset=utf-8");
				response.setCharacterEncoding("UTF-8");
				OutputStream out = response.getOutputStream();
				out.write(result.getBytes("UTF-8"));
				out.flush();
				out.close();
				return;
			}
		}

	}
	
	class ReceiptThread implements Callable<String> {
		private Element root;
		private String xmlStr;

		public ReceiptThread(Element root,String xmlStr) {
			this.root = root;
			this.xmlStr = xmlStr;
		}

		@Override
		public String call() throws Exception {
			String result = "";
			System.out.print("开始执行回执。。。。");
			Thread.sleep(1000);
			synchronized (this) {
				String sendfileid = root.element("sendfileid").getText();
				DataexFzRelation fr = ServiceUtil.getFzRelation(sendfileid);//查询关联表
				Identity sender = new Identity();
				sender.setIdentityFlag(fr.getSendUnitCode());
				sender.setIdentityName(fr.getSendUnitName());
				sender.setIdentityDesc(fr.getSendUnitRemark());
				DataexUtil dataexUtil = ServiceUtil.getDataexUtil(sender);//根据发文id获取处理系统hession接口对象
				result = dataexUtil.getReceiptInfo(xmlStr);
			}
			return result;
		}
	}
	

}
