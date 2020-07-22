package com.hnjz.apps.base.log.service;

import com.hnjz.common.plugins.impl.AbstractPlugin;
import com.hnjz.core.model.ILog;
import com.hnjz.core.plugins.base.ILogProvider;
import com.hnjz.db.hibernate.HibernateUtil;
import com.hnjz.db.query.IAtom;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
public class LogService extends AbstractPlugin implements ILogProvider {
	private static Log log = LogFactory.getLog(LogService.class);
	public static long getLogCount(){
		Object count = new QueryLog(" select count(*) from SLog a ").setMaxResults(1).uniqueResult();
		if(count==null)return 0;
		return Long.parseLong(count.toString());
	}
	public void log(final ILog slog){
			Session s = null;
			TransactionCache tx = null;
			try {
					tx = new TransactionCache();
					String sqlstr = "INSERT INTO  " + getTableName()
					+ " (LOG_ID,SYSID,FUNC_ID,LOG_LEVEL,LOG_TYPE,LOG_DATA,OP_ID,OP_NAME,OP_TIME,OP_IP,DURATION_TIME,SERVER_IP,SERVER_NAME,OP_RESULT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					SQLQuery ps= s.createSQLQuery(sqlstr);
					ps.setString(0, slog.getLogId());
					ps.setString(1, slog.getSysid());
					ps.setString(2, slog.getFuncId());					
					if(slog.getLogLevel()!=null)
						ps.setInteger(3, slog.getLogLevel());
					else
						ps.setInteger(3, 0);
					if(slog.getOpType()!=null)
						ps.setInteger(4, slog.getOpType());
					else
						ps.setInteger(4, 0);
					ps.setString(5, slog.getLogData());
					ps.setString(6, slog.getOpId());
					ps.setString(7, slog.getOpName());							
					ps.setTimestamp(8, new Timestamp(slog.getOpTime().getTime()));
					ps.setString(9, slog.getOpIp());
					ps.setLong(10, slog.getDurationTime());
					 
					ps.setString(11, slog.getServerIp());
					ps.setString(12, slog.getServerName());
					ps.setString(13, slog.getResult());					 
					ps.executeUpdate();
					tx.commit();
			} catch (Exception e) {
				if(tx != null){
					tx.rollback();
				}
				log.error("日志记录失败:",e);
			}finally{
				HibernateUtil.closeSession();
			}
	}
 
	/**
	 * 得到日志表名
	 * @return
	 * @throws Exception
	 */
	protected   String getTableName() throws Exception {
		return "S_LOG_EXT";
	}
	
	/**
	 * 日志归档
	 * @return
	 * @throws Exception
	 */
	public static String fileLog(String filetime){
		SimpleDateFormat tsdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringHelper.isEmpty(filetime)){
			Calendar curCal = Calendar.getInstance();
			curCal.set(Calendar.DAY_OF_MONTH, 1);
	        Date beginTime = curCal.getTime();
	        filetime = tsdf.format(beginTime) + " 00:00:00";
		}else{
			filetime+=" 00:00:00";
		}
		//TransactionCache tx = null;
		String thql="select min(t.opTime) from SLog t";
		QueryCache qc=new QueryCache(thql);
		List mintimelist=qc.listCache();
		Date tmintime=null;
		if(mintimelist!=null && mintimelist.size()>0 ){
			tmintime= (Date) mintimelist.get(0);
		}
		if(tmintime!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			long cd=0;
			try {
				cd=sdf.parse(filetime).getTime()-tmintime.getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			long tmon=1000L*60*60*24*30;//一月；
			if(cd>tmon){
				Calendar cal=Calendar.getInstance();
				cal.setTime(tmintime);
				cal.add(Calendar.MONTH, 1);
				String oktime=tsdf.format(cal.getTime());
				return oktime;
			}
		}
		final String inserthql="insert into s_log_file (log_id, sysid, func_id, log_level, log_type, log_data, op_time, op_result, op_id, op_name, op_ip, server_ip, server_name, duration_time)"+ 
			"(select log_id, sysid, func_id, log_level, log_type, log_data, op_time, op_result, op_id, op_name, op_ip, server_ip, server_name, duration_time from s_log t "+
			"where t.op_Time <=to_date('"+filetime+"','yyyy-mm-dd HH24:MI:SS'))";
		final String delhql="delete from s_log t where t.op_Time <=to_date('"+filetime+"','yyyy-mm-dd HH24:MI:SS')";
		try {
			 qc.getTransaction().executeTrans(new IAtom(){
				 public Object execute(TransactionCache tc){
					 tc.executeUpdateSQL(inserthql);
					 tc.executeUpdateSQL(delhql);
					 return true;
				 }
			 });
		} catch (Exception e) {
			log.error("记录日志失败",e);
			return "error";
		}

		return "success";
	}
}
