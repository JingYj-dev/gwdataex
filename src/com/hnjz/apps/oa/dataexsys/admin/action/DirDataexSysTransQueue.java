package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransQueue;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysTransParamService;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class DirDataexSysTransQueue extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DirDataexSysTransQueue.class);
	private String docTitle;
	private String sendOrg;
	private String recvOrg;
	private String sendStatus;
	private Date beginStartTime;
	private Date endStartTime;
	private Date beginUpdateTime;
	private Date endUpdateTime;
	private Integer maxSendTimes; //最大发送次数，传到前台用来判断任务是否可启动
	private String sendStatusFail; //发送失败常量，传到前台用来判断任务是否可启动
	private Page page;
	
	public DirDataexSysTransQueue() {
		page = new Page();
		page.setCountField("a.sendId");
	}
	public String adminGo() {
		try {
			trimStr();
			sendStatusFail = Constants.kqueue_failure;
			String maxSendTimesStr = DataexSysTransParamService.getParamValue(Constants.MAX_SEND_TIMES_PARAM_ID);
			if (StringHelper.isNotEmpty(maxSendTimesStr)) {
				maxSendTimes = Integer.parseInt(maxSendTimesStr);
			} else {
				setMessage(Messages.getString("systemMsg.notStartQueueWithNoParam", new String[]{Constants.MAX_SEND_TIMES_PARAM_ID.toString()}));
				return Action.ERROR;
			}
			QueryCache qc = new QueryCache(" select a.sendId from DataexSysTransQueue a " + getWhere() + getOrder());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(DataexSysTransQueue.class, page.getResults()));
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1 = 1 ");
		if (StringHelper.isNotEmpty(docTitle)) {
			sb.append(" and a.title like :docTitle ");
		}
		if (StringHelper.isNotEmpty(sendOrg)) {
			sb.append(" and a.sendOrg like :sendOrg ");
		}
		if (StringHelper.isNotEmpty(recvOrg)) {
			sb.append(" and a.recvOrg like :recvOrg ");
		}
		if (StringHelper.isNotEmpty(sendStatus)) {
			sb.append(" and a.sendStatus = :sendStatus ");
		}
		if (beginStartTime != null) {
			sb.append(" and a.startTime >= :beginStartTime ");
		}
		if (endStartTime != null) {
			sb.append(" and a.startTime < :endStartTime ");
		}
		if (beginUpdateTime != null) {
			sb.append(" and a.updateTime >= :beginUpdateTime ");
		}
		if (endUpdateTime != null) {
			sb.append(" and a.updateTime < :endUpdateTime ");
		}
		return sb.toString();
	}

	public void setWhere(QueryCache qc) {
		if (StringHelper.isNotEmpty(docTitle)) {
			qc.setParameter("docTitle", "%" + docTitle + "%");
		}
		if (StringHelper.isNotEmpty(sendOrg)) {
			qc.setParameter("sendOrg", "%" + sendOrg + "%");
		}
		if (StringHelper.isNotEmpty(recvOrg)) {
			qc.setParameter("recvOrg", "%" + recvOrg + "%");
		}
		if (StringHelper.isNotEmpty(sendStatus)) {
			qc.setParameter("sendStatus", sendStatus);
		}
		if (beginStartTime != null) {
			qc.setParameter("beginStartTime", beginStartTime);
		}
		if (endStartTime != null) {
			qc.setParameter("endStartTime", DateUtil.addDate(endStartTime, 1));
		}
		if (beginUpdateTime != null) {
			qc.setParameter("beginUpdateTime", beginUpdateTime);
		}
		if (endUpdateTime != null) {
			qc.setParameter("endUpdateTime", DateUtil.addDate(endUpdateTime, 1));
		}
	}
	private void trimStr() {
		docTitle = StringHelper.isNotEmpty(docTitle) ? docTitle.trim() : "";
		sendOrg = StringHelper.isNotEmpty(sendOrg) ? sendOrg.trim() : "";
		recvOrg = StringHelper.isNotEmpty(recvOrg) ? recvOrg.trim() : "";
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by a.updateTime ";
	}
	public String getDocTitle() {
		return docTitle;
	}
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	public String getSendOrg() {
		return sendOrg;
	}
	public void setSendOrg(String sendOrg) {
		this.sendOrg = sendOrg;
	}
	public String getRecvOrg() {
		return recvOrg;
	}
	public void setRecvOrg(String recvOrg) {
		this.recvOrg = recvOrg;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public Date getBeginStartTime() {
		return beginStartTime;
	}
	public void setBeginStartTime(Date beginStartTime) {
		this.beginStartTime = beginStartTime;
	}
	public Date getEndStartTime() {
		return endStartTime;
	}
	public void setEndStartTime(Date endStartTime) {
		this.endStartTime = endStartTime;
	}
	public Date getBeginUpdateTime() {
		return beginUpdateTime;
	}
	public void setBeginUpdateTime(Date beginUpdateTime) {
		this.beginUpdateTime = beginUpdateTime;
	}
	public Date getEndUpdateTime() {
		return endUpdateTime;
	}
	public void setEndUpdateTime(Date endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Integer getMaxSendTimes() {
		return maxSendTimes;
	}
	public void setMaxSendTimes(Integer maxSendTimes) {
		this.maxSendTimes = maxSendTimes;
	}
	public String getSendStatusFail() {
		return sendStatusFail;
	}
	public void setSendStatusFail(String sendStatusFail) {
		this.sendStatusFail = sendStatusFail;
	}
}
