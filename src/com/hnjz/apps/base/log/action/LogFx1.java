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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class LogFx1 extends AdminAction {

	private static Log log = LogFactory.getLog(LogFx1.class);
	private Integer fxDate;
	private String eventTypes;
	private String eventCodes;
	private List tjList;

	public LogFx1() {
		tjList = new ArrayList();
	}

	@Override
	protected String adminGo() {
		try {
			if (fxDate != null && StringHelper.isNotEmpty(eventTypes)) {
				String[] types = eventTypes.split(",");
				String[] codes = eventCodes.split(",");
				tjList = new ArrayList();
				StringBuffer sb = new StringBuffer(
						" select count(a.event_type) from s_log a where a.op_time>=:beginTime and a.op_time<:endTime and a.event_type=:typeCode ");
				for (int i = 0; i < codes.length; i++) {
					Object[] obj = new Object[15];
					obj[0] = types[i];
					int sum = 0;
					QueryCache qc = new QueryCache(sb.toString(), true);
					for (int j = 1; j <= 12; j++) {
						setWhere(qc, fxDate, j, codes[i]);
						List list2 = qc.listCache();
						obj[j] = new BigInteger(list2.get(0).toString())
								.intValue();
						sum += (Integer) obj[j];
					}
					obj[13] = 1.0 * sum / 12;
					obj[14] = sum;
					tjList.add(obj);
				}
				return Action.SUCCESS;
			} else {
				return Action.SUCCESS;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			setMessage(Messages.getString("systemMsg.exception"));
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public void setWhere(QueryCache qc, Integer year, Integer month,
			String code) {
		String beginTime, endTime;
		if (month == 12) {
			beginTime = "" + year + "-12-01";
			endTime = "" + (year + 1) + "-01-01";
		} else {
			beginTime = "" + year + "-" + month + "-01";
			endTime = "" + year + "-" + (month + 1) + "-01";
		}
		qc.setParameter("beginTime", beginTime);
		qc.setParameter("endTime", endTime);
		qc.setParameter("typeCode", code);
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

	public List getTjList() {
		return tjList;
	}

	public void setTjList(List tjList) {
		this.tjList = tjList;
	}

}
