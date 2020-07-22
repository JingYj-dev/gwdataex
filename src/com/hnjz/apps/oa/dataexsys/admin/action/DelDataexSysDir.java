package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirTree;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file DirDataexInbox.java creation date: [Jun 30, 2014 10:00:27 PM] by mazhh
 * http://www.css.com.cn
 */
public class DelDataexSysDir extends AdminAction{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DelDataexSysDir.class);
	private String ids;
	
	@SuppressWarnings("unchecked")
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try{
			if(StringHelper.isEmpty(ids)){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));	
				return Action.ERROR;		
			}
			
			List<String> listIds = StringHelper.strToList(ids);
			for (String id : listIds) {
				String parentId = (String)new QueryCache(" select a.parentId from DataexSysDir a where a.uuid =:uuid ").setParameter("uuid", id).setMaxResults(1).uniqueResult();
				if (StringHelper.isEmpty(parentId)) {
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("sysmgrMsg.delRootNodeError"));	
					return Action.ERROR;
				}
				List list = null;
				list = new QueryCache(" select a.uuid from DataexSysDir a where a.parentId =:parentId ").setParameter("parentId", id).list();
				if (list != null && list.size() > 0) {
					result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("sysmgrMsg.deleteDirChildExsit"));	
					return Action.ERROR;
				}
			}
			List<DataexSysDir> dirList = QueryCache.idToObj(DataexSysDir.class, listIds);
			List<String> delIds = new ArrayList<String>();
			tx = new TransactionCache();
			for (DataexSysDir dir : dirList) {
				DataexSysDirTree.getInstance().deleteTreeNode(dir.getUuid());
				tx.delete(dir);
				delIds.add(dir.getUuid());
			}
			//DataexSysDirTree.getInstance().reloadTree();
			tx.commit();
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), delIds);
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
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
