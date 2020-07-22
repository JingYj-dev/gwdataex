package com.hnjz.apps.base.common;

import com.hnjz.apps.base.dict.service.DictMan;
import com.hnjz.common.plugins.impl.AbstractPlugin;
import com.hnjz.core.model.IDictable;
import com.hnjz.core.plugins.base.IDictProvider;

import java.util.List;

public class DictProvider extends AbstractPlugin implements  IDictProvider{
	public IDictable getDictType(String table, String dictId) {		 
		return DictMan.getDictType(table, dictId);
	}
	public List<? extends IDictable> getDictType(String table) {		
		return DictMan.getDictType(table);		
	}
	@SuppressWarnings("unchecked")
	public List<? extends IDictable> getDictListQuery(String table) {
		return DictMan.getDictListQuery(table);
	}
	@SuppressWarnings("unchecked")
	public List<? extends IDictable> getDictListSel(String table) {
		return DictMan.getDictListSel(table);
	}
}
