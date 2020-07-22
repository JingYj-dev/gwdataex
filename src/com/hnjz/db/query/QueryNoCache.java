package com.hnjz.db.query;

import com.hnjz.db.hibernate.HibernateUtil;
import com.hnjz.db.hibernate.Session;

import java.util.List;

public class QueryNoCache {
    public String mapping = null;

    public QueryNoCache() {
        this.mapping = "/hibernate.cfg.xml";
    }

    public Session getSession() {
        return HibernateUtil.getFunctionSession(this.mapping);
    }

    public void closeSession() {
        HibernateUtil.closeSession(this.mapping);
    }
    public List getObjects(Class clazz, String sql) {
    	return getSession().createSQLQuery(sql).addEntity(clazz).list();
    }
    /*public List idToObject(Class clazz, List list) {
        if (list == null) {
            return null;
        } else {
            List ids = new ArrayList();

            for(int i = 0; i < list.size(); ++i) {
                Object o = this.getObject(clazz, (Serializable)list.get(i));
                if (o != null) {
                    ids.add(o);
                }
            }

            return ids;
        }
    }
    private Object getDB(Class clazz, Serializable id) {
        try {
            Object obj = this.getSession().get(clazz, id);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession(this.mapping);
        }
        return null;
    }
	public static List idToObj(Class clazz, List list) {
        return (new QueryNoCache()).idToObject(clazz, list);
    }
    public <T> T getObject(Class<T> clazz, Serializable id) {
        if (id == null) {
            return null;
        } else {
        	Object o = this.getDB(clazz, id);
        	if(o != null){
        		return (T) o;
        	}
        }
        return null;
    }*/
}
