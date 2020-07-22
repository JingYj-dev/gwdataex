package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirTree;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.DateUtil;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirDataexSysDir extends AdminAction{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DirDataexSysDir.class);
	private String dirName; //目录名称
//	private String dirIp;
//	private String dirProvider;
	private Date beginCreatedTime;
	private Date endCreatedTime;
//	private String dirType;
//	private String signature;
//	private String encryption;
	private String dirStatus;
	
	private String tree;
	private String dataexSysDirId;
	private String includeFlag;
	private Page page;
	public DirDataexSysDir() {
		includeFlag = "1";
		page = new Page();
		page.setCountField("a.uuid");
	}
	@Override
	protected String adminGo() {
		try {
			trimStr(); //过滤空格
			QueryCache qc = new QueryCache("select a.uuid from DataexSysDir a " + getWhere() + getOrder());			
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(DataexSysDir.class, page.getResults()));
			//获取目录树
			/*JSONObject jo = DataexSysDirItem.getRootDataexSysDir();
			JSONArray ja = DataexSysDirItem.getSubDataexSysDir(DataexSysDirTree.getInstance().getRootNode().getNodeId());
			if(jo != null) {
				ja.add(jo);
			}
			tree = ja.toString();*/
			return SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page.getOrderByString() : " order by a.createdTime desc";
	}
	public String getWhere() {
		StringBuffer sb = new StringBuffer(" where 1 = 1 ");
		if (StringHelper.isNotEmpty(dataexSysDirId)){
			sb.append(" and (  a.uuid in (:parentId) ");
			sb.append(" or a.parentId in (:parentId) )");
		}
		if(StringHelper.isNotEmpty(dirName)) {
			sb.append(" and a.dirName like :dirName ");
		}
//		if(StringHelper.isNotEmpty(dirIp)) {
//			sb.append(" and a.dirIp like :dirIp ");
//		}
//		if(StringHelper.isNotEmpty(dirProvider)) {
//			sb.append(" and a.dirProvider like :dirProvider ");
//		}
		if(beginCreatedTime != null) {
			sb.append(" and a.createdTime >= :beginCreatedTime ");
		}
		if(endCreatedTime != null) {
			sb.append(" and a.createdTime < :endCreatedTime ");
		}
//		if(StringHelper.isNotEmpty(dirType)) {
//			sb.append(" and a.dirType = :dirType ");
//		}
//		if(StringHelper.isNotEmpty(signature)) {
//			sb.append(" and a.signature = :signature ");
//		}
//		if(StringHelper.isNotEmpty(encryption)) {
//			sb.append(" and a.encryption = :encryption ");
//		}
		if(StringHelper.isNotEmpty(dirStatus)) {
			sb.append(" and a.dirStatus = :dirStatus ");
		}
		return sb.toString();
	}
	
	private List<String> getSubDir(String dataexSysDirId){
		List<String> ids =  new ArrayList<String>();
		if("2".equals(includeFlag)){
			DataexSysDirTree dirTree = DataexSysDirTree.getInstance();
			List dataexSysDirIdList = dirTree.getListById(dirTree.getTreeNode(dataexSysDirId).getAllChildren());
			ids.addAll(dataexSysDirIdList);
		}
		ids.add(dataexSysDirId);
		return ids;
	}
	
	@SuppressWarnings("unchecked")
	public void setWhere(QueryCache qc) {
		if (StringHelper.isNotEmpty(dataexSysDirId)){
			qc.setParameter("parentId", getSubDir(dataexSysDirId));
		}
		if(StringHelper.isNotEmpty(dirName)) {
			qc.setParameter("dirName", "%" + dirName + "%");
		}
//		if(StringHelper.isNotEmpty(dirIp)) {
//			qc.setParameter("dirIp", "%" + dirIp + "%");
//		}
//		if(StringHelper.isNotEmpty(dirProvider)) {
//			qc.setParameter("dirProvider", "%" + dirProvider + "%");
//		}
		if(beginCreatedTime != null) {
			qc.setParameter("beginCreatedTime", beginCreatedTime);
		}
		if(endCreatedTime != null) {
			qc.setParameter("endCreatedTime", DateUtil.addDate(endCreatedTime, 1));
		}
//		if(StringHelper.isNotEmpty(dirType)) {
//			qc.setParameter("dirType", dirType);
//		}
//		if(StringHelper.isNotEmpty(signature)) {
//			qc.setParameter("signature", signature);
//		}
//		if(StringHelper.isNotEmpty(encryption)) {
//			qc.setParameter("encryption", encryption);
//		}
		if(StringHelper.isNotEmpty(dirStatus)) {
			qc.setParameter("dirStatus", dirStatus);
		}
	}
	private void trimStr() {
		dirName = StringHelper.isNotEmpty(dirName) ? dirName.trim() : "";
	}
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	public Date getBeginCreatedTime() {
		return beginCreatedTime;
	}
	public void setBeginCreatedTime(Date beginCreatedTime) {
		this.beginCreatedTime = beginCreatedTime;
	}
	public Date getEndCreatedTime() {
		return endCreatedTime;
	}
	public void setEndCreatedTime(Date endCreatedTime) {
		this.endCreatedTime = endCreatedTime;
	}
	public String getTree() {
		return tree;
	}
	public void setTree(String tree) {
		this.tree = tree;
	}
	public String getDataexSysDirId() {
		return dataexSysDirId;
	}
	public void setDataexSysDirId(String dataexSysDirId) {
		this.dataexSysDirId = dataexSysDirId;
	}
	public String getIncludeFlag() {
		return includeFlag;
	}
	public void setIncludeFlag(String includeFlag) {
		this.includeFlag = includeFlag;
	}
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	public String getDirStatus() {
		return dirStatus;
	}
	public void setDirStatus(String dirStatus) {
		this.dirStatus = dirStatus;
	}
}
