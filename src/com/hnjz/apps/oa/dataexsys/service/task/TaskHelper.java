package com.hnjz.apps.oa.dataexsys.service.task;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.oa.dataexsys.admin.model.*;
import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.common.HostInfo;
import com.hnjz.apps.oa.dataexsys.pkg.model.Attach;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.apps.oa.dataexsys.pkg.model.PkgData;
import com.hnjz.apps.oa.dataexsys.service.impl.dataex.DataexProcessor;
import com.hnjz.apps.oa.dataexsys.service.impl.dataexsys.DataexsysProcessor;
import com.hnjz.apps.oa.dataexsys.service.thread.DataexSysTaskHelper;
import com.hnjz.apps.oa.dataexsys.service.thread.InitProducerComponent;
import com.hnjz.apps.oa.dataexsys.service.thread.StateMapUtil;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.StringHelper;
import com.hnjz.util.FileUtil;
import com.hnjz.util.UuidUtil;
import com.hnjz.apps.oa.dataexsys.service.QueryService;
import com.hnjz.apps.oa.dataexsys.util.DataexsysConfiguration;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.net.InetAddress;
import java.util.*;

public class TaskHelper {
	
	public static void startFileWorkerThread(String data) {
		//开启线程记录失败数据
		InitProducerComponent._threadPool.submit(new RecordFailDataTask(data));
	}
	public static void startDBWorkerThread(PkgData data, DataexProcessor handler, DataexSysTrans original) {
		//开启线程保存数据
		InitProducerComponent._threadPool.submit(new DataExDBTask(data, handler, original));
	}
	public static void startDBSYSWorkerThread(PkgData data, DataexsysProcessor processor, ClientInfo clientInfo) {
		//开启线程保存数据
		InitProducerComponent._threadPool.submit(new DataExDBSYSTask(data, processor, clientInfo));
	}
	
	public static String immediateProcess(DataexSysTransTask task) throws Exception {
		//封装Map任务
		Map<String, Object> taskMap = new HashMap<String, Object>();
		//根据recvOrg查找接收方服务地址
		DataexSysDir dir = TaskHelper.findDataexSysDir(task.getReceiver());
		//存储处理后公文内容
		DataexSysTransContent content = QueryCache.get(DataexSysTransContent.class, task.getContentId());
		taskMap.put("targetInfo", dir);
		taskMap.put("gwContent", content);
		taskMap.put("recvOrg", new String[]{ task.getTargetOrg(), task.getTargetOrgName(), task.getTargetOrgDesc()});
		taskMap.put("kqueue", task);
		return DataexSysTaskHelper.getDataexSysTaskHelper(taskMap).process();		
	}
	
	public static void setOriginalData(DataexSysTrans original, String request, String sendType,String packType, ClientInfo clientInfo,Identity sender) throws Exception {//TransactionCache tx, 
		String uuid = UuidUtil.getUuid();
		String fileName = uuid + Constants.GW_ENTITY_SUFFIX;
		String backupFlag = ConfigurationManager.getConfigurationManager().getSysConfigure("app.backup", "false");
		if("true".equals(backupFlag)){
			Integer nServerId = Integer.parseInt(Constants.RECV_SYS_DIR_ID);
			String filepath = AttachItem.filePath(nServerId);
			String sFile = filepath + fileName;
			File file = new File(sFile);
			FileUtil.mkfile(file);
			FileUtil.getFileFromBytes(request.getBytes(Constants.charset), sFile);
		}
		original.setUuid(uuid);
		original.setSendSysId(sender.getIdentityFlag());
		original.setSendSysName(sender.getIdentityName());
		original.setSendSysDesc(sender.getIdentityDesc());
		original.setSendSysType(sendType);
		original.setRecvTime(new Date());
		original.setPackType(packType);
		original.setServerId(Constants.RECV_SYS_DIR_ID);
		original.setDocUrl(fileName);
		original.setTransStatus(Constants.kqueue_received);//已接收
		original.setPackSize(request.getBytes(Constants.charset).length);//单位字节
		original.setSourceType(Constants.TYPE_SYS);
//		original.setClientIp(getClientIp(clientInfo));
		original.setRecvStartTime(getRecvStartTime(clientInfo));
	}
	
	
	public static void setDataexSysTransTaskNumByBean(DataexSysTransTask task,String receiver, Map<String, Map<String, String>> nums){
		if(nums!=null){
			Map<String, String> num = (Map<String, String>)nums.get(receiver);
			String startNum = num.get("startNum");
			if(StringHelper.isNotEmpty(startNum)){
				task.setStartNum(startNum);
			}
			String endNum = num.get("endNum");
			if(StringHelper.isNotEmpty(endNum)){
				task.setEndNum(endNum);
			}
		}
	}
	
	
	/**
	 * @Title: checkSignTimeout
	 * @Description: TODO(描述方法功能:检查签到时间是否超时)
	 * @param sign
	 * @return
	 */
	public static boolean checkSignTimeout(DataexSysSign sign) {
		if ((Calendar.getInstance().getTime().getTime() - sign.getLastDataexTime().getTime()) > sign.getTimeOut()) {
			return true;
		}
		return false;
	}
	
	
	
