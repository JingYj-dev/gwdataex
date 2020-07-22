package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirItem;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetDataexSysDir extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetDataexSysDir.class);
	private DataexSysDir item;
	private String uuid;
	private String defaultPId;
	private String tree;
	public GetDataexSysDir() {
		this.item = new DataexSysDir();
	}

	public String adminGo() {
		try {
			if(StringHelper.isNotEmpty(uuid)){
				item = QueryCache.get(DataexSysDir.class, uuid);
			} else {
				if(StringHelper.isEmpty(defaultPId)){
					defaultPId = "0";
				}
				item = new DataexSysDir();
				item.setParentId(defaultPId);
				item.setDirStatus(Constants.DEFAULT_DIRSTATUS_OPEN);
//				item.setDirType(Constants.DEFAULT_DIRTYPE_EXSYS);
//				item.setEncryption(Constants.DEFAULT_DIRENCRYPTION_NONE);
//				item.setSignature(Constants.DEFAULT_DIRSIGNATURE_NONE);
			}
			tree = DataexSysDirItem.getDataexSysDirRadio(item.getParentId()).toString();
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public DataexSysDir getItem() {
		return item;
	}

	public void setItem(DataexSysDir item) {
		this.item = item;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDefaultPId() {
		return defaultPId;
	}

	public void setDefaultPId(String defaultPId) {
		this.defaultPId = defaultPId;
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}
}
