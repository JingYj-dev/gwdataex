package com.hnjz.apps.base.dict.service;

import com.hnjz.apps.base.dict.model.SDict;
import com.hnjz.core.model.IServiceResult;
import com.hnjz.core.model.ModeFactory;
import com.hnjz.util.StringHelper;
import com.hnjz.util.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DictService {
	private static Log log = LogFactory.getLog(DictService.class);

	public static IServiceResult<String> addDict(SDict dict) {
		IServiceResult<String> res = null;
		String result = "";
		// TODO 数据验证最好有个通用的处理，提高开发效率
		if (dict == null || StringHelper.isEmpty(dict.getCode())
				|| StringHelper.isEmpty(dict.getName())
				|| StringHelper.isEmpty(dict.getParentId())
				|| StringHelper.isEmpty(dict.getTableName())) {
			// String result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
			// Messages.getString("systemMsg.fieldEmpty"));
			res = ModeFactory.getModeFactory().buildNewServiceResult(
					IServiceResult.RESULT_FAILED,
					Messages.getString("systemMsg.fieldEmpty"), result);
		} else {
			Object uuid = new QueryDict(
					"select a.uuid from SDict a where a.tableName = :tableName and a.code =:code and a.uuid!=:uuid ")
					.setParameter("tableName", dict.getTableName()).setParameter(
							"code", dict.getCode()).setParameter("uuid", dict.getUuid()).setMaxResults(1).uniqueResult();
			if (uuid != null) {
				res = ModeFactory.getModeFactory().buildNewServiceResult(
						IServiceResult.RESULT_FAILED,
						Messages.getString("sysmgrMsg.idExist"), result);
			} else {
				try {
					dict.setUuid(DictMan.getUuid(dict.getTableName(),
							dict.getCode()));
					DictMan.addDict(dict);
					// String result =
					// Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS,
					// Messages.getString("systemMsg.success"));
					res = ModeFactory.getModeFactory().buildNewServiceResult(
							IServiceResult.RESULT_OK,
							Messages.getString("systemMsg.success"), result);
				} catch (Throwable ex) {
					// String result =
					// Ajax.JSONResult(Environment.RESULT_CODE_ERROR,
					// Messages.getString("systemMsg.dbFaild"));
					log.error(ex.getMessage(), ex);
					res = ModeFactory.getModeFactory().buildNewServiceResult(
							IServiceResult.RESULT_FAILED,
							Messages.getString("systemMsg.dbFaild"), result);
				}
			}
		}
		return res;
	}
}
