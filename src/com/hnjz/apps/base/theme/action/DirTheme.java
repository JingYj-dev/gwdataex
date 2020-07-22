package com.hnjz.apps.base.theme.action;

import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.AdminAction;
import com.hnjz.apps.base.theme.model.Theme;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class DirTheme extends AdminAction implements ModelDriven{
	private static final long serialVersionUID = 1L;
	private static Log log= LogFactory.getLog(DirTheme.class);
	private Theme item;
	private Page page;
	public DirTheme(){
		item = new Theme();
		page = new Page();
		page.setCountField("a.uuid");
	}
	@Override
	protected String adminGo() {
		try{

			QueryCache qc = new QueryCache("select a.uuid from Theme a "+getWhere()+getOrder());
			setWhere(qc);
			page = qc.page(page);
			List<String> themeIds = page.getResults();
			List<Theme> themeList = QueryCache.idToObj(Theme.class, themeIds);
			page.setResults(themeList);
			return Action.SUCCESS;
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}

	public String getWhere(){
		StringBuffer sb = new StringBuffer("where 1=1");
		if(StringHelper.isNotEmpty(item.getCode())){
			sb.append(" and a.code like :code ");
		}
		if(StringHelper.isNotEmpty(item.getName())){
			sb.append("and a.name like :name ");
		}
		if(StringHelper.isNotEmpty(item.getTypeId())){
			sb.append("and a.typeId=:typeId ");
		} 
		if(StringHelper.isNotEmpty(item.getOpenFlag())){
			sb.append("and a.openFlag=:openFlag");
		}
		return sb.toString();
	}
	public void setWhere(QueryCache qc){
		if(StringHelper.isNotEmpty(item.getCode())){
			qc.setParameter("code", "%"+ item.getCode()+"%");
		}
		if(StringHelper.isNotEmpty(item.getName())){
			qc.setParameter("name", "%"+item.getName()+"%");
		}
		if(StringHelper.isNotEmpty(item.getTypeId())){
			qc.setParameter("typeId", item.getTypeId());
		} 
		if(StringHelper.isNotEmpty(item.getOpenFlag())){
			qc.setParameter("openFlag",item.getOpenFlag());
		}
	}
	public String getOrder(){
		return StringHelper.isEmpty(page.getOrderString())?" order by a.orderNum":page.getOrderByString();
	}
	@Override
	public Object getModel() {
		return item;
	}
	public Theme getItem() {
		return item;
	}
	public void setItem(Theme item) {
		this.item = item;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

}
