/**
 * Copyright (c) Css Team
 * All rights reserved.
 *
 * This file UpdPost.java 
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
 * <p>更新岗位</p>
 * 
 */
public class UpdPost extends AdminAction implements ModelDriven {
	private static Log log = LogFactory.getLog(UpdPost.class);
	private SPost item = null;
	public UpdPost(){
		item = new SPost();
	}
	@Override
	protected String adminGo() {
		if (StringHelper.isEmpty(item.getUuid())||StringHelper.isEmpty(item.getName())) {
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.fieldEmpty"));
			return Action.ERROR;
		}
		TransactionCache tx = null;
		try {
			Object uuid = new QueryCache("select a.uuid from SPost a where a.name=:name and a.uuid!=:uuid")
				.setParameter("name",item.getName()).setParameter("uuid", item.getUuid()).setMaxResults(1).uniqueResult();
			if(uuid != null){
				result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, 
						Messages.getString("systemMsg.nameExist"));				
				return Action.ERROR;
			}
			
			SPost oldItem = QueryCache.get(SPost.class, item.getUuid());
			//判断是否改变原有岗位相关信息
			if(checkUpdate(oldItem, item)){
				oldItem.setName(item.getName());
				oldItem.setRemark(item.getRemark());
				tx = new TransactionCache();
				tx.update(oldItem);
				tx.commit();
				
				LogPart lp = new LogPart();		
				lp.setOpObjType(SPost.class.getName());
				lp.setOpObjId(item.getUuid());
				lp.setRelObjType(SPost.class.getName());
				lp.setRelObjId(item.getUuid());
				lp.setOpType(LogConstant.LOG_TYPE_MODIFY);
				lp.setLogLevel(LogConstant.LOG_LEVEL_COMMON);
				lp.setLogData(Json.object2json(oldItem));
				lp.setResult(LogConstant.RESULT_SUCCESS);
				lp.save();
				
			}
			result = Ajax.JSONResult(Environment.RESULT_CODE_SUCCESS, Messages.getString("systemMsg.success"));
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
	public boolean checkUpdate(String old,String tmp){
		if(old.equals(tmp)){
			return false;
		}else
			return true;
	}
	public boolean checkUpdate(SPost oldItem,SPost tmp){
		if(tmp.getName().equals(oldItem.getName())&& tmp.getRemark().equals(oldItem.getRemark()))
			return false;
		else{
			return true; 
		 }
	}
	@Override
	public Object getModel() {
		return item;
	}
	public SPost getItem() {
		return item;
	}
	public void setItem(SPost item) {
		this.item = item;
	}

}
