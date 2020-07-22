package com.hnjz.apps.base.post.action;


import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetPostRole extends AdminAction{
	private static Log log = LogFactory.getLog(GetPostRole.class);
	private String postId;
	private SPost item;
	private boolean Flag;
	public GetPostRole() {
	}
	@Override
	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(postId)) {
				item = QueryCache.get(SPost.class, postId);
			}
			if(Flag == false){
				setMessage(Messages.getString("systemMsg.NoAddUserRole"));
				return Action.ERROR;	
			}
			return Action.SUCCESS;
		} catch (Exception ex) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			log.error(ex.getMessage(), ex);
			return Action.ERROR;
		}
		
	}

	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public SPost getItem() {
		return item;
	}
	public void setItem(SPost item) {
		this.item = item;
	}
	public boolean isFlag() {
		return Flag;
	}
	public void setFlag(boolean flag) {
		Flag = flag;
	}
	
}

