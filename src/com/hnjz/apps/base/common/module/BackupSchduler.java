package com.hnjz.apps.base.common.module;

import com.hnjz.apps.base.backup.service.AddBackupService;
import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.common.plugins.IDisposable;
import com.hnjz.common.plugins.Initializable;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;

import java.util.Map;
import java.util.TimerTask;

public class BackupSchduler extends AbstractConfigurablePlugin implements
		IDisposable, Initializable {
    private java.util.Timer timer=null;
    private long schInterval = -1;
	@Override
	protected void doConfig(Map<String, String> arg0) {
		String interval = arg0.get("interval");
		if(interval!=null){
			schInterval = Long.parseLong(interval);
		}		
	}

	@Override
	public void dispose() {
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
	}

	@Override
	public void initialize() {
		timer = new java.util.Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				try {
					//设置服务器IP和主机名			 		 
		 			java.net.InetAddress addr=java.net.InetAddress.getLocalHost();
		 			String ip = addr.getHostAddress();
		 			SDict dict= DictMan.getDictType("d_para_g", "69");
					if(dict!=null && ip.equals(dict.getName())){
						 return;
					}
					AddBackupService db=new AddBackupService();
					db.addBackup();
				} catch (Exception e) {					 
					e.printStackTrace();
				}				
			}
			
		}, 10000, schInterval*60*24*1000);
	}

}
