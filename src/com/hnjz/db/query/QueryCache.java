package com.hnjz.db.query;

import com.hnjz.db.config.ConfigurationManager;
import com.hnjz.db.hibernate.DBMapping;
import com.hnjz.db.hibernate.HibernateUtil;
import com.hnjz.db.hibernate.Session;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.db.page.Page;
import com.hnjz.db.query.TransactionCache;
import com.hnjz.db.util.Md5Util;
import org.hibernate.EntityMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.*;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:43
 */
public class QueryCache {
    private Query query;
    private String hql;
    private int maxResult = 0;
    public String mapping = null;
    private boolean naTive = false;
    private Map<String, Object> param = new HashMap();
    private Map<String, Type> scalar = new HashMap();
    private Map<String, Class> entity = new HashMap();
    private static final String MAPPING = "/hibernate.cfg.xml";

    public QueryCache(String hql, boolean naTive) {
        this.mapping = "/hibernate.cfg.xml";
        this.naTive = naTive;
        this.hql = hql;
    }

    public QueryCache(String hql) {
        this.mapping = "/hibernate.cfg.xml";
        this.hql = hql;
    }

    public QueryCache() {
        this.mapping = "/hibernate.cfg.xml";
    }

    public static <T> T get(Class<T> clazz, Serializable id) {
        return (new QueryCache()).getObject(clazz, id);
    }

    public static <T> T get(String className, Serializable id) {
        try {
            return (T) (new QueryCache()).getObject(Class.forName(className), id);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static List idToObj(Class clazz, List list) {
        return (new com.hnjz.db.query.QueryCache()).idToObject(clazz, list);
    }

    public TransactionCache getTransaction() {
        return new TransactionCache(this.mapping);
    }

    public Session getSession() {
        return HibernateUtil.getFunctionSession(this.mapping);
    }

    public void closeSession() {
        HibernateUtil.closeSession(this.mapping);
    }

    public QueryCache(String mapping, String hql, boolean naTive) {
        this.mapping = mapping;
        this.naTive = naTive;
        this.hql = hql;
    }

    public QueryCache(String mapping, String hql) {
        this.mapping = mapping;
        this.hql = hql;
    }

    public com.hnjz.db.query.QueryCache setParameter(String key, Object value) {
        this.param.put(key, value);
        return this;
    }

    public com.hnjz.db.query.QueryCache addScalar(String key, Type type) {
        this.scalar.put(key, type);
        return this;
    }

    public com.hnjz.db.query.QueryCache addEntity(String key, Class clazz) {
        this.entity.put(key, clazz);
        return this;
    }

    public com.hnjz.db.query.QueryCache setMaxResults(int maxResult) {
        this.maxResult = maxResult;
        return this;
    }

    private void setQueryParameter(Query query, String key, Object value) {
        if (value instanceof Collection) {
            query.setParameterList(key, (Collection)value);
        } else if (value instanceof Object[]) {
            query.setParameterList(key, (Object[])value);
        } else {
            query.setParameter(key, value);
        }

    }

    private void createQuery() {
        this.query = this.createQuery(this.hql);
        if (this.maxResult > 0) {
            this.query.setMaxResults(this.maxResult);
        }

    }

    private Query createQuery(String hql) {
        Query query = this.naTive ? this.getSession().createSQLQuery(hql) : this.getSession().createQuery(hql);
        Object[] keys = this.param.keySet().toArray();
        Object value = null;

        int i;
        for(i = 0; i < keys.length; ++i) {
            value = this.param.get(keys[i]);
            this.setQueryParameter((Query)query, keys[i].toString(), value);
        }

        if (this.naTive) {
            if (this.scalar.size() > 0) {
                keys = this.scalar.keySet().toArray();

                for(i = 0; i < keys.length; ++i) {
                    value = this.scalar.get(keys[i]);
                    ((SQLQuery)query).addScalar(keys[i].toString(), (Type)value);
                }
            }

            if (this.entity.size() > 0) {
                keys = this.entity.keySet().toArray();

                for(i = 0; i < keys.length; ++i) {
                    value = this.entity.get(keys[i]);
                    ((SQLQuery)query).addEntity(keys[i].toString(), (Class)value);
                }
            }
        }

        return (Query)query;
    }

    private String getArrayString(Object[] value) {
        if (value == null) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < value.length; ++i) {
                sb.append(value[i] + ",");
            }

            return sb.toString();
        }
    }

    public String getKey(Page page) {
        StringBuffer sb = new StringBuffer((this.mapping + ":" + this.hql + ":").toLowerCase());
        if (page != null) {
            sb.append(page.toString());
        }

        Object[] keys = this.param.keySet().toArray();
        Object value = null;

        for(int i = 0; i < keys.length; ++i) {
            value = this.param.get(keys[i]);
            if (value instanceof Collection) {
                sb.append(this.getArrayString(((Collection)value).toArray()));
            } else if (value instanceof Object[]) {
                sb.append(this.getArrayString((Object[])value));
            } else {
                sb.append(value.toString());
            }
        }

        return Md5Util.MD5Encode(sb.toString());
    }

