package com.hnjz.apps.oa.dataexsys.common;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.apps.oa.dataexsys.admin.service.DataexSysTransParamService;
import com.hnjz.db.util.StringHelper;

public class Constants {
	
	//字符集编码
	public static final String charset = "utf-8";
	
	public static final String GW_CONTENT_SUFFIX = ".ofd";
	public static final String GW_ENTITY_SUFFIX = ".xml";
	
	public static final String BGN = "BGN";//BGN-请求连接
	public static final String END = "END";//END-断开连接
	public static final String FW_FORM = "fw_form";
	public static final String SW_FORM = "sw_form";
	public static final String RECV_SYS_DIR_ID = "62";
	public static final String SEND_SYS_DIR_ID = "63";//63代表交换系统发送目录62代表交换系统接收目录[在字典管理d_serverId和d_para_g中定义]
	public static final String UPLOAD_SYS_DIR_CERT_ID = "64";//64代表交换系统中目录机构证书上传路径[在字典管理d_serverId和d_para_g中定义]
	//返回给[调用方]的状态值
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String RESEND = "resend";
	//返回给[调用方]的交换状态值
	public static final String INTERRUPT = "interrupt";
	public static final String ARRIVE = "arrive";
	
	//签收状态
	public static final String ACCEPT_NO = "1";
	public static final String ACCEPT_YES = "2";
	//处理结果
	public static final String OP_FAILURE = "2";
	public static final String OP_SUCCESS = "1";
	//交换目录节点类型
	public static final String DATAEXSYS = "2";//交换系统
	public static final String GWSYS = "1";//处理系统
	
	//交换目录节点操作类型
	public static final String OP_REQUEST = "1";//请求操作
	public static final String OP_SEND = "2";//发送操作
	
	//接口类型
	public static final String WSTYPE_REQ = "1";//握手
	public static final String WSTYPE_SEND = "2";//发送
	
	//来源类型
	public static final String TYPE_SYS = "1";//系统交换
	public static final String TYPE_LOCAL = "2";//手工导入
	//握手方法
	public static final String REQ_OPERATIONNAME = "reqWebService";
	//公文处理系统服务方法
	public static final String DATAEX_OPERATIONNAME = "receive";
	//交换系统服务方法
	public static final String DATAEX_SYS_OPERATIONNAME = "sendBaseXMLEsbWebService";
	
	public static final long timeUnitHour = 60 * 60 * 1000L;
	public static final long timeUnitMinute = 60 * 1000L;
	public static final long timeUnitSecond = 1000L;

	//交换系统签到超时开关
	public static final boolean isOpen() {//默认关闭状态
		return (DictMan.getDictType("dataex_sys_d_param", "2") != null && DictMan.getDictType("dataex_sys_d_param", "2").getName().equals("open")) ? true : false;
	}
	//验签算法
	public static final String getAlg() {
		return (DictMan.getDictType("dataex_sys_d_param", "3") != null) ? DictMan.getDictType("dataex_sys_d_param", "3").getName() : "RSA";
	}
	
