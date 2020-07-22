package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirTree;
import com.hnjz.core.configuration.Environment;
import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.util.UuidUtil;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddDataexSysDir extends AdminAction implements ModelDriven {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AddDataexSysDir.class);
	private DataexSysDir item;

	public AddDataexSysDir() { 
		this.item = new DataexSysDir();
	}
	
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if (item == null
					|| StringHelper.isEmpty(item.getDirName())
					|| StringHelper.isEmpty(item.getDirOrg())
//					|| StringHelper.isEmpty(item.getDirIp())
//					|| StringHelper.isEmpty(item.getDirProvider())
//					|| StringHelper.isEmpty(item.getDirType())
					|| StringHelper.isEmpty(item.getParentId())
//					|| StringHelper.isEmpty(item.getDataServiceUrl())
					|| StringHelper.isEmpty(item.getDirStatus())) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
						Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			
			//同一个父级目录下面不能出现相同的目录名
			String id = (String)new QueryCache("select a.uuid from DataexSysDir a where a.dirName =:dirName and a.parentId =:parentId ")
				.setParameter("dirName",item.getDirName()).setParameter("parentId", item.getParentId()).setMaxResults(1).uniqueResult();
			if(StringHelper.isNotEmpty(id)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("sysmgrMsg.dirNameExsit", new String[]{item.getDirName()}));				
				return Action.ERROR;
			}
			//机构编码不能重复
			String org_code = (String)new QueryCache("select a.uuid from DataexSysDir a where a.dirOrg=:dirOrg").setParameter("dirOrg", item.getDirOrg()).setMaxResults(1).uniqueResult();
			if(StringHelper.isNotEmpty(org_code)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("sysmgrMsg.dirOrgExsit",new String[]{item.getDirOrg()}));
				return Action.ERROR;
			}
			DataexSysDir sysDir = new DataexSysDir();
			sysDir.setUuid(UuidUtil.getUuid());
//			sysDir.setIdentityId(UuidUtil.getUuid());
			sysDir.setDirName(item.getDirName().trim());
			sysDir.setDirOrg(item.getDirOrg().trim());
//			sysDir.setDirIp(item.getDirIp().trim());
//			sysDir.setDirProvider(item.getDirProvider().trim());
			sysDir.setDirStatus(item.getDirStatus());
//			sysDir.setDirType(item.getDirType());
			sysDir.setCreatedTime(java.util.Calendar.getInstance().getTime());
//			sysDir.setEncryption(item.getEncryption());
//			sysDir.setSignature(item.getSignature());
			sysDir.setParentId(item.getParentId());
//			sysDir.setDataServiceUrl(item.getDataServiceUrl());
			sysDir.setExnodeId(item.getExnodeId());
			sysDir.setAppid(item.getAppid());
			tx = new TransactionCache();
			tx.save(sysDir);
			tx.commit();
			
			TreeNode node = new TreeNode();
			node.setNodeId(sysDir.getUuid());
			node.setParentId(sysDir.getParentId());
			DataexSysDirTree.getInstance().addTreeNode(node);
			DataexSysDirTree.getInstance().reloadTree();
			
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), sysDir.toJson());
			return Action.SUCCESS;
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	@Override
	public Object getModel() {
		return item;
	}

}
