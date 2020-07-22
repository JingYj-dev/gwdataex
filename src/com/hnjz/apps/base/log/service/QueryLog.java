package com.hnjz.apps.base.log.service;

import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;

import java.io.Serializable;
import java.util.List;

public class QueryLog extends QueryCache {
	private static final String MAPPING = "/hibernate_log.cfg.xml";
	public QueryLog(String hql, boolean naTive) {
		super(MAPPING, hql, naTive);
	}
	public QueryLog(String hql) {
		super(MAPPING, hql);
	}
	public QueryLog() {
		super(MAPPING, null);
	}
	public static <T> T get(Class<T> clazz, Serializable id) {
		return new QueryLog().getObject(clazz, id);
	}
	public static List idToObj(Class clazz, List list) {
		return new QueryLog().idToObject(clazz, list);
	}
	public TransactionCache getTransaction() {
		return new TransactionCache(MAPPING);
	}
}
