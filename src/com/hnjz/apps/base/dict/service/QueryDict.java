package com.hnjz.apps.base.dict.service;

import com.hnjz.db.query.QueryCache;
import com.hnjz.db.query.TransactionCache;

import java.io.Serializable;
import java.util.List;

public class QueryDict extends QueryCache {
	public static final String MAPPING = "/hibernate_dict.cfg.xml";
	public QueryDict(String hql, boolean naTive) {
		super(MAPPING, hql, naTive);
	}
	public QueryDict(String hql) {
		super(MAPPING, hql);
	}
	public QueryDict() {
		super(MAPPING, null);
	}
	public static <T> T get(Class<T> clazz, Serializable id) {
		return new QueryDict().getObject(clazz, id);
	}
	public static List idToObj(Class clazz, List list) {
		return new QueryDict().idToObject(clazz, list);
	}
	public TransactionCache getTransaction() {
		return new TransactionCache(MAPPING);
	}
}
