package com.hnjz.apps.base.org.action;

import com.hnjz.apps.base.org.model.SOrg;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.role.action.GetRole;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Qianzhj
 * @version v0.1
 * @since 2014-8-1 下午11:22:39
 */
@SuppressWarnings("serial")
public class GetOrgPost extends AdminAction {
	private static Log log = LogFactory.getLog(GetRole.class);
	private String uuid;
	private String orgId;
	private SOrg item;

	public GetOrgPost() {
		
	} 

	@Override
	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(orgId)) {
				item = QueryCache.get(SOrg.class, orgId);
				if(item==null || "1".equals(item.getDelFlag())){
					setMessage(Messages.getString("systemMsg.NoOrg"));
					return Action.ERROR;	
				}
				QueryCache qc = new QueryCache("select count(a.postId) from SOrgPost a where a.orgId=:orgId")
				.setParameter("orgId", orgId);
				QueryCache qs = new QueryCache("select count(a.uuid) from SPost a");
				Long orgpostCount = (Long) qc.uniqueResult();
				Long postCount = (Long) qs.uniqueResult();
				if(orgpostCount.equals(postCount)){
					setMessage(Messages.getString("systemMsg.NoAddPost"));
					return Action.ERROR;	
				}
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
	}

	public SOrg getItem() {
		return item;
	}

	public void setItem(SOrg item) {
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
	
}
