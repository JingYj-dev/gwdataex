package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysDirItem;
import com.hnjz.core.configuration.Environment;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetDataexSysDirRadioTree extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GetDataexSysDirRadioTree.class);
	private String parentId;
	private String id;
	
	public GetDataexSysDirRadioTree() {
	}

	@Override
	protected String adminGo() {
		try {
			if(StringHelper.isNotEmpty(id)){
				JSONArray ja = DataexSysDirItem.getSubDataexSysDirRadio(id, parentId);
				result = ja.toString();
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
