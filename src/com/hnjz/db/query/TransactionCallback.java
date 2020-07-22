package com.hnjz.db.query;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:45
 */
public abstract class TransactionCallback {
    public TransactionCallback() {
    }

    protected abstract void run() throws Exception;
}
