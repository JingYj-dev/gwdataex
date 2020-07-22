package com.hnjz.common.plugins.config;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:56
 */
public class PluginConfigException extends RuntimeException{
    public PluginConfigException() {
    }

    public PluginConfigException(String message) {
        super(message);
    }

    public PluginConfigException(Throwable cause) {
        super(cause);
    }

    public PluginConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
