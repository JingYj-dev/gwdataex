package com.hnjz.db.query;

import com.hnjz.db.query.QueryCache;

import java.io.Serializable;
import java.util.List;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:43
 */
public class JoinList {
    protected Class clazz = null;
    protected QueryCache qc = null;

    public JoinList(Class clazz, QueryCache qc) {
        this.clazz = clazz;
        this.qc = qc;
    }

    public List getListById() {
        return this.qc.listCache(-1);
    }

    public List getList() {
        return QueryCache.idToObj(this.clazz, this.getListById());
    }

    public void add(Serializable id) {
        this.qc.listCacheAdd(id);
    }

    public void removeAll() {
        this.qc.listCacheRemove();
    }

    public void remove(Serializable id) {
        this.qc.listCacheRemove(id);
    }
}
