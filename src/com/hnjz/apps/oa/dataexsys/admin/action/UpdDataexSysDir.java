package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirTree;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UpdDataexSysDir extends AdminAction implements ModelDriven {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(UpdDataexSysDir.class);
	private DataexSysDir item;
	
	public UpdDataexSysDir() { 
		this.item = new DataexSysDir();
	}
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if (item == null || StringHelper.isEmpty(item.getUuid())
					|| StringHelper.isEmpty(item.getDirName())
					|| StringHelper.isEmpty(item.getDirOrg())
//					|| StringHelper.isEmpty(item.getDirIp())
//					|| StringHelper.isEmpty(item.getDirProvider())
//					|| StringHelper.isEmpty(item.getDataServiceUrl())
//					|| StringHelper.isEmpty(item.getDirType())
					|| StringHelper.isEmpty(item.getDirStatus())) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
						Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			
			DataexSysDir sysDir = QueryCache.get(DataexSysDir.class, item.getUuid());
			if (sysDir == null) {
				result = Ajax.JSONResult(1, Messages.getString("systemMsg.recordnotfound"));
				return Action.ERROR;
		 	}
			//同一级目录下目录名称不能重复
			String dir_name = (String)new QueryCache("select a.uuid from DataexSysDir a where a.dirName=:dirName and a.parentId=:parentId and a.uuid!=:uuid ")
						  .setParameter("dirName",item.getDirName()).setParameter("parentId", item.getParentId()).setParameter("uuid",item.getUuid()).setMaxResults(1).uniqueResult();
			if(StringHelper.isNotEmpty(dir_name)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("sysmgrMsg.dirNameExsit",new String[]{item.getDirName()}));
				return Action.ERROR;
			}
			//机构编码不能重复
			String dir_org = (String)new QueryCache("select a.uuid from DataexSysDir a where dirOrg=:dirOrg and a.uuid!=:uuid ").setParameter("dirOrg", item.getDirOrg()).setParameter("uuid",item.getUuid()).setMaxResults(1).uniqueResult();
			if(StringHelper.isNotAnyEmpty(dir_org)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("sysmgrMsg.dirOrgExsit",new String[]{item.getDirOrg()}));
				return Action.ERROR;
			}
			
			
			sysDir.setDirName(item.getDirName());
			sysDir.setDirOrg(item.getDirOrg());
//			sysDir.setDirIp(item.getDirIp());
//			sysDir.setDirProvider(item.getDirProvider());
//			sysDir.setDirType(item.getDirType());
//			sysDir.setDataServiceUrl(item.getDataServiceUrl());
			sysDir.setDirStatus(item.getDirStatus());
//			sysDir.setSignature(item.getSignature());
//			sysDir.setEncryption(item.getEncryption());
//			sysDir.setCreatedTime(new Date());
			sysDir.setParentId(item.getParentId());
			sysDir.setExnodeId(item.getExnodeId());
			sysDir.setAppid(item.getAppid());
			tx = new TransactionCache();
			tx.update(sysDir);
			tx.commit();
			
			TreeNode node = new TreeNode();
			node.setNodeId(item.getUuid());
			node.setParentId(item.getParentId());
			DataexSysDirTree.getInstance().addTreeNode(node);
			DataexSysDirTree.getInstance().reloadTree();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), sysDir.toJson());
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
					Messages.getString("systemMsg.dbFaild"));
			return Action.ERROR;
		}
		
	}
	@Override
	public Object getModel() {
		return item;
	}
}
