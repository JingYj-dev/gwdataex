package com.hnjz.apps.base.log.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class LogFx extends AdminAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(LogFx.class);
	private Integer fxDate;
	private String eventTypes;
	private String eventCodes;
	private List<Object[]> tjList;

	public LogFx() {
		tjList = new ArrayList<Object[]>();
	}

	@Override
	protected String adminGo() {
		try {
			if (fxDate != null && StringHelper.isNotEmpty(eventCodes)) {
				String[] codes = eventCodes.split(",");
				tjList = new ArrayList<Object[]>();
				StringBuffer sb = new StringBuffer(" select a.event_type,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-01-01' and a.op_time<'"+fxDate+"-02-01') then 1 else 0 end) as 一月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-02-01' and a.op_time<'"+fxDate+"-03-01') then 1 else 0 end) as 二月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-03-01' and a.op_time<'"+fxDate+"-04-01') then 1 else 0 end) as 三月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-04-01' and a.op_time<'"+fxDate+"-05-01') then 1 else 0 end) as 四月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-05-01' and a.op_time<'"+fxDate+"-06-01') then 1 else 0 end) as 五月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-06-01' and a.op_time<'"+fxDate+"-07-01') then 1 else 0 end) as 六月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-07-01' and a.op_time<'"+fxDate+"-08-01') then 1 else 0 end) as 七月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-08-01' and a.op_time<'"+fxDate+"-09-01') then 1 else 0 end) as 八月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-09-01' and a.op_time<'"+fxDate+"-10-01') then 1 else 0 end) as 九月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-10-01' and a.op_time<'"+fxDate+"-11-01') then 1 else 0 end) as 十月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-11-01' and a.op_time<'"+fxDate+"-12-01') then 1 else 0 end) as 十一月,");
				sb.append("sum(case when(a.op_time>='"+fxDate+"-12-01' and a.op_time<'"+(fxDate+1)+"-01-01') then 1 else 0 end) as 十二月,");
				sb.append("count(a.event_type)/12.0,count(a.event_type)");
				sb.append(" from s_log a ");
				sb.append(" where a.event_type in :typeCode");
				sb.append(" group by a.event_type order by a.event_type ");
				QueryCache qc = new QueryCache(sb.toString(), true);
				qc.setParameter("typeCode", codes);
				List<?> list = qc.listCache();
				for(int i = 0; i < codes.length; i++){
					Object[] obj = {null,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
					obj[0] = codes[i];
					if(list != null && list.size() > 0){
						for(int j = 0; j < list.size(); j++){
							Object[] obj2 = (Object[]) list.get(j);
							if((String.valueOf(obj2[0])).equals(obj[0])){
								obj = obj2;
							}
						}
					}
					tjList.add(obj);
				}
				return Action.SUCCESS;
			} else {
				return Action.SUCCESS;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages
					.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public Integer getFxDate() {
		return fxDate;
	}

	public void setFxDate(Integer fxDate) {
		this.fxDate = fxDate;
	}

	public String getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(String eventTypes) {
		this.eventTypes = eventTypes;
	}

	public String getEventCodes() {
		return eventCodes;
	}

	public void setEventCodes(String eventCodes) {
		this.eventCodes = eventCodes;
	}

	public List<Object[]> getTjList() {
		return tjList;
	}

	public void setTjList(List<Object[]> tjList) {
		this.tjList = tjList;
	}

}
