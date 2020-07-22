package com.hnjz.base.memcached;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.hnjz.db.util.Md5Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：JingYj
 * @date ：2020/7/2
 * @version: V1.0.0
 */
@Component
public class MemCachedFactory {
    private static final Log log = LogFactory.getLog(MemCachedFactory.class);

    @Autowired
    public void setMemcachedConfiguration(MemcachedConfiguration memcachedConfiguration) {
        MemCachedFactory.memcachedConfiguration = memcachedConfiguration;
    }

    private static MemcachedConfiguration memcachedConfiguration;

    //    private static final Map<String, MemcachedConfiguration> configMap = new HashMap();
//    private static final Map<String, SockIOPool> poolMap = new HashMap();
    private static final Map<String, MemCachedClient> clientMap = new HashMap();
    public static byte[] lock = new byte[0];


    private MemCachedFactory() {
    }


    public static MemCachedClient buildClient(String config) {
        SockIOPool pool = null;
        MemCachedClient mcc = null;
        synchronized (lock) {
//            configMap.put(config, memcachedConfiguration);
//            pool = (SockIOPool) poolMap.get(config);
//            if (pool == null) {
//                pool = SockIOPool.getInstance(config);
//                pool.setServers(memcachedConfiguration.getServers());
//                pool.setWeights(memcachedConfiguration.getWeights());
//                pool.setInitConn(memcachedConfiguration.getInitConn());
//                pool.setMinConn(memcachedConfiguration.getMinConn());
//                pool.setMaxConn(memcachedConfiguration.getMaxConn());
//                pool.setMaxIdle(memcachedConfiguration.getMaxIdle());
//                pool.setMaintSleep(memcachedConfiguration.getMaintSleep());
//                pool.setNagle(memcachedConfiguration.isNagle());
//                pool.setSocketTO(memcachedConfiguration.getSocketTO());
//                pool.setSocketConnectTO(memcachedConfiguration.getSocketConnectTO());
//                pool.initialize();
//                poolMap.put(config, pool);
//            }
            //每一个config,就启动一个MemCachedClient
            mcc = (MemCachedClient) clientMap.get(config);
            if (mcc == null) {
                mcc = new MemCachedClient(config);
                mcc.setPrimitiveAsString(memcachedConfiguration.isPrimitiveAsString());
                clientMap.put(config, mcc);
            }
            return mcc;
        }
    }

    public static MemCachedClient buildClient() {
        return buildClient("/memcached.properties");
    }

    public static MemCachedClient currentClient(String key) {
        return key.toLowerCase().startsWith("dict_") ? buildClient("/memcached_dict.properties") : buildClient();
    }

    public static boolean set(String key, Object value) {
        return currentClient(key).set(Md5Util.MD5Encode(key), value);
    }

    public static boolean set(String key, Object value, Date date) {
        return currentClient(key).set(Md5Util.MD5Encode(key), value, date);
    }

    public static boolean set(String key, Object value, Integer size) {
        return currentClient(key).set(Md5Util.MD5Encode(key), value, size);
    }

    public static Object get(String key) {
        return currentClient(key).get(Md5Util.MD5Encode(key));
    }

    public static Object delete(String key) {
        return currentClient(key).delete(Md5Util.MD5Encode(key));
    }


}