	//重发次数
	public static final int getSendTimes() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(2)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(2)) : 5;
	}
	//cas循环最大次数[max]
	public static final int getCasTimes() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(8)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(8)) : 500;
	}
	//初始化线程池大小
	public static final int getThreadPoolSize() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(5)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(5)) : 50;
	}
	//超时阀值
	public static final long getTimeOut() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(1)) ? Long.parseLong(DataexSysTransParamService.getParamValue(1)) * timeUnitMinute : 60 * timeUnitMinute;
	}
	//任务阀值
	public static final int getTaskNum() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(3)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(3)) : 1;
	} 
	//判断何时回收锁
	public static int getGcTimes() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(4)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(4)) : 5;
	}
	
	//成功休眠时间
	public static long getSuccessSleepTime() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(6)) ? Long.parseLong(DataexSysTransParamService.getParamValue(6)) : 1000L;
	}
	//任务回收时间间隔
	public static long getFailureSleepTime() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(7)) ? Long.parseLong(DataexSysTransParamService.getParamValue(7)) * timeUnitMinute : 10 * timeUnitMinute;
	}
	
	/*//重发次数
	public static final int getSendTimes() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(1)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(1)) : 5;
	}
	//初始化线程池大小
	public static final int getThreadPoolSize() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(5)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(5)) : 10;
	}
	//超时阀值
	public static final long getTimeOut() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(2)) ? Long.parseLong(DataexSysTransParamService.getParamValue(2)) : 60000L;
	}
	//任务阀值
	public static final int getTaskNum() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(3)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(3)) : 1;
	} 
	//判断何时回收锁
	public static int getGcTimes() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(4)) ? Integer.parseInt(DataexSysTransParamService.getParamValue(4)) : 5;
	}
	
	//成功休眠时间
	public static long getSuccessSleepTime() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(6)) ? Long.parseLong(DataexSysTransParamService.getParamValue(6)) : 100000L;
	}
	//失败休眠时间
	public static long getFailureSleepTime() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(7)) ? Long.parseLong(DataexSysTransParamService.getParamValue(7)) : 50000L;
	}*/
	//分布式锁key值
	public static String getLockKey() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(8)) ? DataexSysTransParamService.getParamValue(8) : "wait_to_lock";
	}
	//备份key
	public static String getBackupKey() {
		return StringHelper.isNotEmptyByTrim(DataexSysTransParamService.getParamValue(9)) ? DataexSysTransParamService.getParamValue(9) : "backup_wait_to_lock";
	}
	
	//对账单类型
	public static final String ACCOUNT_SEND = "1";//发送
	public static final String ACCOUNT_RECV = "2";//接收
	public static final String ACCOUNT_IMP = "3";//导入
	public static final String ACCOUNT_EXP = "4";//导出
	
	//对账单备注信息
	public static final String _RECV_SUCCESS_MEMO = "接收数据成功";
	public static final String _RECV_FAILURE_MEMO = "接收数据失败";
	public static final String _SEND_SUCCESS_MEMO = "发送数据成功";
	public static final String _SEND_FAILURE_MEMO = "发送数据失败";
	
	//报文类型
	public static final String MSG_DATAPACK = "OFC";//公文包
	public static final String MSG_FEEDBACK = "RET";//回执
	
	/*//任务队列[op_status状态字段]
	public static final String kqueue_received = "1";//任务已接收
	public static final String kqueue_committed = "2";//任务已提交
	public static final String kqueue_running = "3";//任务正在运行
	public static final String kqueue_failure = "5";//任务处理失败
	public static final String kqueue_success = "4";//任务处理成功
*/	
	//任务队列[op_status状态字段]
	public static final String kqueue_received = "1";//已接收
	public static final String kqueue_sended = "2";//已发送
	public static final String kqueue_failure = "3";//发送失败
	public static final String kqueue_success = "4";//发送成功
	
	public static final String KEY = "THREAD_LOCAL_KEY";
	
	public static final String DEFAULT_DIRSTATUS_OPEN = "1"; //新增目录页面目录状态默认值，‘1’开启， ‘2’关闭
	public static final String DEFAULT_DIRTYPE_EXSYS = "2"; //新增目录页面目录类型默认值，‘1’处理系统， ‘2’交换系统
	public static final String DEFAULT_DIRSIGNATURE_NONE = "1"; //新增目录页面目录签名默认值，‘1’无
	public static final String DEFAULT_DIRENCRYPTION_NONE = "1"; //新增目录页面目录加密默认值，‘1’无
	public static final Integer MAX_SEND_TIMES_PARAM_ID = 1; //在参数表中通过此主键值获取参数值
//	public static final String SEND_STATUS_FAIL = "3"; //发送状态--失败  跟字典表 发送状态 对应
	
	public static void main(String[] args) {
//		String tasknum = DataexSysTransParamService.getParamValue(Constants.taskNum);
//		System.out.println(tasknum);
//		System.out.println(Constants.successSleepTime);
//		System.out.println(Constants.taskNum);
		String request = DictMan.getDictType("dataex_sys_wsType", "1").getRemark();
		System.out.println(request+ "\n" + DictMan.getDictType("dataex_sys_wsType", "2").getRemark());
//		System.out.println(Constants.timeout);
//		
//		System.out.println(Constants.timeout);
		System.out.println(Constants.getThreadPoolSize());
		System.out.println(Constants.getThreadPoolSize());
	}
}
