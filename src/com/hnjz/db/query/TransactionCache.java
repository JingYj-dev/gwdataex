package com.hnjz.db.query;

import com.hnjz.db.hibernate.DBMapping;
import com.hnjz.db.hibernate.HibernateUtil;
import com.hnjz.db.hibernate.Session;
import com.hnjz.db.query.IAtom;
import com.hnjz.db.query.QueryCache;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.metadata.ClassMetadata;

import java.io.Serializable;
import java.util.*;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:44
 */
public class TransactionCache {
    private Session session;
    private String mapping;
    private Transaction tx;
    private List<Object> objList;

    public TransactionCache(String mapping, Session session) {
        this.session = null;
        this.mapping = null;
        this.tx = null;
        this.objList = new ArrayList();
        this.mapping = mapping;
        this.session = session;
    }

    public TransactionCache() {
        this("/hibernate.cfg.xml");
    }

    public TransactionCache(String mapping) {
        this.session = null;
        this.mapping = null;
        this.tx = null;
        this.objList = new ArrayList();
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
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void executeUpdateSQL(String hql) throws HibernateException {
        this.session.createSQLQuery(hql).executeUpdate();
    }

    public void executeUpdate(String hql) throws HibernateException {
        this.session.createQuery(hql).executeUpdate();
    }

    public void executeUpdateSQL(String sql, Map<String, Object> params) throws HibernateException {
        SQLQuery query = this.session.createSQLQuery(sql);
        if (params != null) {
            Iterator var5 = params.entrySet().iterator();

            label21:
            while(true) {
                while(true) {
                    if (!var5.hasNext()) {
                        break label21;
                    }

                    Map.Entry<String, Object> entry = (Map.Entry)var5.next();
                    Object value = entry.getValue();
                    if (value != null && Collection.class.isAssignableFrom(entry.getValue().getClass())) {
                        query.setParameterList((String)entry.getKey(), (Collection)entry.getValue());
                    } else {
                        query.setParameter((String)entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        query.executeUpdate();
    }

    public void executeUpdate(String hql, Map<String, Object> params) throws HibernateException {
        Query query = this.session.createQuery(hql);
        if (params != null) {
            Iterator var5 = params.entrySet().iterator();

            label21:
            while(true) {
                while(true) {
                    if (!var5.hasNext()) {
                        break label21;
                    }

                    Map.Entry<String, Object> entry = (Map.Entry)var5.next();
                    Object value = entry.getValue();
                    if (value != null && Collection.class.isAssignableFrom(entry.getValue().getClass())) {
                        query.setParameterList((String)entry.getKey(), (Collection)entry.getValue());
                    } else {
                        query.setParameter((String)entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        query.executeUpdate();
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
        } catch (Exception var7) {
            var7.printStackTrace();
        } finally {
            this.tx = null;
            this.closeSession();
            Iterator var4 = this.objList.iterator();

            while(true) {
                if (!var4.hasNext()) {
                    ;
                } else {
                    Object obj = var4.next();
                    if (obj != null) {
                        (new com.hnjz.db.query.QueryCache(this.mapping, (String)null)).remove(obj);
                    }
                }
            }
        }

    }

    public void save(Object obj) throws HibernateException {
        if (obj != null) {
            this.session.save(obj);
            this.objList.add(obj);
            (new com.hnjz.db.query.QueryCache(this.mapping, (String)null)).set(obj);
        }

    }

    public void update(Object obj) throws HibernateException {
        if (obj != null) {
            this.session.update(obj);
            this.objList.add(obj);
            (new com.hnjz.db.query.QueryCache(this.mapping, (String)null)).set(obj);
        }

    }

    public void delete(Object obj) throws HibernateException {
        if (obj != null) {
            this.session.delete(obj);
            (new com.hnjz.db.query.QueryCache(this.mapping, (String)null)).remove(obj);
        }

    }

    public void delete(Class clazz, Serializable idKey) throws HibernateException {
        if (clazz != null && idKey != null) {
            DBMapping dbMapping = HibernateUtil.getDBmapping(this.mapping);
            ClassMetadata cm = dbMapping.getSessionFactory().getClassMetadata(clazz);
            String keyPropName = cm.getIdentifierPropertyName();
            String entityName = clazz.getSimpleName();
            String hql = "DELETE FROM " + entityName + " WHERE " + keyPropName + " =  :key";
            this.session.createQuery(hql).setParameter("key", idKey).executeUpdate();
            (new com.hnjz.db.query.QueryCache(this.mapping, (String)null)).remove(clazz, idKey);
        }

    }

    public void delete(Class clazz, List idKeys) throws HibernateException {
        if (clazz != null && idKeys != null && !idKeys.isEmpty()) {
            DBMapping dbMapping = HibernateUtil.getDBmapping(this.mapping);
            ClassMetadata cm = dbMapping.getSessionFactory().getClassMetadata(clazz);
            String keyPropName = cm.getIdentifierPropertyName();
            String entityName = clazz.getSimpleName();
            String hql = "DELETE FROM " + entityName + " WHERE " + keyPropName + " in (:key) ";
            this.session.createQuery(hql).setParameterList("key", idKeys).executeUpdate();
            (new QueryCache(this.mapping, (String)null)).remove(clazz, idKeys);
        }

    }

    public void save(List lstObj) throws HibernateException {
        if (lstObj != null && lstObj.size() > 0) {
            Iterator var3 = lstObj.iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                this.save(obj);
            }

        }
    }

    public void update(List lstObj) throws HibernateException {
        if (lstObj != null && lstObj.size() > 0) {
            Iterator var3 = lstObj.iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                this.update(obj);
            }

        }
    }

    public void delete(List lstObj) throws HibernateException {
        if (lstObj != null && lstObj.size() > 0) {
            Iterator var3 = lstObj.iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                this.delete(obj);
            }

        }
    }

    public void xsave(List lstObj) throws HibernateException {
        try {
            this.save(lstObj);
            this.commit();
        } catch (Exception var3) {
            this.rollback();
            if (var3 instanceof HibernateException) {
                throw (HibernateException)var3;
            } else {
                throw new HibernateException(var3);
            }
        }
    }

    public void xsave(Object obj) throws HibernateException {
        try {
            this.save(obj);
            this.commit();
        } catch (Exception var3) {
            this.rollback();
            if (var3 instanceof HibernateException) {
                throw (HibernateException)var3;
            } else {
                throw new HibernateException(var3);
            }
        }
    }

    public void xupdate(Object obj) throws HibernateException {
        try {
            this.update(obj);
            this.commit();
        } catch (Exception var3) {
            this.rollback();
            if (var3 instanceof HibernateException) {
                throw (HibernateException)var3;
            } else {
                throw new HibernateException(var3);
            }
        }
    }

    public void xdelete(Object obj) throws HibernateException {
        try {
            this.delete(obj);
            this.commit();
        } catch (Exception var3) {
            this.rollback();
            if (var3 instanceof HibernateException) {
                throw (HibernateException)var3;
            } else {
                throw new HibernateException(var3);
            }
        }
    }

    public void xupdate(List lstObj) throws HibernateException {
        try {
            this.update(lstObj);
            this.commit();
        } catch (Exception var3) {
            this.rollback();
            if (var3 instanceof HibernateException) {
                throw (HibernateException)var3;
            } else {
                throw new HibernateException(var3);
            }
        }
    }

    public void xdelete(List lstObj) throws HibernateException {
        try {
            this.delete(lstObj);
            this.commit();
        } catch (Exception var3) {
            this.rollback();
            if (var3 instanceof HibernateException) {
                throw (HibernateException)var3;
            } else {
                throw new HibernateException(var3);
            }
        }
    }

    public Object executeTrans(IAtom atom) throws HibernateException {
        Object result = null;

        try {
            result = atom.execute(this);
            this.commit();
            return result;
        } catch (Exception var4) {
            this.rollback();
            if (var4 instanceof HibernateException) {
                throw (HibernateException)var4;
            } else {
                throw new HibernateException(var4);
            }
        }
    }
}
