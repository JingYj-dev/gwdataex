package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.cas.QueueUtil;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.db.query.QueryCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class DataexSysGcTask implements IGcTask {
	
	private static final Log _logger = LogFactory.getLog(DataexSysGcTask.class);

	@Override
	public boolean gcTask() {
		boolean isSuccess = false;
		/*QueryCache qc = new QueryCache("select a.UUID from DATAEX_SYS_TRANSTASK a  " + getWhere(),true);
		setWhere(qc);
		List lst = qc.list();
		List<DataexSysTransTask> timeoutTasks = QueryCache.idToObj(DataexSysTransTask.class, lst);
		//XXX 无任务，则无需重新加载
		if(timeoutTasks==null || timeoutTasks.size()==0)
		{
			isSuccess = true;
			return isSuccess;
		}
		TransactionCache tx = null;*/
		try {
			//发送失败5次后，不再发送（暂时不用重置失败次数，这样对导致一直发送）(2018/11/16)
			/*tx = new TransactionCache();
			for (DataexSysTransTask item : timeoutTasks) {
				item.setSendStatus(Constants.kqueue_received);
				item.setEndTime(null);
				item.setResendTimes(1);
				tx.update(item);
			}
			tx.commit();*/
			//初始化操作
			//这里老的旧的res任务数据会被覆盖掉
			//FIXME  此处是否应该考虑并发，或者应该作增量处理
			QueueUtil.initDataexSysQueue();
			isSuccess = true;
		} catch (Exception e) {
			/*if (tx != null) {
				tx.rollback();
			}*/
			if (_logger.isErrorEnabled()) {
				_logger.error(e.getMessage() == null ? "[update timeoutTasks occur Exception. ]" : e.getMessage(), e);
			}
		}
		return isSuccess;
	}
	//这里任务的执行策略均采用FIFO算法
	private String getWhere() {
		StringBuffer sb = new StringBuffer("where ((a.SEND_STATUS='" + Constants.kqueue_sended + "') and (a.END_TIME<=:finishTime)) or (a.SEND_STATUS=:failure and a.RESEND_TIMES <:times) order by a.START_TIME");
		return sb.toString();
	}

	private void setWhere(QueryCache qc) {
		long diffTime = new Date().getTime() - Constants.getTimeOut();
		Date finishTime = new Date();
		finishTime.setTime(diffTime);
		qc.setParameter("finishTime", finishTime).setParameter("failure", Constants.kqueue_failure).setParameter("times", Constants.getSendTimes());
	}
	
	/*public static void main(String[] args) {
		System.out.println(ChannelDict.WEB.toString());
	}*/

}
