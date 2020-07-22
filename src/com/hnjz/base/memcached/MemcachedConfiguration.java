package com.hnjz.base.memcached;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * @author ：JingYj
 * @date ：2020/7/2
 * @version: V1.0.0
 */
@Component
public class MemcachedConfiguration {
    private static final Log log = LogFactory.getLog(MemcachedConfiguration.class);

    private String[] servers = new String[]{"127.0.0.1:11211"};

    private Integer[] weights = new Integer[]{1};


    @Value("#{T(java.lang.Integer).parseInt('${memcached.initConn:20}')}")
    private Integer initConn;
    @Value("#{T(java.lang.Integer).parseInt('${memcached.minConn:20}')}")
    private Integer minConn;
    @Value("#{T(java.lang.Integer).parseInt('${memcached.maxConn:250}')}")
    private Integer maxConn;
    @Value("#{T(java.lang.Long).parseLong('${memcached.maxIdle:360000000L}')}")
    private long maxIdle;
    @Value("#{T(java.lang.Long).parseLong('${memcached.maintSleep:3000L}')}")
    private long maintSleep;
    @Value("#{T(java.lang.Boolean).parseBoolean('${memcached.nagle:false}')}")
    private boolean nagle;
    @Value("#{T(java.lang.Integer).parseInt('${memcached.socketTO:3000}')}")
    private Integer socketTO;
    @Value("#{T(java.lang.Integer).parseInt('${memcached.socketConnectTO:0}')}")
    private Integer socketConnectTO = 0;
    @Value("#{T(java.lang.Boolean).parseBoolean('${memcached.compressEnable:true}')}")
    private boolean compressEnable = true;
    @Value("#{T(java.lang.Long).parseLong('${memcached.compressThreshold:20480L}')}")
    private long compressThreshold;
    @Value("#{T(java.lang.Boolean).parseBoolean('${memcached.primitiveAsString:true}')}")
    private boolean primitiveAsString;

    /*Getter and Setter */
    @Value("${memcached.servers}")
    public void setServers(String servers) {
        this.servers = servers.split(",");
    }

    @Value("${memcached.weights}")
    public void setWeights(String weights) {
        String[] strs = weights.split(",");
        Integer[] num = new Integer[strs.length];
        for (int i = 0; i < num.length; ++i) {
            num[i] = Integer.parseInt(strs[i]);
        }
        this.weights = num;
    }


    public String[] getServers() {
        return servers;
    }

    public Integer[] getWeights() {
        return weights;
    }

    public Integer getInitConn() {
        return initConn;
    }

    public Integer getMinConn() {
        return minConn;
    }

    public Integer getMaxConn() {
        return maxConn;
    }

    public long getMaxIdle() {
        return maxIdle;
    }

    public long getMaintSleep() {
        return maintSleep;
    }

    public boolean isNagle() {
        return nagle;
    }

    public Integer getSocketTO() {
        return socketTO;
    }

    public Integer getSocketConnectTO() {
        return socketConnectTO;
    }

    public boolean isCompressEnable() {
        return compressEnable;
    }

    public long getCompressThreshold() {
        return compressThreshold;
    }

    public boolean isPrimitiveAsString() {
        return primitiveAsString;
    }
}
