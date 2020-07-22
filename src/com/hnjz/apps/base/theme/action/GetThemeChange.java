package com.hnjz.apps.base.theme.action;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.core.configuration.Environment;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.Ajax;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.webwork.action.UserAction;
import com.hnjz.apps.base.theme.model.Theme;
import com.hnjz.util.Messages;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class GetThemeChange extends UserAction implements ModelDriven{
	private static final long serialVersionUID = 1L;
	private Theme theme;
	private static Log log = LogFactory.getLog(GetThemeChange.class);
	private List<Integer> pageNums;//记录是第几页
	private Page page;
	private String flag;
	public GetThemeChange(){
		page = new Page();
		theme = new Theme();
		page.setCountField("a.uuid");
		page.setPageSize(6);
		pageNums = new ArrayList<Integer>();
	}
	@Override
	protected String userGo() {
		try{
			QueryCache qc = new QueryCache("select a.uuid  from Theme a "+getWhere());
			setWhere(qc);
			page = qc.page(page);
			page.setResults(QueryCache.idToObj(Theme.class,page.getResults()));
			//主题分页点的个数
			for(int i = 1;i <= page.getTotalPages(); i++){
					pageNums.add(i);
			}
			if(StringHelper.isEmpty(theme.getTypeId()) && StringHelper.isEmpty(flag)){
				
				return Action.SUCCESS;
			
			}else{
				
				return "minipage";
			}
	
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			result = Ajax.JSONResult(Environment.RESULT_CODE_ERROR, Messages.getString("systemMsg.exception"));
			return Action.ERROR;
		}
	}
	public String getWhere(){
		StringBuffer sb = new StringBuffer(" where 1=1 and a.openFlag = 1 ");
		List themTypes = DictMan.getDictType("theme_type");
		if(StringHelper.isEmpty(theme.getTypeId()) && themTypes != null && themTypes.size() > 0 ){
			sb.append(" and a.typeId = 1 ");
		}else if(StringHelper.isNotEmpty(theme.getTypeId())){
			sb.append(" and a.typeId =:typeId ");
		}
		return sb.toString();
	}
	public void setWhere(QueryCache qc){
		if(StringHelper.isNotEmpty(theme.getTypeId())){
			qc.setParameter("typeId", theme.getTypeId());
		}
	}
	@Override
	public Object getModel() {
		return theme;
	}
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<Integer> getPageNums() {
		return pageNums;
	}
	public void setPageNums(List<Integer> pageNums) {
		this.pageNums = pageNums;
	}
	
}
