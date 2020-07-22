package com.hnjz.apps.oa.dataexsys.service.expansion;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransContent;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.pkg.model.Expansion;

public interface IExpansionProcessor {
	
	public void unpkg(DataexSysTransContent transContent,
                      DataexSysTransTask task, Expansion expansion, String[] args);

	public Expansion pkg(DataexSysTransContent transContent,
                         DataexSysTransTask task, Expansion expansion);
}
