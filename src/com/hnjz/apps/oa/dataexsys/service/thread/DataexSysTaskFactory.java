package com.hnjz.apps.oa.dataexsys.service.thread;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysDir;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.cas.QueueUtil;
import com.hnjz.apps.oa.dataexsys.service.task.TaskHelper;
import com.hnjz.db.query.QueryCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataexSysTaskFactory implements TaskFactory {
	
	@Override
	public List<AbstractTask> getTask() {
		List<AbstractTask> taskLst = new ArrayList<AbstractTask>();
		String resType = QueueUtil.QUEUE_DATAEXSYS;
		DataexSysTransTask res = QueueUtil.getDataFromQueue(resType);
		if (res != null) {
			try {
				//封装Map任务
				Map<String, Object> taskMap = new HashMap<String, Object>();
				//根据recvOrg查找接收方服务地址
				DataexSysDir dir = TaskHelper.findDataexSysDir(res.getReceiver());
				//存储处理后公文内容
				DataexSysTransContent content = QueryCache.get(DataexSysTransContent.class, res.getContentId());
				taskMap.put("targetInfo", dir);
				taskMap.put("gwContent", content);
				taskMap.put("recvOrg", new String[]{ res.getTargetOrg(), res.getTargetOrgName() ,res.getTargetOrgDesc()});
				taskMap.put("kqueue", res);
				//封装任务
				AbstractTask task = DataexSysTask.getAbstractTask(taskMap);
				taskLst.add(task);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return taskLst;
	}

}
