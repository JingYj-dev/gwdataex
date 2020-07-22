package com.hnjz.webbase;

import com.hnjz.util.StringHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebAppInfo {
    private static com.hnjz.webbase.WebAppInfo inf = new com.hnjz.webbase.WebAppInfo();
    private String webappName;
    private String realPath;

    public WebAppInfo() {
    }

    public static com.hnjz.webbase.WebAppInfo getWebAppInfo() {
        return inf;
    }

    public String getWebappName() {
        return this.webappName;
    }

    public void setWebappName(String webappName) {
        this.webappName = webappName;
    }

    public String getRealPath() {
        return this.realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public List<String> getWebAppClassPathEntries() throws IOException {
        List<String> res = new ArrayList();
        if (StringHelper.isEmptyByTrim(this.realPath)) {
            return res;
        } else {
            String classesDir = this.realPath + "WEB-INF" + File.separator + "classes";
            if ((new File(classesDir)).exists()) {
                res.add(classesDir);
            }

            String libDir = this.realPath + "WEB-INF" + File.separator + "lib";
            File dir = new File(libDir);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".jar") || name.toLowerCase().endsWith(".zip");
                    }
                });
                File[] var9 = files;
                int var8 = files.length;

                for(int var7 = 0; var7 < var8; ++var7) {
                    File file = var9[var7];
                    res.add(file.getCanonicalPath());
                }

                return res;
            } else {
                return res;
            }
        }
    }
}
