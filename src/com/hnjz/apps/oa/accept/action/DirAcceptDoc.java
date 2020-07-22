package com.hnjz.apps.oa.accept.action;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.apps.oa.accept.model.DataexSysTransContentVo;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
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
public class DirAcceptDoc extends AdminAction {
	private static Log log = LogFactory.getLog(DirAcceptDoc.class);

	private Page page;
	// 查询项
	private String docTitle;
	private Date startRecvTime;
	private Date endRecvTime;
	
	private String acceptStatus = Constants.ACCEPT_NO;
	
	
	public DirAcceptDoc() {
		page = new Page();
		page.setCountField("a.uuid");
	}

	public String adminGo() {
		try {
			QueryCache qc = new QueryCache(" select a.uuid,a.contentId from DataexSysTransTask a,DataexSysTransContent b "
					+ getWhere() + getOrder());
			setWhere(qc);
			page = qc.page(page);
			loadPage();
			return Action.SUCCESS;
		} catch (Exception ex) {
			String msg = Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			setMessage(msg);
			return Action.ERROR;
		}
	}
	
	private void loadPage(){
		List<DataexSysTransContentVo> vos = new ArrayList<DataexSysTransContentVo>();
		List<Object[]> values = page.getResults();
		if(values!=null){
			for(Object[] value : values){
				String sendId = (String)value[0];
				DataexSysTransTask task = QueryCache.get(DataexSysTransTask.class, sendId);
				String contentId = (String)value[1];
				DataexSysTransContent content = QueryCache.get(DataexSysTransContent.class, contentId);
				DataexSysTransContentVo vo = new DataexSysTransContentVo();
				vo.init(content);
				vo.setSendId(sendId);
				vo.setStartNum(task.getStartNum());
				vo.setEndNum(task.getEndNum());
				
				vos.add(vo);
			}
		}
		page.setResults(vos);
	}
	
	private String getOrgCode(){
		String orgId = this.sUser.getOrganId();
		if(StringHelper.isEmpty(orgId)){return "";}
		SOrg org = QueryCache.get(SOrg.class, orgId);
		if(org==null){return "";}
		String orgCode = org.getCode();
		if(StringHelper.isEmpty(orgCode)){return "";}
		return orgCode;
	}

	protected String getWhere() {
		//StringBuffer sb = new StringBuffer(" where a.packType = '"+Constants.GWSYS+"' ");
//		StringBuffer sb = new StringBuffer(" where a.contentId = b.uuid and a.targetOrg='"+getOrgCode()+"' ");
		StringBuffer sb = new StringBuffer(" where a.contentId = b.uuid");
		if (Constants.ACCEPT_YES.equals(acceptStatus)){
			sb.append(" and a.receiptStatus is not null ");
		}else{
			sb.append(" and a.receiptStatus is null ");
		}
		sb.append(" and a.packType = 'OFC'");
		if (StringHelper.isNotEmpty(docTitle)){
			sb.append(" and b.docTitle like :docTitle ");
		}
		if(startRecvTime != null) {
			sb.append(" and b.recvTime >=:startRecvTime");
		}
		if(endRecvTime != null) {
			sb.append(" and b.recvTime <:endRecvTime");
		}
		return sb.toString();
	}

	protected void setWhere(QueryCache qc) {
//		if (Constants.ACCEPT_YES.equals(acceptStatus)){
//			qc.setParameter("acceptStatus",Constants.ACCEPT_YES);
//		}else{
//			qc.setParameter("acceptStatus", Constants.ACCEPT_NO);
//		}
		if (StringHelper.isNotEmpty(docTitle)){
			qc.setParameter("docTitle", "%" + docTitle.trim() + "%");
		}
		if(startRecvTime != null){
			qc.setParameter("startRecvTime",startRecvTime);
		}
		if(endRecvTime != null){
			qc.setParameter("endRecvTime",DateUtil.addDate(endRecvTime, 1));
		}
	}

	protected String getOrder() {
		if (StringHelper.isEmpty(page.getOrderString())) {
			page.setOrderFlag(0);
			page.setOrderString(" b.recvTime ");
		}
		return page.getOrderByString();
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public Date getStartRecvTime() {
		return startRecvTime;
	}

	public void setStartRecvTime(Date startRecvTime) {
		this.startRecvTime = startRecvTime;
	}

	public Date getEndRecvTime() {
		return endRecvTime;
	}

	public void setEndRecvTime(Date endRecvTime) {
		this.endRecvTime = endRecvTime;
	}

	public String getAcceptStatus() {
		return acceptStatus;
	}

	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}


}
