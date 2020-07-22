package com.hnjz.webbase.webwork;


import com.hnjz.core.configuration.ConfigurationManager;
import com.opensymphony.util.FileManager;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WrappedConfigurationProvider extends XmlConfigurationProvider {
    private List<String> xworkResources = new ArrayList();
    private static Log log = LogFactory.getLog(WrappedConfigurationProvider.class);
    private static final String XWORK_FILE_HEADER = "<!DOCTYPE xwork PUBLIC \"-//OpenSymphony Group//XWork 1.0//EN\"    \"http://www.opensymphony.com/xwork/xwork-1.0.dtd\"><xwork>";

    public WrappedConfigurationProvider() {
    }

    public WrappedConfigurationProvider(String filename) {
        super(filename);
    }

    private List<String> getClassName(String packageName) throws Exception {
        List<String> classNames = new ArrayList();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String resourceName = packageName.replaceAll("\\.", "/");
        URL url = loader.getResource(resourceName);
        File urlFile = new File(url.toURI());
        File[] files = urlFile.listFiles();
        File[] var11 = files;
        int var10 = files.length;

        for(int var9 = 0; var9 < var10; ++var9) {
            File f = var11[var9];
            this.getClassName(packageName, f, classNames);
        }

        return classNames;
    }

    private void getClassName(String packageName, File packageFile, List<String> list) throws Exception {
        if (packageFile.isFile()) {
            String name = packageFile.getName();
            if (name.startsWith("xwork-") && name.toLowerCase().endsWith("xml")) {
                this.xworkResources.add(packageName.replaceAll("\\.", "\\/") + "/" + name);
            }
        } else if (packageFile.isDirectory()) {
            File[] files = packageFile.listFiles();
            if (files != null) {
                String tmPackageName = packageName + "." + packageFile.getName();
                File[] var9 = files;
                int var8 = files.length;

                for(int var7 = 0; var7 < var8; ++var7) {
                    File f = var9[var7];
                    this.getClassName(tmPackageName, f, list);
                }
            }
        }

    }

    protected InputStream getInputStream(String fileName) {
        if ("_cssapp.xml".equals(fileName)) {
            String encoding = "UTF-8";
            String packageName = ConfigurationManager.getConfigurationManager().getSysConfigure("app.scan.pacakge", "com.hnjz");
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            try {
                this.getClassName(packageName);
                StringBuffer xwork = new StringBuffer("<!DOCTYPE xwork PUBLIC \"-//OpenSymphony Group//XWork 1.0//EN\"    \"http://www.opensymphony.com/xwork/xwork-1.0.dtd\"><xwork>");
                Iterator it = this.xworkResources.iterator();

                while(it.hasNext()) {
                    String xworkFile = (String)it.next();
                    if (xworkFile != null && !xworkFile.trim().equals("")) {
                        xwork.append("<include file=\"").append(xworkFile.trim()).append("\" />");
                        log.info("load xwork file: <include file=\"" + xworkFile.trim() + "\"/>");
                    }
                }

                xwork.append("</xwork>");
                os.write(xwork.toString().getBytes(encoding));
                ByteArrayInputStream var9 = new ByteArrayInputStream(os.toByteArray());
                return var9;
            } catch (Exception var16) {
                throw new RuntimeException("autoload Action config error!", var16);
            } finally {
                try {
                    os.close();
                    os = null;
                } catch (Exception var15) {
                    ;
                }

            }
        } else {
            return FileManager.loadFile(fileName, this.getClass());
        }
    }

    public void destroy() {
        super.destroy();
    }
}
