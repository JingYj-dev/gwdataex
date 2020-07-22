package com.hnjz.apps.oa.dataexsys.admin.service;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDocAttach;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.oa.dataexsys.util.Base64Util;

import java.util.*;

public class ExportOfficialDocPackSysService {


	@SuppressWarnings("unchecked")
	public static Map<String, String> getOrgMapByContentId(String contentId) {
		List<String> idList = null;
		List<DataexSysTransTask> taskList = null;
		QueryCache qc = new QueryCache("select a.uuid from DataexSysTransTask a where a.contentId = :contentId ").setParameter("contentId", contentId);
		idList = qc.list();
		taskList = QueryCache.idToObj(DataexSysTransTask.class, idList);
		Map<String, String> map = null;
		if (taskList != null && taskList.size() > 0) {
			map = new HashMap<String, String>();
			for (DataexSysTransTask task : taskList) {
				map.put(task.getTargetOrg(), task.getTargetOrgName());
			}
		}
		return map;
	}
	
	/**
	 * @Title: sendInbox
	 * @Description: TODO(公文打包方法)
	 * @param sendOrgMap
	 * @param receiveOrgsMap
	 * @param gwmj
	 * @param jjcd
	 * @param gwIds
	 * @param topic
	 * @param type
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String packPackage(DataexSysTransContent dataexSysTransContent) throws Exception {
		Map<String, String> sendOrgMap = new HashMap<String, String>();
		Map<String, String> receiveOrgMap = new HashMap<String, String>();
		String contentId = dataexSysTransContent.getUuid();
		String mj = "";
		String jjcd = "";
		String[] gwIds = null;
		String topic = "";
		String type = "";
		String remark = "";
		String serverId = "";
		if (dataexSysTransContent != null) {
			sendOrgMap.put(dataexSysTransContent.getSendOrg(), DataexSysDirItem.getDirByDirOrg(dataexSysTransContent.getSendOrg()).getDirName());
			receiveOrgMap = getOrgMapByContentId(contentId);
			mj = dataexSysTransContent.getDocSecurity();
			jjcd = dataexSysTransContent.getDocEmergency();
			gwIds = StringHelper.strToArr(dataexSysTransContent.getDocId());
			topic = dataexSysTransContent.getDocTitle();
			type = dataexSysTransContent.getPackType();
			remark = "";
			serverId = dataexSysTransContent.getServerId();
		}
		
		//处理正文
		String gwFilePath = null;
		//处理附件
		List<Map<String, String>> attachList = new ArrayList<Map<String, String>>();
		int nServerId = Integer.parseInt(serverId);
		gwFilePath = AttachItem.filePath(nServerId) + dataexSysTransContent.getDocAddress();
		List<String> uuids = new QueryCache("select a.uuid from DataexSysDocAttach a where a.contentId =:contentId").setParameter("contentId", contentId).list();
		List<DataexSysDocAttach> attachments = QueryCache.idToObj(DataexSysDocAttach.class, uuids);
		for (DataexSysDocAttach docAttach : attachments) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", docAttach.getFileName());
			map.put("ext", docAttach.getExtName());
			map.put("flag", docAttach.getUuid());
			map.put("size", docAttach.getFileSize() + "");
			map.put("content", getAttachContent(docAttach.getServerId(), docAttach.getAttachUrl()));
			attachList.add(map);
		}
		return packPackage(sendOrgMap, receiveOrgMap, mj, jjcd, gwIds, topic, type, attachList, remark, gwFilePath, null);
	}
	/**
	 * @Title: sendInbox
	 * @Description: TODO(取得公文附件内容)
	 * @param serverId
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private static String getAttachContent(String serverId, String url) throws Exception {
		int nServerId = Integer.parseInt(serverId);
		String filepath = AttachItem.filePath(nServerId);
		return Base64Util.encodeBase64File(filepath + url);
	}
	
	/**
	 * 封装公文包
	 * @param sendOrgMap 发件机构信息&nbsp;&nbsp;[2001-A总局]&nbsp;&nbsp;
	 * @param receiveOrgsMap 收件机构集合&nbsp;&nbsp;[2001-A总局，2002-B总局]&nbsp;&nbsp;	
	 * @param gwmj	公文密级
	 * @param jjcd	紧急程度
	 * @param gwPaths	公文正文列表&nbsp;&nbsp;[公文id1-公文1正文路径,公文id2-公文2正文路径]&nbsp;&nbsp;<font size="2" color="red">*&nbsp;非空</font>	
	 * @param extendProperty	备注信息
	 * @param attachments 附件集合&nbsp;&nbsp;[Attach1 UUID-[Attach1Prefix,Attach1Name,Attach1Path],Attach1 UUID-[Attach1Prefix,Attach1Name,Attach1Path]
	 * @return String
	 * @throws Exception 
	 */
	public static String packPackage(Map<String, String> sendOrgMap, Map<String, String> receiveOrgsMap,
			String gwmj, String jjcd, String[] gwIds, String topic, String type, List<Map<String, String>> attachments, 
												String remark, String gwFilePath, String extendProperty) throws Exception{
		//校验数据合法
			//1.sendOrgMap size必须==1
			if(gwIds == null || gwIds.length <= 0){
				throw new Exception("公文列表为空!");
			}
			StringBuffer sb = new StringBuffer("");
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<公文封装包>");
//			Set<String> gwIds = gwPaths.keySet();
//			if(gwIds.size()<gwPaths.size()){
//				throw new Exception("公文列表重复!");
//			}
			for(String gwId : gwIds){
//				String documentPath = gwPaths.get(gwId);
				sb.append("<封装实体>");
				//发文机关段落
				sb.append("<发文机关>");
				String sendOrgId = "";
				String sendOrgName = "";
				if(sendOrgMap != null&&sendOrgMap.size()>0){
					Set<String> keys = sendOrgMap.keySet();
					for(String key : keys){   
						/*if(StringHelper.isEmpty(key)){
						throw new Exception("发件单位标识为空!");
					}*/
						sendOrgName = sendOrgMap.get(key);
						/*if(StringHelper.isEmpty(sendOrgName)){
						throw new Exception("发件单位名称为空!");
					}*/
						sendOrgId = key;
						break;
					} 
				}else{}
					sb.append("<身份标识>" + sendOrgId + "</身份标识>");
					sb.append("<身份名称>" + sendOrgName + "</身份名称>");
					sb.append("<身份描述/>");
				sb.append("</发文机关>");
				String receivedOrgIds = "";
				//收文机关段落
				if(receiveOrgsMap != null && receiveOrgsMap.size() > 0){
					Set<String> receiveOrgIds = receiveOrgsMap.keySet();
					if(receiveOrgIds != null&&receiveOrgIds.size() > 0){
						for(String receiveOrgId:receiveOrgIds){
							String receiveOrgName = "";
							if(StringHelper.isNotEmpty(receiveOrgId)){
								receiveOrgName = receiveOrgsMap.get(receiveOrgId);
								receivedOrgIds += (receiveOrgId+",");
							}
							/*if(StringHelper.isEmpty(receiveOrgId)){
								throw new Exception("收件机关标识为空!");
							}*/
							sb.append("<收文机关>");
								sb.append("<身份标识>" + receiveOrgId + "</身份标识>");
								sb.append("<身份名称>" + receiveOrgName + "</身份名称>");
								sb.append("<身份描述/>");
							sb.append("</收文机关>");
						}
					}
					if(StringHelper.isNotEmpty(receivedOrgIds)&&receivedOrgIds.lastIndexOf(",") == receivedOrgIds.length() - 1){
						receivedOrgIds = receivedOrgIds.substring(0, receivedOrgIds.length() - 1);
					}
				}
				if(StringHelper.isEmpty(gwId)){
					throw new Exception("公文标题为空!");
				}
				sb.append("<公文信息>");
					sb.append("<密级>" + (StringHelper.isNotEmpty(gwmj) ? gwmj : "") + "</密级>");
					sb.append("<紧急程度>" + (StringHelper.isNotEmpty(jjcd) ? jjcd : "") + "</紧急程度>");
					sb.append("<发文机关>" + sendOrgId + "</发文机关>");
					sb.append("<收文机关>" + receivedOrgIds + "</收文机关>");
//					String gwTitle = (String)new QueryCache("select title from TblWfProcessExtendData where gwId='"+gwId+"'").uniqueResult();
					/*String gwTitle = gwTitles.get(gwId);
//					String gwTitle = QueryCache.get(TblDocPost.class, gwId).getGwTitle();
					if(StringHelper.isEmpty(gwTitle)){
						gwTitle="";
					}*/
					sb.append("<公文标题>" + topic + "</公文标题>");
					/*String documentSb = "";
					//documentSb = Base64Util.encodeBase64File(documentPath);
					if(StringHelper.isNotEmpty(documentPath)){
						File zipFile = new File(documentPath);
						if (zipFile.exists()){
							documentSb = Base64Util.encodeBase64File(documentPath);
						 }
					}*/
					
					sb.append("<公文内容>" + Base64Util.encodeBase64File(gwFilePath) + "</公文内容>");
					sb.append("<扩展属性>" + (StringHelper.isNotEmpty(extendProperty)?extendProperty:"") + "</扩展属性>");
				sb.append("</公文信息>");
				sb.append("<附件集>");
				//添加附件集
				appendAttachments(sb, attachments);
				sb.append("</附件集>");
			sb.append("<备注>" + remark + "</备注>");
			sb.append("</封装实体>");
			}
		sb.append("</公文封装包>");
		return sb.toString();
	}
	
	private static void appendAttachments(StringBuffer sb, List<Map<String, String>> attachments) {
		for (Map<String, String> map : attachments) {
			sb.append("<附件>");
			sb.append("<附件名称>" + map.get("name") + "</附件名称>");
			sb.append("<扩展名>" + map.get("ext") + "</扩展名>");
			sb.append("<附件标识>" + map.get("flag") + "</附件标识>");
			sb.append("<附件大小>" + map.get("size") + "</附件大小>");
			sb.append("<附件内容>" + map.get("content") + "</附件内容>");
			sb.append("</附件>");
		}
	}
}
