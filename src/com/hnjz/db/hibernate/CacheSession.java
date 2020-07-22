package com.hnjz.db.hibernate;

import com.hnjz.db.hibernate.Session;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:17
 */
public class CacheSession {
    public static final int SESSION_TYPE_FUNCTION = 0;
    public static final int SESSION_TYPE_TRANSACTION = 1;
    public static final int SESSION_TYPE_THREAD = 2;
    private int sessionType = 0;
    private Session session = null;

    public CacheSession(Session session, int sessionType) {
        this.session = session;
        this.sessionType = sessionType;
    }

    public void close() {
        if (this.session != null) {
            this.session.close();
            this.session = null;
        }

    }

    public Session getSession() {
        return this.session;
    }

    public int getSessionType() {
        return this.sessionType;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }
}