	public static Date getRecvStartTime(ClientInfo clientInfo) {
		if(clientInfo==null)return null;
		return clientInfo.getStartTime();
	}
	public static String getClientIp(ClientInfo clientInfo) {
		if(clientInfo==null)return "";
		return clientInfo.getRequest().getRemoteAddr();
	}
	
	public static String[] strToArray(String str){
		if(StringHelper.isNotEmpty(str)){
			return str.split(":");
		}
		return null;
	}
	
	public static DataexSysDir findDataexSysDir(Identity receiver) throws Exception{
		String mode = DataexsysConfiguration.getReceiverMatchMode();
		DataexSysDir dir = null;
		try {
			if(mode.equals("2")){
				Object id = new QueryCache("select a.uuid from DataexSysDir a where a.dirOrg like :dirOrg")
				.setParameter("dirOrg",receiver.getIdentityFlag()+"%").setMaxResults(1).uniqueResult();
				dir = QueryCache.get(DataexSysDir.class, id.toString());
			}else if(mode.equals("3")){
				Object id = new QueryCache("select a.uuid from DataexSysDir a where a.dirOrg=:dirOrg")
				.setParameter("dirOrg",receiver.getIdentityDesc()).setMaxResults(1).uniqueResult();
				dir = QueryCache.get(DataexSysDir.class, id.toString());
			}else{
				Object id = new QueryCache("select a.uuid from DataexSysDir a where a.dirOrg=:dirOrg")
				.setParameter("dirOrg",receiver.getIdentityFlag()).setMaxResults(1).uniqueResult();
				dir = QueryCache.get(DataexSysDir.class, id.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(dir==null){
				throw new Exception("dataexdir[" + receiver.getIdentityFlag() + "]["
						+ receiver.getIdentityName() + "]["
						+ receiver.getIdentityDesc()
						+ "] can not be found with mode[" + mode + "].");
			}
		}
		return dir;
	}
	
	public static void setDataexSysTransTask(DataexSysTransTask task, DataexSysTransContent content, String status, Identity receiver, DataexSysDir targetInfo) throws Exception {
		String uuid = UuidUtil.getUuid();
		task.setUuid(uuid);
		task.setTransId(content.getTransId());
		task.setContentId(content.getUuid());
//		task.setIdentityId(targetInfo.getUuid());
		task.setPacketUrl(content.getDocAddress());
		task.setServerId(content.getServerId());
		task.setPackType(content.getPackType());
		task.setSendOrg(content.getSendOrg());
		task.setSendOrgName(content.getSendOrgName());
		task.setSendOrgDesc(content.getSendOrgDesc());
		task.setTargetOrg(receiver.getIdentityFlag());
		task.setTargetOrgName(receiver.getIdentityName());
		task.setTargetOrgDesc(receiver.getIdentityDesc());
		if(task.getResendTimes()==null){
			task.setResendTimes(0);//初始值设为0次
		}
		task.setSendStatus(status);
		task.setStartTime(Calendar.getInstance().getTime());
	}
	
	public static boolean setOriginalAndAccount(DataexSysTrans original, Identity sender, DataexSysTransAccount account, String opType) throws Exception {
		//更新[交易日志表]
		updateInData(account, original, opType, Constants.OP_SUCCESS, Constants._RECV_SUCCESS_MEMO);
		boolean flag = false;
		original.setSendSysId(sender.getIdentityFlag());
		original.setSendSysName(sender.getIdentityName());
		original.setSendSysDesc(sender.getIdentityDesc());
		flag = true;
		return flag;
	}
	
	public static void updateInData(DataexSysTransAccount account, DataexSysTrans orginal, String opType, String opStatus, String memo) throws Exception {
		String uuid = UuidUtil.getUuid();
		account.setUuid(uuid);
		account.setRelId(orginal.getUuid());
		account.setMsgType(orginal.getPackType());
		account.setOpType(opType);//[状态]已接收//Constants.STATUS_RECV
		account.setOpStatus(opStatus);
		account.setOpTime(orginal.getRecvTime());
		account.setOrgCode(orginal.getSendSysId());
		InetAddress netAddress = HostInfo.getInetAddress();
		account.setServerName(HostInfo.getHostName(netAddress));
		account.setServerIp(HostInfo.getHostIP(netAddress));
		account.setMemo(memo);
		account.setTransId(orginal.getUuid());
		account.setSendId(TaskHelper.findSendSys(orginal.getSender()));
//		account.setRecvId(recvId);
	}
	
	public static void saveDataexSysDocAttach(DataexSysTransContent content, TransactionCache tx, List<Attach> gwAttachs) throws Exception {
		for(Attach gwAttach : gwAttachs){
			DataexSysDocAttach attach = new DataexSysDocAttach();
			String uuid = UuidUtil.getUuid();
			String fileName = uuid + "." + gwAttach.getExt();
			//存储附件
			Integer nServerId = Integer.parseInt(Constants.RECV_SYS_DIR_ID);
			String filepath = AttachItem.filePath(nServerId);
			//创建文件
			filepath += fileName;
			FileUtil.mkfile(filepath);
			//写入请求数据
			String fileContent = gwAttach.getContent();
			FileUtil.getFileFromBytes(new BASE64Decoder().decodeBuffer(fileContent), filepath);
			
			attach.setUuid(uuid);
			attach.setContentId(content.getUuid());
			attach.setDocId(content.getDocId());
			attach.setFileName(gwAttach.getName());
			attach.setExtName(gwAttach.getExt());
			String sFileSize = gwAttach.getSize();
			attach.setFileSize((sFileSize == null || "".equals(sFileSize.trim()) || "null".equals(sFileSize.trim())) ? 0 : Integer.parseInt(sFileSize));
			attach.setAttachMar(gwAttach.getFlag());
			attach.setAttachUrl(fileName);
			attach.setServerId(Constants.RECV_SYS_DIR_ID);
			attach.setOrderBy(gwAttach.getOrderBy());
			tx.save(attach);
		}
	}
	
	public static List<String> getUndoTasksByContent(String transContentId){
		QueryCache qc = new QueryCache(" select uuid from DataexSysTransTask where contentId=:contentId and (sendStatus=:receiveStatus or sendStatus=:failureStatus) ")
				.setParameter("contentId", transContentId)
				.setParameter("receiveStatus", Constants.kqueue_received)
				.setParameter("failureStatus", Constants.kqueue_failure);
		List<String> list = qc.list();
		return list;
	}
	
	/**
	 * 清除痕迹
	 * 按content查找所有task，如果task都成功执行，则清除痕迹
	 * @param task 如果task不为null，则当前task将视为成功执行
	 * @param transContent  不能为空
	 */
	public static void clearMark(DataexSysTransTask task,DataexSysTransContent transContent){
		if(transContent==null)return;
		if(task!=null){
			StateMapUtil.getInstance().set(StateMapUtil.getMd5Key(task.getUuid()), Constants.kqueue_success);
		}
		String backupFlag = ConfigurationManager.getConfigurationManager().getSysConfigure("app.backup", "false");
		if(!"true".equals(backupFlag)){
			List<String> taskIds = getUndoTasksByContent(transContent.getUuid());
			if(taskIds==null)return;
			int i = 0 ;
			for(String taskId : taskIds){
				String state = StateMapUtil.getState(taskId);
				if(!Constants.kqueue_success.equals(state)){
					break;
				}
				 i++;
			}
			if(taskIds.size()!=i)return;
			try {
				//clear attachs
				List<DataexSysDocAttach> attachments = QueryService.getAttacByTransContent(transContent.getUuid());
				if(attachments!=null){
					for (DataexSysDocAttach docAttach : attachments) {
						String attachFilePath = AttachItem.filePath( Integer.parseInt(docAttach.getServerId()))+docAttach.getAttachUrl();
						FileUtil.delFile(attachFilePath);
					}
				}
				//clear zw_attach
				if(StringHelper.isNotEmpty(transContent.getServerId(),transContent.getDocAddress())){
					String zwFilePath = AttachItem.filePath( Integer.parseInt(transContent.getServerId()))+transContent.getDocAddress();
					FileUtil.delFile(zwFilePath);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String findSendSys(Identity receiver) throws Exception{
		DataexSysDir dir = TaskHelper.findDataexSysDir(receiver);
		if(dir==null)return "";
		DataexSysNode data = QueryCache.get(DataexSysNode.class, dir.getExnodeId());
		if(data==null)return "";
		return data.getExnodeName();
	}
	
	
	 
	public static boolean checkOrg(String orgId) throws Exception {
		List lst = new QueryCache("select a.uuid from DataexSysDir a where a.dirOrg=:dirOrg").setParameter("dirOrg",orgId).list();
		return !lst.isEmpty();
	}
	/**
	 * 根据机构三位编码获取appid
	 */
	public static String findDataexSysAppid(String deptCode){
		Object id = new QueryCache("select b.appidCode from DataexSysDir a, DataexSysAppid b where a.appid = b.uuid and a.dirOrg = :deptCode ")
				.setParameter("deptCode", deptCode).setMaxResults(1).uniqueResult();
		if(id == null){
			return "";
		}
		return id.toString();
	}
}
