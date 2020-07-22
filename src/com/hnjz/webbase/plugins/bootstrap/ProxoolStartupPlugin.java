package com.hnjz.webbase.plugins.bootstrap;

import com.hnjz.common.plugins.IDisposable;
import com.hnjz.common.plugins.Initializable;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.webbase.listener.PluginBusListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import java.io.InputStreamReader;
import java.util.Map;

public class ProxoolStartupPlugin extends AbstractConfigurablePlugin implements IDisposable, Initializable {
    private static final Log log = LogFactory.getLog(com.hnjz.webbase.plugins.bootstrap.ProxoolStartupPlugin.class);
    private boolean proxoolStart = false;
    private String proxoolFile = null;

    public ProxoolStartupPlugin() {
    }

    public void initialize() {
        if (this.proxoolStart) {
            if (!this.proxoolFile.startsWith("/")) {
                this.proxoolFile = "/" + this.proxoolFile;
            }

            try {
                JAXPConfigurator.configure(new InputStreamReader(PluginBusListener.class.getResourceAsStream(this.proxoolFile)), false);
            } catch (ProxoolException var2) {
                log.error("proxool start failed!", var2);
            }

            log.info("proxool initialized ... ");
        } else {
            log.info("proxool doesn't configured startup when bootstrap...");
        }

    }

    public void dispose() {
        if (this.proxoolStart) {
            ProxoolFacade.shutdown(0);
            log.info("proxool stopping ... ");
        }

    }

    protected void doConfig(Map<String, String> configMap) {
        String needStartup = (String)configMap.get("app.proxool.active");
        if ("true".equals(needStartup)) {
            this.proxoolStart = true;
        }

        if (this.proxoolStart) {
            this.proxoolFile = (String)configMap.get("app.proxool.file");
            if (this.proxoolFile == null || this.proxoolFile.trim().equals("")) {
                this.proxoolFile = "proxool.xml";
            }
        }

    }
}
