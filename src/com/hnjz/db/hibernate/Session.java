package com.hnjz.db.hibernate;

import org.hibernate.*;

import java.io.Serializable;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:21
 */
public class Session {
    private StatelessSession innerSession = null;

    public Session(StatelessSession s) {
        this.innerSession = s;
    }

    public void close() {
        this.innerSession.close();
    }

    public void delete(Object arg0) {
        this.innerSession.delete(arg0);
    }

    public void delete(String arg0, Object arg1) {
        this.innerSession.delete(arg0, arg1);
    }

    public Object get(String arg0, Serializable arg1) {
        return this.innerSession.get(arg0, arg1);
    }

    public Object get(Class arg0, Serializable arg1) {
        return this.innerSession.get(arg0, arg1);
    }

    public Object get(String arg0, Serializable arg1, LockMode arg2) {
        return this.innerSession.get(arg0, arg1, arg2);
    }

    public Object get(Class arg0, Serializable arg1, LockMode arg2) {
        return this.innerSession.get(arg0, arg1, arg2);
    }

    public Serializable insert(Object arg0) {
        return this.innerSession.insert(arg0);
    }

    public Serializable save(Object arg0) {
        return this.innerSession.insert(arg0);
    }

    public Serializable insert(String arg0, Object arg1) {
        return this.innerSession.insert(arg0, arg1);
    }

    public void refresh(Object arg0) {
        this.innerSession.refresh(arg0);
    }

    public void refresh(String arg0, Object arg1) {
        this.innerSession.refresh(arg0, arg1);
    }

    public void refresh(Object arg0, LockMode arg1) {
        this.innerSession.refresh(arg0, arg1);
    }

    public void refresh(String arg0, Object arg1, LockMode arg2) {
        this.innerSession.refresh(arg0, arg1, arg2);
    }

    public void update(Object arg0) {
        this.innerSession.update(arg0);
    }

    public void update(String arg0, Object arg1) {
        this.innerSession.update(arg0, arg1);
    }

    public Transaction beginTransaction() {
        return this.innerSession.beginTransaction();
    }

    public Criteria createCriteria(Class arg0) {
        return this.innerSession.createCriteria(arg0);
    }

    public Criteria createCriteria(String arg0) {
        return this.innerSession.createCriteria(arg0);
    }

    public Criteria createCriteria(Class arg0, String arg1) {
        return this.innerSession.createCriteria(arg0, arg1);
    }

    public Criteria createCriteria(String arg0, String arg1) {
        return this.innerSession.createCriteria(arg0, arg1);
    }

    public Query createQuery(String arg0) {
        return this.innerSession.createQuery(arg0);
    }

    public SQLQuery createSQLQuery(String arg0) {
        return this.innerSession.createSQLQuery(arg0);
    }

    public Query getNamedQuery(String arg0) {
        return this.innerSession.getNamedQuery(arg0);
    }

    public Transaction getTransaction() {
        return this.innerSession.getTransaction();
    }

    public void clear() {
    }

    public void flush() {
    }

    public void evict(Object obj) {
    }
}
