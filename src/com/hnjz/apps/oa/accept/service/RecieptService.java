package com.hnjz.apps.oa.accept.service;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.core.model.IUser;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDirWs;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.service.ISender;
import com.hnjz.apps.oa.dataexsys.service.SenderProvider;
import com.hnjz.apps.oa.dataexsys.service.task.TaskHelper;
import com.hnjz.apps.oa.dataexsys.service.thread.DataexSysTaskHelper;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecieptService {
	public static void reciept(DataexSysTransContent content, IUser user, String receiptContent, String receiptMemo) throws Exception{
		String sendOrg = content.getSendOrg();
		String recieveOrg = user.getOrganId();
		//根据recvOrg查找接发方的回执服务地址
		DataexSysDir targetInfo = TaskHelper.findDataexSysDir(content.getSender());
		DataexSysNode exnode = QueryCache.get(DataexSysNode.class, targetInfo.getExnodeId());
		List<DataexSysDirWs> ws = DataexSysTaskHelper.findDataexSysDirWs(exnode.getExnodeId(), Constants.WSTYPE_SEND);
		String dataServiceUrl = null;
		DataexSysDirWs dirWs = null;
		if (ws.size() > 0) {
			dirWs = ws.get(0);
			dataServiceUrl = dirWs.getDataServiceUrl();
		}
		if(StringHelper.isNotEmpty(dataServiceUrl)){
			String flag = Constants.FAILURE;
			try {
				String pageXml = packPackage(content, user, receiptContent, receiptMemo);
				ISender sender = SenderProvider.getSender(dirWs.getDataServiceUrl(), dirWs.getMethodName());//Constants.DATAEX_OPERATIONNAME
				String ret = sender.send(pageXml);
				if (ret.equals(Constants.SUCCESS)) {//交换系统间约定返回[0代表成功][1代表失败][2代表重发]
					flag = Constants.SUCCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 封装公文包
	 * <?xml version="1.0" encoding="UTF-8"?>
		<公文封装包>
			<封装实体>
				<发文机关>
					<身份标识>2001</身份标识>
					<身份名称>税务总局</身份名称>
					<身份描述 />
				</发文机关>
				<收文机关>
					<身份标识>2002</身份标识>
					<身份名称>税务总局</身份名称>
					<身份描述 />
				</收文机关>
				<回执信息>
					<公文标识></公文标识>
					<回执内容>签收|拒收</回执内容>
					<扩展属性></扩展属性>
				</回执信息>		 
				<备注></备注>
			</封装实体>
		</公文封装包>
	 * @return String
	 * @throws Exception 
	 */
	public static String packPackage(DataexSysTransContent content,IUser user,String receiptContent,String receiptMemo) throws Exception{
		//校验数据合法
		SOrg sender = QueryCache.get(SOrg.class, user.getOrganId());
	    //当前用户orgid是否在content对应的DataexSysTransTask中
		StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<公文封装包>");
		sb.append("<封装实体>");
		sb.append("<发文机关>");
			sb.append("<身份标识>"+sender.getCode()+"</身份标识>");
			sb.append("<身份名称>"+sender.getName()+"</身份名称>");
			sb.append("<身份描述/>");
		sb.append("</发文机关>");
		sb.append("<收文机关>");
			sb.append("<身份标识>"+content.getSendOrg()+"</身份标识>");
			sb.append("<身份名称>"+content.getSendOrgName()+"</身份名称>");
			sb.append("<身份描述/>");
		sb.append("</收文机关>");
		sb.append("<回执信息>");
			sb.append("<公文标识>"+content.getDocId()+"</公文标识>");
			sb.append("<回执时间>"+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+"</回执时间>");
			sb.append("<回执内容>"+receiptContent+"</回执内容>");
			sb.append("<扩展属性/>");
		sb.append("</回执信息>");
		sb.append("<备注>");
			sb.append(receiptMemo);
		sb.append("</备注>");
		sb.append("</封装实体>");
		sb.append("</公文封装包>");
		return sb.toString();
	}

}
