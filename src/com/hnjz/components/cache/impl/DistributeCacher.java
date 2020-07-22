package com.hnjz.components.cache.impl;

import com.hnjz.common.plugins.config.PluginConfigException;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.components.cache.ICacher;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.hnjz.util.PropertiesHelper;
import com.hnjz.util.numeric.Arithmetic;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:27
 */
public class DistributeCacher extends AbstractConfigurablePlugin implements ICacher {
    private String confPropFile = "/memcached.properties";
    private MemCachedClient mcc = new MemCachedClient();

    public DistributeCacher() {
    }

    public Object getObject(Object key) {
        return key == null ? null : this.mcc.get(key.toString());
    }

    public void setObject(String key, Object value) {
        if (key != null) {
            this.mcc.set(key, value);
        }
    }

    public void setObject(String key, Object value, long time) {
        if (key != null) {
            this.mcc.set(key, value, new Date(time));
        }
    }

    public void remove(Object key) {
        if (key != null) {
            this.mcc.delete(key.toString());
        }
    }

    public void removeAll() {
        this.mcc.flushAll();
    }

    public Map<String, Object> getObjects(String... keys) {
        return this.mcc.getMulti(keys);
    }
/*
    public void doConfig(Map<String, String> conf) {
        if (conf.get("conf") != null) {
            this.confPropFile = (String)conf.get("conf");
        }

        if (this.confPropFile != null && !this.confPropFile.trim().equals("")) {
            if (com.hnjz.components.cache.impl.DistributeCacher.class.getResourceAsStream(this.confPropFile) == null) {
                throw new PluginConfigException("缓存配置文件[" + this.confPropFile + "]不存在!");
            } else {
                DistributeCacher.MemcachedConfiguration mc = new DistributeCacher.MemcachedConfiguration(this.confPropFile);
                SockIOPool pool = SockIOPool.getInstance();
                pool.setServers(mc.getServers());
                pool.setWeights(mc.getWeights());
                pool.setInitConn(mc.getInitConn());
                pool.setMinConn(mc.getMinConn());
                pool.setMaxConn(mc.getMaxConn());
                pool.setMaxIdle(mc.getMaxIdle());
                pool.setMaintSleep(mc.getMaintSleep());
                pool.setNagle(mc.getNagle());
                pool.setSocketTO(mc.getSocketTO());
                pool.setSocketConnectTO(mc.getSocketConnectTO());
                pool.initialize();
                this.mcc.setPrimitiveAsString(mc.getPrimitiveAsString());
            }
        } else {
            throw new PluginConfigException("缓存配置错误:conf设置不正确!");
        }
    }
*/

    public boolean addObject(String key, Object value, long time) {
        return this.mcc.add(key, value, new Date(time));
    }

/*
    public static class MemcachedConfiguration {
        private String[] servers = null;
        private Integer[] weights = null;
        private Integer initConn = null;
        private Integer minConn = null;
        private Integer maxConn = null;
        private long maxIdle;
        private long maintSleep;
        private boolean nagle = false;
        private Integer socketTO = null;
        private Integer socketConnectTO = null;
        private boolean compressEnable = true;
        private long compressThreshold;
        private boolean primitiveAsString = true;

        protected MemcachedConfiguration(String propertiesFile) {
            InputStream fin = this.getClass().getResourceAsStream(propertiesFile);
            Properties dbProps = new Properties();

            try {
                dbProps.load(fin);
                this.servers = PropertiesHelper.getString("memcached.servers", dbProps, "localhost:11211").split(",");
                this.weights = toIntArray(PropertiesHelper.getString("memcached.port", dbProps, "1").split(","));
                this.initConn = PropertiesHelper.getInt("memcached.initConn", dbProps, 20);
                this.minConn = PropertiesHelper.getInt("memcached.minConn", dbProps, 20);
                this.maxConn = PropertiesHelper.getInt("memcached.maxConn", dbProps, 250);
                this.maxIdle = (long) Arithmetic.evaluate(PropertiesHelper.getString("memcached.maxIdle", dbProps, "1000 * 60 * 60 * 6"));
                this.maintSleep = (long)Arithmetic.evaluate(PropertiesHelper.getString("memcached.maintSleep", dbProps, "3000"));
                this.nagle = PropertiesHelper.getBoolean("memcached.nagle", dbProps, false);
                this.socketTO = Arithmetic.evaluate(PropertiesHelper.getString("memcached.socketTO", dbProps, "3000"));
                this.socketConnectTO = Arithmetic.evaluate(PropertiesHelper.getString("memcached.socketConnectTO", dbProps, "0"));
                this.compressEnable = PropertiesHelper.getBoolean("memcached.compressEnable", dbProps, true);
                this.compressThreshold = (long)Arithmetic.evaluate(PropertiesHelper.getString("memcached.compressThreshold", dbProps, "20 * 1024"));
                this.primitiveAsString = PropertiesHelper.getBoolean("memcached.primitiveAsString", dbProps, true);
                fin.close();
            } catch (Exception var5) {
            }

        }

        public static Integer[] toIntArray(String[] str) {
            Integer[] num = new Integer[str.length];

            for(int i = 0; i < num.length; ++i) {
                num[i] = Integer.parseInt(str[i]);
            }

            return num;
        }

        public String[] getServers() {
            return this.servers;
        }

        public Integer[] getWeights() {
            return this.weights;
        }

        public Integer getInitConn() {
            return this.initConn;
        }

        public Integer getMinConn() {
            return this.minConn;
        }

        public Integer getMaxConn() {
            return this.maxConn;
        }

        public boolean getNagle() {
            return this.nagle;
        }

        public Integer getSocketTO() {
            return this.socketTO;
        }

        public Integer getSocketConnectTO() {
            return this.socketConnectTO;
        }

        public boolean getCompressEnable() {
            return this.compressEnable;
        }

        public boolean getPrimitiveAsString() {
            return this.primitiveAsString;
        }

        public long getMaxIdle() {
            return this.maxIdle;
        }

        public long getMaintSleep() {
            return this.maintSleep;
        }

        public long getCompressThreshold() {
            return this.compressThreshold;
        }
    }
*/


    @Override
    protected void doConfig(Map<String, String> var1) {

    }
}
