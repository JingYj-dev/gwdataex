package com.hnjz.db.query;

import com.hnjz.db.hibernate.HibernateUtil;
import com.hnjz.db.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.util.List;

/**
 * 保存、更新时不再存入缓存；同步机构时用
 * @author psy
 *
 */
public class TransactionNoCache {
	private Session session;
	private String mapping;
	private Transaction tx;

	public TransactionNoCache(String mapping, Session session) {
		this.session = null;
		this.mapping = null;
		this.tx = null;
		this.mapping = mapping;
		this.session = session;
	}

	public TransactionNoCache() {
		this("/hibernate.cfg.xml");
	}

	public TransactionNoCache(String mapping) {
		this.session = null;
		this.mapping = null;
		this.tx = null;
		this.mapping = mapping;
		boolean err = true;

		try {
			this.session = HibernateUtil.getTransactionSession(this.mapping);
			this.tx = this.session.beginTransaction();
			err = false;
		} finally {
			if (err) {
				this.closeSession();
			}

		}

	}

	public Session getSession() {
		return this.session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void closeSession() {
		try {
			HibernateUtil.closeTransactionSession(this.mapping);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void commit() throws HibernateException {
		if (this.tx != null) {
			this.tx.commit();
		}

		this.closeSession();
	}

	public void rollback() {
		try {
			if (this.tx != null) {
				this.tx.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.tx = null;
			this.closeSession();
		}
	}

	public void save(Object obj) throws HibernateException {
		if (obj != null) {
			this.session.save(obj);
		}

	}

	public void update(Object obj) throws HibernateException {
		if (obj != null) {
			this.session.update(obj);
		}

	}

	public void saveList(List lstObj) throws HibernateException {
		if ((lstObj == null) || (lstObj.size() <= 0)) {
			return;
		}
		Object obj = null;
		for(int i=0;i<lstObj.size();i++){
			obj = lstObj.get(i);
			if(obj != null){
				this.session.save(obj);
			}
			if(i%1000 == 0){
				this.session.flush();
				this.session.clear();
			}
		}
	}

	public void updateList(List lstObj) throws HibernateException {
		if ((lstObj == null) || (lstObj.size() <= 0)) {
			return;
		}
		Object obj = null;
		for(int i=0;i<lstObj.size();i++){
			obj = lstObj.get(i);
			if(obj != null){
				this.session.update(obj);
			}
			if(i%1000 == 0){
				this.session.flush();
				this.session.clear();
			}
		}
	}
}
