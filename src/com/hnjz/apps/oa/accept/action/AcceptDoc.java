package com.hnjz.apps.oa.accept.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.oa.accept.model.DataexSysReceipt;
import com.hnjz.apps.oa.accept.service.RecieptService;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.admin.action.UpdDirStatus;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class AcceptDoc extends AdminAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(UpdDirStatus.class);
	private String  ids;
	private String status;
	private String receiptMemo="";
	
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			List<String> listIds = StringHelper.strToList(ids);
			List<DataexSysReceipt> receipts = new ArrayList<DataexSysReceipt>();
			List<DataexSysTransTask> tasks = QueryCache.idToObj(DataexSysTransTask.class, listIds);
			if(StringHelper.isEmpty(status) || (!Constants.ACCEPT_YES.equals(status)&&!Constants.ACCEPT_NO.equals(status)) ){
				status = Constants.ACCEPT_YES;
			}
			tx = new TransactionCache();
			for(DataexSysTransTask task : tasks){
				String receiptContent = "";
				if(Constants.ACCEPT_YES.equals(status)){
					receiptContent = "签收";
				}else{
					receiptContent = "退回";
				}
				DataexSysTransContent content = QueryCache.get(DataexSysTransContent.class,task.getContentId());
				task.setReceiptStatus(status);
					
				DataexSysReceipt receipt = addDataexSysReceipt(task, receiptContent);
				receipts.add(receipt);
				
				RecieptService.reciept(content, sUser,receiptContent, receiptMemo);
			}
			tx.update(tasks);
			tx.save(receipts);
			tx.commit();
			for(DataexSysTransTask task : tasks){
				LogPart lp = new LogPart();		
				lp.setOpObjType(DataexSysTransTask.class.getName());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setOpObjId(task.getUuid());
				lp.setLogData(Json.object2json(task));
				lp.setResult(result);
				lp.save();
			}
			for(DataexSysReceipt receipt : receipts){
				LogPart lp = new LogPart();		
				lp.setOpObjType(DataexSysReceipt.class.getName());
				lp.setOpType(LogConstant.LOG_TYPE_ADD);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setOpObjId(receipt.getUuid());
				lp.setLogData(Json.object2json(receipt));
				lp.setResult(result);
				lp.save();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	
	private DataexSysReceipt addDataexSysReceipt(DataexSysTransTask task,String receiptContent){
		DataexSysReceipt receipt = new DataexSysReceipt();
		receipt.setUuid(UuidUtil.getUuid());
		receipt.setSendId(task.getUuid());
		receipt.setContentId(task.getContentId());
		SOrg sender = QueryCache.get(SOrg.class, sUser.getOrganId());
		receipt.setSendOrg(sender.getCode());
		receipt.setSendOrgName(sender.getName());
		receipt.setTargetOrg(task.getTargetOrg());
		receipt.setTargetOrgName(task.getTargetOrgName());
		receipt.setReceiptContent(receiptContent);
		receipt.setReceiptMemo(receiptMemo);
		receipt.setReceiveTime(new Date());
		receipt.setSenderId(sUser.getUserId());
		receipt.setSenderName(sUser.getRealName());
		receipt.setSendStatus("1");
		return receipt;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceiptMemo() {
		return receiptMemo;
	}

	public void setReceiptMemo(String receiptMemo) {
		this.receiptMemo = receiptMemo;
	}

}
