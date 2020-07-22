package com.hnjz.apps.oa.dataexsys.util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 系统参数配置表:来自配置文件config.properties
 * @since 1.0
 */
public final class DataexsysConfiguration {
	private java.util.Properties props=new java.util.Properties();
	private static DataexsysConfiguration instance = null;
	private static Log  log = LogFactory.getLog(DataexsysConfiguration.class);
	public static synchronized DataexsysConfiguration getConfigurationManager() {
		if (instance == null) {
			instance = new DataexsysConfiguration();
			instance.init();
		}		 
		return instance;
	}
	private void init(){
		//先装载appconfig.properties文件
		try{
			props.load(DataexsysConfiguration.class.getResourceAsStream("config.properties"));
		}catch(Exception e){
			log.error("装载配置文件【config.properties】失败",e);
		}		
	}
 
	/**
	 * 获取配置参数
	 * @param key
	 * @return
	 */
	public String getSysConfigure(String key){
		return props.getProperty(key);
	}
	/**
	 * 获取配置参数，若参数不存在则返回默认值
	 * @param key  配置参数名称
	 * @param defaultValue  默认值
	 * @return
	 */
	public String getSysConfigure(String key,String defaultValue){
		return props.getProperty(key,defaultValue);
	}
	/**
	 * 获取int类型配置参数，若参数不存在则返回默认值
	 * @param key 配置参数名称
	 * @param defaultValue 默认值
	 * @return
	 */
	public int getSysConfigureAsInt(String key,int defaultValue){
		String cfg= props.getProperty(key);
		if(cfg==null)return defaultValue;
		else
		{
			return Integer.parseInt(cfg.trim());
		}
	}
	/**
	 *  获取boolean类型配置参数，若参数不存在则返回默认值
	 * @param key 配置参数名称
	 * @param defaultValue 默认值
	 * @return
	 */
	public boolean getSysConfigureAsBool(String key,boolean dv){
		String cfg= props.getProperty(key);
		if(cfg==null)return dv;
		else
		{
			return Boolean.parseBoolean(cfg.trim().toLowerCase());
		}
	}
	
	/**
	 * 删除特定系统参数配置
	 * @param key 参数名称
	 */
	public void removeSysConfigure(String key){
		 props.remove(key);
	}
	
	/**
	 * 更新参数配置
	 * @param key 参数名称
	 * @param value 字符串形式的参数值
	 */
	public void  updateSysConfigure(String key,String value){
		 props.setProperty(key, value);
	}
	
	/**
	 * 获取自定义属性类
	 * @return
	 */
	public static String getExpansionClass() {		 
		return DataexsysConfiguration.getConfigurationManager().getSysConfigure("expansion.name");
	}
	/**
	 * 获取自定义属性处理类
	 * @return
	 */
	public static String getExpansionProcessor() {		 
		return DataexsysConfiguration.getConfigurationManager().getSysConfigure("expansionprocessor.name");
	}
	/**
	 * 获取任务工厂类
	 * @return
	 */
	public static String getTaskFactory() {		 
		return DataexsysConfiguration.getConfigurationManager().getSysConfigure("taskfactory.name");
	}
	/**
	 * 获取回收任务类
	 * @return
	 */
	public static String getGcTask() {		 
		return DataexsysConfiguration.getConfigurationManager().getSysConfigure("gctask.name");
	}
	/**
	 * 接收单位匹配模式
	 * 1.接收单位identityFlag 一对一匹配
	 * 2.接收单位identityFlag 模糊匹配
	 * 3.接收单位identityDesc 一对一匹配
	 * @return
	 */
	public static String getReceiverMatchMode() {
		return DataexsysConfiguration.getConfigurationManager().getSysConfigure("receivermatch.mode","1");
	}
	
}