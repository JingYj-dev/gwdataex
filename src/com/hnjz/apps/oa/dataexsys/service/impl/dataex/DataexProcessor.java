package com.hnjz.apps.oa.dataexsys.service.impl.dataex;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTrans;
import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.apps.oa.dataexsys.pkg.model.PkgData;
import com.hnjz.db.query.TransactionCache;

import java.util.List;

public abstract class DataexProcessor {
	public abstract String process(Object xml, ClientInfo clientInfo);
	public abstract List<DataexSysTransTask> saveTransData(TransactionCache tx, DataexSysTrans original, PkgData data)throws Exception;
	public abstract Identity getSender(PkgData data);
}