    public Object uniqueResult() {
        try {
            this.createQuery();
            Object var3 = this.query.uniqueResult();
            return var3;
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            this.closeSession();
        }

        return null;
    }

    public Object uniqueResultCache() {
        return this.uniqueResultCache(ConfigurationManager.getCacheTime());
    }

    public Object uniqueResultCache(int time) {
        String key = this.getKey((Page)null);
        Object obj = MemCachedFactory.get(key);
        if (obj == null) {
            obj = this.uniqueResult();
            if (obj != null) {
                if (time < 0) {
                    MemCachedFactory.set(key, obj);
                } else {
                    MemCachedFactory.set(key, obj, new Date((long)time));
                }
            }
        }

        return obj;
    }

    public List list() {
        try {
            this.createQuery();
            List var3 = this.query.list();
            return var3;
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            this.closeSession();
        }

        return null;
    }

    public boolean isNoCache() {
        String key = this.getKey((Page)null);
        List result = (List)MemCachedFactory.get(key);
        return result == null;
    }

    public List listCache(int time) {
        String key = this.getKey((Page)null);
        List result = (List)MemCachedFactory.get(key);
        if (result == null) {
            result = this.list();
            if (result != null) {
                if (time < 0) {
                    MemCachedFactory.set(key, result);
                } else {
                    MemCachedFactory.set(key, result, new Date((long)time));
                }
            }
        }

        return result;
    }

    public List listCache() {
        return this.listCache(ConfigurationManager.getCacheTime());
    }

    public void listCacheUpdate(List result) {
        MemCachedFactory.set(this.getKey((Page)null), result);
    }

    public void listCacheRemove() {
        String key = this.getKey((Page)null);
        MemCachedFactory.delete(key);
    }

    public void listCacheRemove(Serializable id) {
        List result = this.listCache(-1);
        if (result != null) {
            for(int i = 0; i < result.size(); ++i) {
                if (result.get(i).toString().equals(id.toString())) {
                    result.remove(i);
                    break;
                }
            }

            this.listCacheUpdate(result);
        }
    }

    public void listCacheAdd(Object o) {
        List result = this.listCache(-1);
        if (result == null) {
            result = new ArrayList();
        }

        if (!((List)result).contains(o)) {
            ((List)result).add(o);
        }

        this.listCacheUpdate((List)result);
    }

    public Page page(Page page) {
        try {
            this.createQuery();
            int froms = this.hql.toLowerCase().indexOf("from");
            String hqlCount = "select count(" + page.getCountField() + ") " + this.hql.substring(froms);
            Query queryCount = this.createQuery(hqlCount);
            page.initPage(this.query, queryCount);
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            this.closeSession();
        }

        return page;
    }

    public Page pageCache(Page page, int time) {
        String key = this.getKey(page);
        Page cachepage = (Page)MemCachedFactory.get(key);
        if (cachepage == null) {
            cachepage = this.page(page);
            MemCachedFactory.set(key, cachepage, new Date((long)time));
        }

        return cachepage;
    }

    public Page pageCache(Page page) {
        return this.pageCache(page, ConfigurationManager.getCacheTime());
    }

    public List idToObject(Class clazz, List list) {
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
            Object var5 = this.getSession().get(clazz, id);
            return var5;
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            HibernateUtil.closeSession(this.mapping);
        }

        return null;
    }

    public <T> T getObject(Class<T> clazz, Serializable id) {
        if (id == null) {
            return null;
        } else {
            Object o = MemCachedFactory.get(getKey(this.mapping, clazz.getName(), id.toString()));
            if (o == null) {
                o = this.getDB(clazz, id);
                if (o != null) {
                    MemCachedFactory.set(getKey(this.mapping, clazz.getName(), id.toString()), o);
                }
            }

            return (T) o;
        }
    }

    public static String getKey(String mapping, String prefix, String key) {
        return Md5Util.MD5Encode((mapping + ":" + prefix + ":" + key).toLowerCase());
    }

    public String getObjecttKey(String mapping, Object o) {
        DBMapping dbMapping = HibernateUtil.getDBmapping(mapping);
        ClassMetadata cm = dbMapping.getSessionFactory().getClassMetadata(o.getClass());
        Object idKeyValue = cm.getIdentifier(o, EntityMode.POJO);
        if (idKeyValue == null) {
            idKeyValue = "";
        }

        return getKey(mapping, o.getClass().getName(), idKeyValue.toString());
    }

    public void set(Object o) {
        try {
            MemCachedFactory.set(this.getObjecttKey(this.mapping, o), o);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void remove(Object o) {
        try {
            MemCachedFactory.delete(this.getObjecttKey(this.mapping, o));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void remove(Class clazz, Object key) {
        try {
            if (key == null) {
                key = "";
            }

            String md5Key = getKey(this.mapping, clazz.getName(), key.toString());
            MemCachedFactory.delete(md5Key);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public void remove(Class clazz, List keys) {
        if (keys != null) {
            Iterator var4 = keys.iterator();

            while(var4.hasNext()) {
                Object key = var4.next();
                this.remove(clazz, key);
            }

        }
    }

    public Query getQuery() {
        return this.query;
    }
}
