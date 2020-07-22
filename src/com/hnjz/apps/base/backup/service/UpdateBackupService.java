package com.hnjz.apps.base.backup.service;

import com.hnjz.apps.base.backup.model.SDataBackup;
import com.hnjz.db.query.TransactionCache;

public class UpdateBackupService {

	public void updateBackup(SDataBackup sdb){
		TransactionCache tc = null;
		try {
			tc = new TransactionCache();
			tc.update(sdb);
			tc.commit();
		} catch (Exception e) {
			if (tc != null)
				tc.rollback();
			e.printStackTrace();
		}
	}
}
