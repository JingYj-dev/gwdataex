package com.hnjz.apps.oa.dataexsys.service.task;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.util.DateUtil;
import com.hnjz.util.FileUtil;
import com.hnjz.util.UuidUtil;

import java.util.Calendar;
import java.util.concurrent.Callable;

public final class RecordFailDataTask implements Callable<String> {
		private String data;
		public RecordFailDataTask(String data) {
			this.data = data;
		}
		@Override
		public String call() throws Exception {
			recordFailData(data);
			return Constants.SUCCESS;
		}
		private void recordFailData(String data) throws Exception {
			String uuid = UuidUtil.getUuid();
			String pattern = "yyyyMMddHHmmss";
			String dateStr = DateUtil.getDateStr(Calendar.getInstance().getTime(), pattern);
			Integer nServerId = Integer.parseInt(Constants.RECV_SYS_DIR_ID);
			String filepath = AttachItem.filePath(nServerId);
			filepath += "fail/";
			String filename = dateStr + "_" + uuid;
			String sFile = filepath + filename;
			FileUtil.mkfile(sFile);
			//写入请求数据
			FileUtil.getFileFromBytes(data.getBytes(Constants.charset), sFile);
		}
	}