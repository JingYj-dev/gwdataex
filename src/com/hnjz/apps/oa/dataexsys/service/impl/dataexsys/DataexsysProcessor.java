package com.hnjz.apps.oa.dataexsys.service.impl.dataexsys;

import com.hnjz.apps.oa.dataexsys.admin.model.DataexSysTransTask;
import com.hnjz.apps.oa.dataexsys.common.ClientInfo;
import com.hnjz.apps.oa.dataexsys.pkg.model.Identity;
import com.hnjz.apps.oa.dataexsys.pkg.model.PkgData;
import com.hnjz.db.query.TransactionCache;

public abstract class DataexsysProcessor {
	public abstract String process(Object xml, ClientInfo clientInfo);
	public abstract DataexSysTransTask saveTransData(TransactionCache tx,PkgData data, ClientInfo clientInfo)throws Exception;
	public abstract Identity getSender(PkgData data);
}
