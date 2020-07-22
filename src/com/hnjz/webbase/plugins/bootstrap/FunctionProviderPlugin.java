package com.hnjz.webbase.plugins.bootstrap;

import com.hnjz.common.plugins.IDisposable;
import com.hnjz.common.plugins.Initializable;
import com.hnjz.common.plugins.impl.AbstractPlugin;
import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.util.StringHelper;
import com.hnjz.webbase.FunctionManager;
import com.hnjz.webbase.webwork.WrappedConfigurationProvider;
import com.opensymphony.xwork.config.ConfigurationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionProviderPlugin extends AbstractPlugin implements IDisposable, Initializable {
    private static final Log log = LogFactory.getLog(com.hnjz.webbase.plugins.bootstrap.FunctionProviderPlugin.class);
    private static final String clazzProvider = ConfigurationManager.getConfigurationManager().getSysConfigure("app.xwork.provider", (String)null);

    public FunctionProviderPlugin() {
    }

    public void initialize() {
        ConfigurationProvider myProvider = null;
        if (StringHelper.isEmptyByTrim(clazzProvider)) {
            myProvider = new WrappedConfigurationProvider();
        } else {
            try {
                myProvider = (ConfigurationProvider)Class.forName(clazzProvider).newInstance();
            } catch (Throwable var3) {
                throw new RuntimeException(var3);
            }
        }

        com.opensymphony.xwork.config.ConfigurationManager.clearConfigurationProviders();
        com.opensymphony.xwork.config.ConfigurationManager.addConfigurationProvider((ConfigurationProvider)myProvider);
        FunctionManager.getFunctionManager();
        log.debug("FunctionManager initialized!");
    }

    public void dispose() {
        FunctionManager.getFunctionManager().cleanUp();
        log.debug("FunctionManager disposed!");
    }
}
