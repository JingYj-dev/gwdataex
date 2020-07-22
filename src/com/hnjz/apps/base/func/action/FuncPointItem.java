package com.hnjz.apps.base.func.action;

import com.hnjz.apps.base.func.model.SFunc;
import com.hnjz.apps.base.func.model.SFuncAction;
import com.hnjz.core.model.DefaultActionLog;
import com.hnjz.core.model.IDictable;
import com.hnjz.db.query.QueryCache;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.FunctionManager;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncPointItem {
	public static SFunc getFunc(String uuid) {
		return QueryCache.get(SFunc.class, uuid);
	}

	public static List<SFuncAction> getFuncActionByCode(String funcCode) {
		QueryCache qc = new QueryCache(
				" select a.uuid from SFuncAction a where a.funcCode=:funcCode ")
				.setParameter("funcCode", funcCode);
		return QueryCache.idToObj(SFuncAction.class, qc.list());
	}
	
	/**
	 * t为列表框的text,v为列表框的value，s表示子一级对象
	 */
	public static final String KEY = "v";
	public static final String VALUE = "t";
	public static final String SUB = "s";

	private static String funcPointJsonStrTemp;

	/**
	 * 获取功能项的jsonStr
	 * 
	 * @return
	 */
	public static String getFuncPointJsonStr() {
//		if (StringHelper.isEmpty(funcPointJsonStrTemp)) {
			// funcPointJsonStrTemp =
			// StringHelper.getStrRemoveEscapeStr(load().toString());
//			funcPointJsonStrTemp = load().toString();
//		}
		return funcPointJsonStrTemp;
	}

	protected static JSONArray load() {
		// 系统级别
		List sysList = new ArrayList();
		List<Map<String,String>> sysIds = getSysIds();
		if (null != sysIds) {
			for (Map<String,String> sys : sysIds) {
				String sysId = sys.get("id");
				Map sysMap = new HashMap();
				sysMap.put(KEY, format(sysId));
				sysMap.put(VALUE, format(sysId));
				// 功能包级别
				List<IDictable> packages = getPackages(sysId);
				if (null != packages) {
					List packageList = new ArrayList();
					for (IDictable pakg : packages) {
						Map packageMap = new HashMap();
						packageMap.put(KEY, format(pakg.getItemCode()));
						packageMap.put(VALUE, format(pakg.getItemName()));
						// 功能项级别
						List<DefaultActionLog> actions = getActions(sysId, pakg
								.getItemCode());
						if (null != actions) {
							List actionList = new ArrayList();
							for (DefaultActionLog action : actions) {
								Map actionMap = new HashMap();
								actionMap.put(KEY, format(pakg.getItemCode()
										+ "/" + action.getActionName()));
								actionMap.put(VALUE, format(action
										.getActionDesc()));
								actionList.add(actionMap);
							}
							packageMap.put(SUB, actionList);
						}
						packageList.add(packageMap);
					}
					sysMap.put(SUB, packageList);
				}
				sysList.add(sysMap);
			}
		}
		return JSONArray.fromObject(sysList);
	}

	private static String format(String s) {
		if (StringHelper.isEmpty(s)) {
			return " ";
		}
		return s;
	}

	public static List<Map<String,String>> getSysIds() {
		return FunctionManager.getFunctionManager().getSysIds();
	}

	public static List<IDictable> getPackages(String sysId) {
		return FunctionManager.getFunctionManager().getPackages(sysId);
	}

	public static List<DefaultActionLog> getActions(String sysId,
			String packageId) {
		if(StringHelper.isEmpty(sysId)){return null;}
		Map<String, List<DefaultActionLog>> actionMap = FunctionManager
			.getFunctionManager().getActionMap(sysId);
		if (null == actionMap) {return null;}
		if(StringHelper.isEmpty(packageId)){
			List<DefaultActionLog> result = new ArrayList<DefaultActionLog>();
			for(List<DefaultActionLog> actions : actionMap.values()){
				result.addAll(actions);
			}
			return result;
		}
		return actionMap.get(packageId);
	}
	
	
	public static List<DefaultActionLog> getActions(String sysId) {
		return getActions(sysId, null);
	}

	public static void main(String[] args) {
		System.out.println(getFuncPointJsonStr());
		System.out.println(getFuncPointJsonStr());
	}


}
