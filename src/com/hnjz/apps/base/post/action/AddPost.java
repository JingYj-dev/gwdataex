
/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file AddPost.java 
 * http://www.css.com.cn
 */
package com.hnjz.apps.base.post.action;

import com.hnjz.apps.base.log.model.LogConstant;
import com.hnjz.apps.base.log.model.LogPart;
import com.hnjz.apps.base.post.model.SPost;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.Json;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * <p>添加岗位</p>
 * 
 */
public class AddPost extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(AddPost.class);
	private SPost item;
	public AddPost() { 
		this.item = new SPost();
	}
	@Override
	protected String adminGo() {
		// TODO Auto-generated method stub
		TransactionCache tx = null;
		try {
			if(StringHelper.isEmpty(item.getName())){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
				return Action.ERROR;
			}
			
			Object uuid = new QueryCache("select a.uuid from SPost a where a.name=:name ")
				.setParameter("name",item.getName()).setMaxResults(1).uniqueResult();
			if(uuid != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.nameExist"));				
				return Action.ERROR;
			}
			
			tx = new TransactionCache();
			tx.save(item);
			tx.commit();
			
			LogPart lp = new LogPart();		
			lp.setOpObjType(SPost.class.getName());
			lp.setOpObjId(item.getUuid());
			lp.setRelObjType(SPost.class.getName());
			lp.setRelObjId(item.getUuid());
			lp.setOpType(LogConstant.LOG_TYPE_ADD);
			lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
			lp.setLogData(Json.object2json(item));
			lp.setResult(LogConstant.RESULT_SUCCESS);
			lp.save();
			
			//LogMan.addLog(getSLog(funcid, item));
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"), item.getUuid());
			return Action.SUCCESS;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			if (tx != null) {
				tx.rollback();
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));		
			return Action.ERROR;
		}
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return item;
	}

	public SPost getItem() {
		return item;
	}

	public void setItem(SPost item) {
		this.item = item;
	}

}
