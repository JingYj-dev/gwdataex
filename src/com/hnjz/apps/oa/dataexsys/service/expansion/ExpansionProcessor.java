package com.hnjz.apps.oa.dataexsys.service.expansion;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.pkg.model.Expansion;

public class ExpansionProcessor implements IExpansionProcessor {
	
	public void unpkg(DataexSysTransContent transContent,
			DataexSysTransTask task, Expansion expansion, String[] args) {
		if(transContent==null || task==null || expansion==null){
			return;
		}
		transContent.setExpansion(expansion.asXML());
	}

	@Override
	public Expansion pkg(DataexSysTransContent transContent,
			DataexSysTransTask task, Expansion expansion) {
		if(expansion==null){
			expansion = new Expansion();
		}
		expansion.load(transContent.getExpansion());
		return expansion;
	}
}
