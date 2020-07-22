package com.hnjz.common.plugins;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 17:45
 */
public class PluginException extends RuntimeException{
    public PluginException() {
    }

    public PluginException(String message) {
        super(message);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
