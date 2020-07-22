/*
 * Created on 2005-7-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransAccount;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.admin.service.ExportOfficialDocPackSysService;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsys.common.HostInfo;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mazhh TODO To change the template for this generated type
 *         group go to Window - Preferences - Java - Code Style - Code Templates
 */
public class ExportOfficialDocPackSys extends AdminAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ExportOfficialDocPackSys.class);
	private String ids;
	private String downloadFileName;
	private String xmlContent;
	private boolean flag = false;
	public ExportOfficialDocPackSys() {
		
	}
	
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		List<DataexSysTransAccount> accountList = null;
		try{
			String contentId = (String)new QueryCache("select a.uuid from DataexSysTransContent a where a.transId =:transId ").
				setParameter("transId", ids).setMaxResults(1).uniqueResult();
			DataexSysTransContent content = QueryCache.get(DataexSysTransContent.class, contentId);
			//打包
			xmlContent = ExportOfficialDocPackSysService.packPackage(content);
//			log.info(xmlContent);
			flag = true;
			downloadFileName = content.getDocTitle() + ".xml";
			downloadFileName = new String(downloadFileName.getBytes("utf8"), "iso8859-1");
			tx = new TransactionCache();
			accountList = new ArrayList<DataexSysTransAccount>();
			setTransAndAccount(content, accountList); //本地导出添加交换流水日志表记录
			tx.save(accountList);
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			String msg = ex.getMessage();
			result = Ajax.xmlResult(IServiceResult.RESULT_ERROR, msg);
			downloadFileName = "文件不存在.txt";
			try {
				downloadFileName = new String(downloadFileName.getBytes("utf8"), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				if (log.isErrorEnabled()) {
					log.error(e.getMessage(), e);
				}
			} 
			return Action.SUCCESS;
		}
	}
	/**
	* @Title: 本地导出添加交换流水日志表记录
	* @Description: 
	* @throws Exception
	*/
    @SuppressWarnings("unchecked")
	public void setTransAndAccount(DataexSysTransContent content, List<DataexSysTransAccount> accountList) throws Exception {
    	List<String> sendIdList = new QueryCache("select a.uuid from DataexSysTransTask a where a.contentId =:contentId ").
		setParameter("contentId", content.getUuid()).list();
		List<DataexSysTransTask> taskList = QueryCache.idToObj(DataexSysTransTask.class, sendIdList);
		DataexSysTransAccount account = null;
		if (taskList != null && taskList.size() > 0) {
			InetAddress netAddress = HostInfo.getInetAddress();
			//在流水日志表中添加记录
			for (DataexSysTransTask task : taskList) {
				account = new DataexSysTransAccount();
				String accountUUid = UuidUtil.getUuid();
				account.setUuid(accountUUid);
				account.setMemo(content.getRemark());
				account.setMsgType(Constants.MSG_DATAPACK); //1 公文包 2 回执
				account.setOpStatus(Constants.OP_FAILURE);// 操作状态 1 成功 2 失败
				account.setOpTime(new Date());
				account.setOpType(Constants.ACCOUNT_EXP);//导出
				account.setOrgCode(task.getTargetOrg());
				account.setRelId(task.getUuid()); //任务表记录的主键
				account.setServerIp(HostInfo.getHostIP(netAddress));
				account.setServerName(HostInfo.getHostName(netAddress));
				accountList.add(account);
			}
		}
    }
	/**
	 * 获取文件名称
	 * @return
	 */
	public String getDownloadFileName () {
        return downloadFileName;     
    }
	/**
	 * 获取文件流
	 * @return
	 * @throws Exception
	 */
	public InputStream getInputStream () throws Exception {     
		InputStream is = null;     
		if (flag) {
			is = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
		} else {
			is = new ByteArrayInputStream("文件不存在!".getBytes("UTF-8"));
		}     
        return is; 
    }
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	
}
