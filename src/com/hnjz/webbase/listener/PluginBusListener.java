package com.hnjz.webbase.listener;

import com.hnjz.common.PluginBus;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.webbase.WebAppInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class PluginBusListener implements ServletContextListener {
    private static final Log log = LogFactory.getLog(PluginBusListener.class);

    public PluginBusListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            WebAppInfo wai = WebAppInfo.getWebAppInfo();
            wai.setRealPath(sce.getServletContext().getRealPath("/"));
            wai.setWebappName(sce.getServletContext().getServletContextName());
            PluginBus.init();
        } catch (Exception var3) {
            if (ConfigurationManager.isDebug()) {
                throw new RuntimeException("加载插件工厂异常，应用初始化失败", var3);
            }

            throw new RuntimeException(var3.getMessage());
        }

        log.info(" PluginFactory initialized ...");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        PluginBus.dispose();
        log.info(" PluginFactory destroyed ...");
    }
}
