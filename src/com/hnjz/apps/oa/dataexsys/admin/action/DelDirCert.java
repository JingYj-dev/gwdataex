package com.hnjz.apps.oa.dataexsys.admin.action;

import com.hnjz.apps.base.common.upload.AttachItem;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.core.model.ModeFactory;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.oa.dataexsys.common.Constants;
import com.hnjz.apps.oa.dataexsysnode.model.DataexSysNode;
import com.hnjz.util.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.List;

public class DelDirCert extends AdminAction {
	
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(DelOfficialDocSys.class);
	
	private String uuid;
	private String attach;
	public DelDirCert() {
	}
	@Override
	protected String adminGo() {
		IServiceResult<?> res = delOfficialAttach(uuid, attach);
		result = res.toJson();
		return res.toActionResult();		
	}
	
	public IServiceResult<String> delOfficialAttach(String ids, String attach) {
		IServiceResult<String> res = null;
		String result = "";
		try {
			if (StringHelper.isEmpty(ids)) {
				return ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_FAILED, 
															Messages.getString("systemMsg.fieldEmpty"), result);
			}
			List<String> dictIdList = StringHelper.strToList(ids);
			if (dictIdList != null && dictIdList.size() > 0) {
				delAttach(dictIdList, attach, null, false);
			}
			res = ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_OK, 
									Messages.getString("systemMsg.success"), Json.list2json(dictIdList));
		} catch (Exception ex) {
			String msg = Messages.getString("systemMsg.dbFaild");
			log.error(msg, ex);
			res=ModeFactory.getModeFactory().buildNewServiceResult(IServiceResult.RESULT_ERROR, msg, result);
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public void delAttach(List<String> ids, String attach, TransactionCache tc, boolean onlyFile) throws Exception {
		try {
			List<DataexSysNode> list = QueryCache.idToObj(DataexSysNode.class, ids);
			if (list != null && list.size() > 0) {
				DataexSysNode temp = list.get(0);
				String file = temp.getCertPath();
				temp.setCertPath("");
				new TransactionCache().xupdate(temp);
				//String filepath = getAttachPath(Integer.parseInt(temp.getServerId()));	
				delAttachFile(file);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	public String getAttachPath(Integer serverId){
		//String dir = System.getProperty("user.dir");
		if(null == serverId){
			serverId = Integer.parseInt(Constants.RECV_SYS_DIR_ID);
		}
		return AttachItem.filePath(serverId);
	}
	
	public void delAttachFile(String file) throws Exception{
		File f = new File(file);
		if (f.exists())
			f.delete();
	}
	
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}


