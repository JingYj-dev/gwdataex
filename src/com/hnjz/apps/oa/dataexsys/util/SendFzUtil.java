package com.hnjz.apps.oa.dataexsys.util;

import com.hnjz.apps.base.org.action.OrgItem;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.apps.oa.dataexsys.service.task.TaskHelper;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

//方正数据xml发送

public class SendFzUtil {

	 //发送签收信息
	 public static String sendXml(String xml, String sendId) {
		 String resultStr = "";
			try {
				//先调用发送机构用户所属交换箱查询接口
				SOrg sendOrg = null;
				if (StringHelper.isNotEmpty(sendId)) {
					sendOrg = OrgItem.getOrg(sendId);
				}
				if (sendOrg != null && StringHelper.isNotEmpty(xml)) {
					//根据机构编码查机构对应系统appid
					//String appid = DictMan.getDictName("system_appid", sendOrg.getUnitCode());
					Document document = DocumentHelper.parseText(xml);
					Element root = document.getRootElement();					
					//先调用发送机构用户所属交换箱查询接口	
//					String exDataUrl = DictMan.getDictName("docdataex_address", "1");

					String trustorgno = root.element("trustorgno").getText();
					if (StringHelper.isNotEmpty(trustorgno)&&trustorgno.contains("-")) {
						trustorgno = trustorgno.replaceAll("-", "");
					}
					SOrg recvOrg = QueryCache.get(SOrg.class, trustorgno);
					//String recvappid = DictMan.getDictName("system_appid", recvOrg.getUnitCode());
					String recvappid = TaskHelper.findDataexSysAppid(recvOrg.getUnitCode());
					Element appidNode = root.addElement("appid");	//添加appid节点			
					appidNode.addText(recvappid);
					String docXml = document.asXML();
					System.out.print("交换封装签收/拒收信息="+docXml);
					
					String exDataUrl = DocDataexUtil.getFzAddr(sendId);
					String exAddrXml = GwExAddrService.getGwExAddr("org_query", recvOrg.getRealOrgId(), recvappid);//根据接收机构查询
					String exAddrStr  = DocDataexUtil.docExc(exDataUrl, exAddrXml);
					String url = "";
					//解析返回信息
					if (StringHelper.isNotEmpty(exAddrStr)) {
						Document redocument = DocumentHelper.parseText(exAddrStr);
						Element root1 = redocument.getRootElement();
						Element jhxNode = root1.element("jhx");
						url = jhxNode.element("url").getText();	
						System.out.println("方正交换箱地址==="+url);
					}
					//调用方正接口
					if (StringHelper.isNotEmpty(url)) {
						resultStr  =  DocDataexUtil.docExc(url+"/ArchiveHandleServlet", docXml);
					}
				}
			}  catch (Exception e) {
				e.printStackTrace();
			}

			return resultStr;
	}    
	 
	  //发送打印信息
	  public static String sendPrintXml(String xml, String sendId,String type) {
			 String resultStr = "";
				try {
					if ("FZ".equals(type)) {
						//先调用发送机构用户所属交换箱查询接口
						SOrg sendOrg = null;
						if (StringHelper.isNotEmpty(sendId)) {
							sendOrg = OrgItem.getOrg(sendId);
						}
						if (sendOrg != null && StringHelper.isNotEmpty(xml)) {
							//根据机构编码查机构对应系统appid
							//String appid = DictMan.getDictName("system_appid", sendOrg.getUnitCode());
							String appid = TaskHelper.findDataexSysAppid(sendOrg.getUnitCode());
							Document document = DocumentHelper.parseText(xml);
							Element root = document.getRootElement();
							Element appidNode = root.addElement("appid");	//添加appid节点			
							appidNode.addText(appid);
							String docXml = document.asXML();
							System.out.print(docXml);
							//先调用发送机构用户所属交换箱查询接口
//							String exDataUrl = DictMan.getDictName("docdataex_address", "1");

							Element printsNode = root.element("prints");
							Element printNode = printsNode.element("print");
							String trustorgno = printNode.element("trustorgno").getText();
							if (StringHelper.isNotEmpty(trustorgno)&&trustorgno.contains("-")) {
								trustorgno = trustorgno.replaceAll("-", "");
							}
							SOrg recvOrg = QueryCache.get(SOrg.class, trustorgno);
							//String recvappid = DictMan.getDictName("system_appid", recvOrg.getUnitCode());
							String recvappid= TaskHelper.findDataexSysAppid(recvOrg.getUnitCode());
							String exDataUrl = DocDataexUtil.getFzAddr(sendId);
							String exAddrXml = GwExAddrService.getGwExAddr("org_query", recvOrg.getRealOrgId(), recvappid);//根据接收机构查询
							String exAddrStr  = DocDataexUtil.docExc(exDataUrl, exAddrXml);
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
								resultStr  =  DocDataexUtil.docExc(url+"/ArchiveHandleServlet", docXml);
							}
						}
					} else if("ZR".equals(type)) {
						//先
						SOrg sendOrg = null;
						if (StringHelper.isNotEmpty(sendId)) {
							String sendorgId = (String) new  QueryCache("select a.uuid from SOrg a where a.code=:code").setParameter("code", sendId).uniqueResultCache();
							sendOrg = OrgItem.getOrg(sendorgId);
						}
						if (sendOrg != null && StringHelper.isNotEmpty(xml)){
							Identity sender = new Identity();
							sender.setIdentityFlag(sendId);
							sender.setIdentityName(sendOrg.getOrgName());
							sender.setIdentityDesc("");
							DataexUtil dataexUtil = ServiceUtil.getDataexUtil(sender);//根据发送机构获取处理系统hession接口对象
							try {
								resultStr = dataexUtil.getZrPrintInfo(xml);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
				}  catch (Exception e) {
					e.printStackTrace();
				}

				return resultStr;
		} 
     
}
