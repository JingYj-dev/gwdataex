package com.hnjz.webbase.webwork;

import com.hnjz.core.configuration.ConfigurationManager;
import com.hnjz.webbase.util.ClassPathResourceUtil;
import com.opensymphony.util.FileManager;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * 实现动态加载xwork配置文件
 */
public class WrappedConfigurationProviderEx extends XmlConfigurationProvider {
    private static Log log = LogFactory.getLog(com.hnjz.webbase.webwork.WrappedConfigurationProviderEx.class);
    private static final String XWORK_FILE_HEADER = "<!DOCTYPE xwork PUBLIC \"-//OpenSymphony Group//XWork 1.0//EN\"    \"http://www.opensymphony.com/xwork/xwork-1.0.dtd\"><xwork>";

    public WrappedConfigurationProviderEx() {
    }

    public WrappedConfigurationProviderEx(String filename) {
        super(filename);
    }

    protected InputStream getInputStream(String fileName) {
        if ("_xwork-app.xml".equals(fileName)) {
            String encoding = "UTF-8";
            String xworkRegex = ConfigurationManager.getConfigurationManager().getSysConfigure("app.xwork.patten", "^xwork\\-.+\\.xml$");
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            ByteArrayInputStream var10;
            try {
                List<String> xworkResources = ClassPathResourceUtil.getClassPathResource(xworkRegex);
                if (xworkResources.contains("xwork-default.xml")) {
                    xworkResources.remove("xwork-default.xml");
                }

                StringBuffer xwork = new StringBuffer("<!DOCTYPE xwork PUBLIC \"-//OpenSymphony Group//XWork 1.0//EN\"    \"http://www.opensymphony.com/xwork/xwork-1.0.dtd\"><xwork>");
                Iterator it = xworkResources.iterator();

                while(it.hasNext()) {
                    String xworkFile = (String)it.next();
                    if (xworkFile != null && !xworkFile.trim().equals("")) {
                        xwork.append("<include file=\"").append(xworkFile.trim()).append("\" />");
                        log.info("load xwork file: <include file=\"" + xworkFile.trim() + "\"/>");
                    }
                }

                xwork.append("</xwork>");
                os.write(xwork.toString().getBytes(encoding));
                var10 = new ByteArrayInputStream(os.toByteArray());
            } catch (Exception var17) {
                throw new RuntimeException("autoload Action config error!", var17);
            } finally {
                try {
                    os.close();
                    os = null;
                } catch (Exception var16) {
                    ;
                }

            }

            return var10;
        } else {
            return FileManager.loadFile(fileName, this.getClass());
        }
    }

    public void destroy() {
        super.destroy();
    }
}
