package com.hnjz.apps.base.user.action;

import com.hnjz.util.Ajax;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FirstPage extends UserAction {
	
	private static Log log = LogFactory.getLog(FirstPage.class);
	private String dbCounts="0";
	private String cbCounts="0";
	@Override
	protected String userGo() {
		
		try {			
//		    dbCounts = DbCountsService.getDbWorkCounts(UserProvider.currentUser());
//			cbCounts = DragUrgencyNumService.getDraUrgencyNum(UserProvider.currentUser());
			result = Ajax.JSONResult(0, dbCounts+","+cbCounts);
			return Action.SUCCESS;
		} catch (Exception ex) {
			String msg=Messages.getString("systemMsg.exception");
			log.error(msg, ex);
			setMessage(msg);
			dbCounts = "0";
			cbCounts = "0";
			result = Ajax.JSONResult(0, dbCounts+","+cbCounts);
			return Action.ERROR;
		}
	}
	public String getDbCounts() {
		return dbCounts;
	}
	public void setDbCounts(String dbCounts) {
		this.dbCounts = dbCounts;
	}
	public String getCbCounts() {
		return cbCounts;
	}
	public void setCbCounts(String cbCounts) {
		this.cbCounts = cbCounts;
	}
	
	
}
