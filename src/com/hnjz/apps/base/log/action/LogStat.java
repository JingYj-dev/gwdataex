package com.hnjz.apps.base.log.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LogStat extends AdminAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(LogStat.class);
	private String statBeginDate;
	private String statEndDate;
	private Page page;

	public LogStat() {
		page = new Page();
	}

	@Override
	protected String adminGo() {
		try {
			if (StringHelper.isNotEmpty(statBeginDate)
					&& StringHelper.isNotEmpty(statEndDate)) {
				String[] s = statBeginDate.split("-");
				Integer year1 = Integer.valueOf(s[0]);
				Integer month1 = Integer.valueOf(s[1]);
				s = statEndDate.split("-");
				Integer year2 = Integer.valueOf(s[0]);
				Integer month2 = Integer.valueOf(s[1]);
				List<Object[]> list = new ArrayList<Object[]>();

				for (int i = year1; i <= year2; i++) {
					if (i < year2) {
						int j = 0;
						j = i == year1 ? month1 : 1;
						for (; j <= 12; j++) {
							doSearch(list, i, j);
						}
					} else if (i == year2) {
						int j = 0;
						j = i == year1 ? month1 : 1;
						for (; j <= month2; j++) {
							doSearch(list, i, j);
						}
					}
				}
				page.setResults(list);
				return Action.SUCCESS;

			} else {
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
						Messages.getString("systemMsg.exception"));
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

	public void doSearch(List<Object[]> list, Integer i, Integer j) {
		StringBuffer sb = new StringBuffer(
				" select a.event_type,sum(case when(a.log_level='1') then 1 else 0 end) as 一般,"
						+ " sum(case when(a.log_level='2') then 1 else 0 end) as 重要,"
						+ " sum(case when(a.log_level='3') then 1 else 0 end) as 严重  "
						+ " from s_log a " + getWhere(i, j));
		QueryCache qc = new QueryCache(sb.toString(), true);
		List<?> list1 = qc.listCache();
		if (list1 != null && list1.size() > 0) {
			Object[] obj3 = new Object[2];
			obj3[0] = "" + i + "-" + (j > 9 ? j : "0" + j);
			obj3[1] = list1;
			list.add(obj3);
		}
	}

	public String getWhere(Integer i, Integer j) {
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		sb.append(" and a.op_time >= '" + i + "-" + j + "-01'");

		if (j != 12) {
			sb.append(" and a.op_time < '" + i + "-" + (j + 1) + "-01'");
		} else {
			sb.append(" and a.op_time < '" + (i + 1) + "-01-01'");
		}

		sb.append(" group by a.event_type order by a.event_type");

		return sb.toString();
	}

	public String getOrder() {
		return StringHelper.isNotEmpty(page.getOrderByString()) ? page
				.getOrderByString() : "";
	}

	public void setWhere(QueryCache qc, int i, int j) throws ParseException {
		qc.setParameter("statBeginDate", "" + i + "-" + j + "01");
		if (j != 12) {
			qc.setParameter("statEndDate", "" + i + "-" + (j + 1) + "01");
		} else {
			qc.setParameter("statEndDate", "" + (i + 1) + "-01-01");
		}
	}

	public String getStatBeginDate() {
		return statBeginDate;
	}

	public void setStatBeginDate(String statBeginDate) {
		this.statBeginDate = statBeginDate;
	}

	public String getStatEndDate() {
		return statEndDate;
	}

	public void setStatEndDate(String statEndDate) {
		this.statEndDate = statEndDate;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
