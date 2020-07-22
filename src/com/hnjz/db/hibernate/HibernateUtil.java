package com.hnjz.db.hibernate;

import com.hnjz.db.hibernate.DBMapping;
import com.hnjz.db.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 16:20
 */
public class HibernateUtil {
    private static final Map<String, com.hnjz.db.hibernate.DBMapping> mapDBMapping = new HashMap();

    public HibernateUtil() {
    }

    public static synchronized com.hnjz.db.hibernate.DBMapping getDBmapping(String mapping) {
        com.hnjz.db.hibernate.DBMapping dbMapping = (com.hnjz.db.hibernate.DBMapping)mapDBMapping.get(mapping);
        if (dbMapping == null) {
            Configuration configuration = (new Configuration()).configure(mapping);
            if (configuration != null) {
                dbMapping = new com.hnjz.db.hibernate.DBMapping();
                dbMapping.setMapping(mapping);
                dbMapping.setConfiguration(configuration);
                mapDBMapping.put(mapping, dbMapping);
            }
        }

        return dbMapping;
    }

    public static Session currentSession(String mapping, int sessionType) {
        try {
            DBMapping dbMapping = getDBmapping(mapping);
            return dbMapping != null ? dbMapping.currentSession(sessionType) : null;
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ExceptionInInitializerError(var3);
        }
    }

    public static Session currentSession() {
        return getFunctionSession();
    }

    public static Session getFunctionSession() {
        return currentSession("/hibernate.cfg.xml", 0);
    }

    public static Session getTransactionSession() {
        return currentSession("/hibernate.cfg.xml", 1);
    }

    public static Session getThreadSession() {
        return currentSession("/hibernate.cfg.xml", 2);
    }

    public static Session getFunctionSession(String mapping) {
        return currentSession(mapping, 0);
    }

    public static Session getTransactionSession(String mapping) {
        return currentSession(mapping, 1);
    }

    public static Session getThreadSession(String mapping) {
        return currentSession(mapping, 2);
    }

    public static void closeSession(String mapping) {
        com.hnjz.db.hibernate.DBMapping dbMapping = (com.hnjz.db.hibernate.DBMapping)mapDBMapping.get(mapping);
        if (dbMapping != null) {
            dbMapping.closeSession();
        }

    }

    public static void closeTransactionSession(String mapping) {
        com.hnjz.db.hibernate.DBMapping dbMapping = (com.hnjz.db.hibernate.DBMapping)mapDBMapping.get(mapping);
        if (dbMapping != null) {
            dbMapping.closeTransactionSession();
        }

    }

    public static void closeThreadSession(String mapping) {
        com.hnjz.db.hibernate.DBMapping dbMapping = (DBMapping)mapDBMapping.get(mapping);
        if (dbMapping != null) {
            dbMapping.closeThreadSession();
        }

    }

    public static void closeSession() {
        closeSession("/hibernate.cfg.xml");
    }

    public static void closeTransactionSession() {
        closeTransactionSession("/hibernate.cfg.xml");
    }

    public static void closeThreadSession() {
        closeThreadSession("/hibernate.cfg.xml");
    }
}
