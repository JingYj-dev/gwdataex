package com.hnjz.db.hibernate;

import com.hnjz.db.hibernate.CacheSession;
import com.hnjz.db.hibernate.Session;
import com.hnjz.db.hibernate.ThreadLocalMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:19
 */
public class DBMapping {
    private static Log log = LogFactory.getLog(com.hnjz.db.hibernate.DBMapping.class);
    private Configuration configuration = null;
    private String mapping = null;
    private SessionFactory sessionFactory = null;

    public DBMapping() {
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public synchronized void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        this.sessionFactory = this.configuration.buildSessionFactory();
    }

    public Session currentSession(int sessionType) {
        CacheSession cs = (com.hnjz.db.hibernate.CacheSession) ThreadLocalMap.get(this.mapping);
        if (cs == null) {
            log.debug("new session(" + this.mapping + "):" + Thread.currentThread().getId());
            Session s = new Session(this.sessionFactory.openStatelessSession());
            cs = new com.hnjz.db.hibernate.CacheSession(s, sessionType);
            ThreadLocalMap.put(this.mapping, cs);
        } else {
            if (sessionType > cs.getSessionType()) {
                cs.setSessionType(sessionType);
            }

            log.debug("same session(" + this.mapping + "):" + Thread.currentThread().getId());
        }

        return cs.getSession();
    }

    private com.hnjz.db.hibernate.CacheSession getCS() {
        return (com.hnjz.db.hibernate.CacheSession)ThreadLocalMap.get(this.mapping);
    }

    public void closeThreadSession() {
        com.hnjz.db.hibernate.CacheSession cs = this.getCS();
        if (cs != null) {
            this.close(cs);
        }

    }

    public void closeTransactionSession() {
        com.hnjz.db.hibernate.CacheSession cs = this.getCS();
        if (cs != null && cs.getSessionType() < 2) {
            this.close(cs);
        }

    }

    public void closeSession() {
        com.hnjz.db.hibernate.CacheSession cs = this.getCS();
        if (cs != null && cs.getSessionType() < 1) {
            this.close(cs);
        }

    }

    private void close(CacheSession cs) {
        log.debug("closs session(" + this.mapping + "):" + Thread.currentThread().getId() + "\t" + cs.hashCode());

        try {
            cs.close();
            cs = null;
        } finally {
            ThreadLocalMap.remove(this.mapping);
        }

    }

    public String getMapping() {
        return this.mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }
}
