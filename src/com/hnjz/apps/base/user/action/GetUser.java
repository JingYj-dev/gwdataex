package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.common.BaseEnvironment;
import com.hnjz.apps.base.org.action.OrgItem;
import com.hnjz.apps.base.org.action.OrgTree;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.RegexCheck;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetUser extends AdminAction {
	private static Log log = LogFactory.getLog(GetUser.class);
	private SUser item;
	private String uuid;
	private String orgId;
	private String tree;
	public GetUser() {
		this.item = new SUser();
	}
	public String adminGo() {
		try {
			if(StringHelper.isNotEmpty(uuid)){
				item = QueryCache.get(SUser.class, uuid);
				String str = RegexCheck.TagReverse(item.getRemark());
				item.setRemark(str);
				orgId = item.getOrgId();
			} else {
				item = new SUser();
				if(StringHelper.isEmpty(orgId) && (OrgTree.getInstance().getRootNode()!=null) && StringHelper.isNotEmpty(OrgTree.getInstance().getRootNode().getNodeId())){
					orgId = OrgTree.getInstance().getRootNode().getNodeId();
				}
				item.setOpenFlag("1");
				item.setSex("1");
				item.setUserType(BaseEnvironment.USERTYPE_NORMAL);
 				item.setSecLevel("1");
			}
			tree = OrgItem.getOrgRadio(orgId).toString();
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public SUser getItem() {
		return item;
	}

	public void setItem(SUser item) {
		this.item = item;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

}
