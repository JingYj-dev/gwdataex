package com.hnjz.apps.base.user.action;

import com.hnjz.apps.base.security.model.SecurityParam;
import com.hnjz.apps.base.security.service.SecurityService;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.hnjz.apps.base.user.model.SUser;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class PerUserActiveStatus extends AdminAction {
	private static final Log log = LogFactory.getLog(PerUserActiveStatus.class);
	private String  ids;
	
	@Override
	protected String adminGo() {
		TransactionCache tx = null;
		try {
			if (StringHelper.isEmpty(ids)) {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			int num=0;
			List lst = StringHelper.strToList(ids);
			List<SUser> itemLst = (List<SUser>) QueryCache.idToObj(SUser.class, lst);
			tx = new TransactionCache();
			for(SUser item : itemLst ){
				if("1".equals(item.getActiveStatus()) || "2".equals(item.getActiveStatus())){  //只有已创建的用户才可以设置激活状态为2，允许激活
					item.setActiveStatus("2");
					String time = SecurityService.getParamValue(SecurityParam.ACTIVE_TIME);
					Calendar cc = new GregorianCalendar();
					cc.set(Calendar.MINUTE, cc.get(Calendar.MINUTE) + Integer.parseInt(time));
/*					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String getTime = sdf.format(cc.getTime());*/
					item.setActiveDeadLine(cc.getTime());
					tx.update(item);
					num++;
				}
			}
			tx.commit();
			String str_num=Integer.toString(num);
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.EditActiveStatusNum",new String[]{str_num}));
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
