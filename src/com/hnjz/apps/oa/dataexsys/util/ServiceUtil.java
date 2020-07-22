package com.hnjz.apps.oa.dataexsys.util;

import com.caucho.hessian.client.HessianProxyFactory;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexFzRelation;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDirWs;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.apps.oa.dataexsys.service.task.TaskHelper;
import com.hnjz.apps.oa.dataexsys.service.thread.DataexSysTaskHelper;
import com.hnjz.db.query.QueryCache;

import java.net.MalformedURLException;
import java.util.List;

//公文发送工具类

public class ServiceUtil {

	 //根据发送机构查询对应处理hession接口地址
	 public static DataexUtil getDataexUtil(Identity sender) {
		 DataexUtil dataexUtil = null;
		    try {
				//根据机构,通过组织机构编码获取节点，节点信息中有接口地址(hession)
				DataexSysDir dir = TaskHelper.findDataexSysDir(sender);
				String exnodeId = dir.getExnodeId();
				List<DataexSysDirWs> ws = DataexSysTaskHelper.findDataexSysDirWs(exnodeId, "3");//3代表hession
				
				String serverUrl = ws.get(0).getDataServiceUrl();
				System.out.print("节点信息："+serverUrl);
				HessianProxyFactory factory = new HessianProxyFactory();
				dataexUtil = (DataexUtil)factory.create(DataexUtil.class,serverUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return dataexUtil;

	} 
	 
	 public static DataexFzRelation getFzRelation(String fzSendFileId){
			String hsql =  " from DataexFzRelation where fzSendFileId=:fzSendFileId";
			QueryCache cache = new QueryCache(hsql);
			cache.setParameter("fzSendFileId", fzSendFileId);
			return (DataexFzRelation)cache.uniqueResult();
	}
	 
	 public static DataexFzRelation getFzRelationByGwId(String gwFileId,String receiverUnitCode){
			String hsql =  " from DataexFzRelation where zrSendFileId=:zrSendFileId and receiverUnitCode=:receiverUnitCode";
			QueryCache cache = new QueryCache(hsql);
			cache.setParameter("zrSendFileId", gwFileId).setParameter("receiverUnitCode", receiverUnitCode);
			return (DataexFzRelation)cache.uniqueResult();
	}
	 
	//处理标题换行问题
	public static String getTitle(String title){
		if(title.indexOf("newLine") > 0){
			title = title.replace("newLine", "<br/>");
		}
		return title;
	}
     
}
