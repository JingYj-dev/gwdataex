package com.hnjz.apps.oa.dataexsys.admin.service;

import com.hnjz.db.query.QueryCache;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysAppid;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;

import java.util.ArrayList;
import java.util.List;

public class DataexSysHelper {
	
	/*public static List getAppListQuery() {
		List dict = getMsgAppList();
		List lstRes = new ArrayList();
		MsgApp msgApp = new MsgApp();
		msgApp.setAppName("全部");
		lstRes.add(msgApp);
		lstRes.addAll(dict);
		return lstRes;
	}
	
	public static List<MsgApp> getMsgAppList() {
		QueryCache qc = new QueryCache("select a.appId from MsgApp a");
		List lst = qc.list();
		List<MsgApp> appLst = QueryCache.idToObj(MsgApp.class, lst);
		return appLst;
	}*/
	
	public static List getDataexSysNodeListQuery() {
		List dict = getDataexSysNodeList();
		List lstRes = new ArrayList();
		DataexSysNode item = new DataexSysNode();
		item.setExnodeName("-请选择-");
		lstRes.add(item);
		lstRes.addAll(dict);
		return lstRes;
	}
	
	public static List<DataexSysNode> getDataexSysNodeList() {
		QueryCache qc = new QueryCache("select a.exnodeId from DataexSysNode a");
		List lst = qc.list();
		List<DataexSysNode> appLst = QueryCache.idToObj(DataexSysNode.class, lst);
		return appLst;
	}
	
	public String getExNodeName(String exnodeId) {
		DataexSysNode exnode = QueryCache.get(DataexSysNode.class, exnodeId);
		return exnode.getExnodeName();
	}
	/**
	 * 获取机构的appid
	 * @param appidId
	 * @return
	 */
	public String getExAppidCode(String appidId) {
		DataexSysAppid exnode = QueryCache.get(DataexSysAppid.class, appidId);
		return exnode.getAppidCode();
	}
	public static List<DataexSysAppid> getDataexSysAppidListQuery() {
		QueryCache qc = new QueryCache("select a.uuid from DataexSysAppid a");
		List<DataexSysAppid> appidLst = QueryCache.idToObj(DataexSysAppid.class, qc.list());
		List<DataexSysAppid> list = new ArrayList<DataexSysAppid>();
		DataexSysAppid item = new DataexSysAppid();
		item.setAppidName("-请选择-");
		list.add(item);
		list.addAll(appidLst);
		return list;
	}
	/*public static void main(String[] args) {
		List lst = getAppListQuery();
		for (Object object : lst) {
			System.out.println(((MsgApp) object).getAppId() + "," + ((MsgApp) object).getAppName());
		}
		List lst1 = getDataexSysNodeListQuery();
		for (Object object : lst1) {
			System.out.println(((DataexSysNode) object).getExnodeId() + "," + ((DataexSysNode) object).getExnodeName());
		}
	}*/

}
