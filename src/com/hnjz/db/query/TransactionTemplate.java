package com.hnjz.db.query;

import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.query.TransactionCallback;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:46
 */
public class TransactionTemplate {
    protected TransactionCache transaction;

    public TransactionTemplate(TransactionCache transaction) {
        this.transaction = transaction;
    }

    public void execute(TransactionCallback action) throws Exception {
        try {
            action.run();
            this.transaction.commit();
        } catch (Exception var3) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }

            throw var3;
        }
    }
}
