package com.hnjz.webbase.util;

import com.hnjz.util.StringHelper;
import com.hnjz.webbase.WebAppInfo;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassPathResourceUtil {
    public ClassPathResourceUtil() {
    }

    public static List<String> getClassPath() throws Exception {
        String cp = System.getProperty("java.class.path");
        if (StringHelper.isEmpty(cp)) {
            return null;
        } else {
            String sepChar = File.pathSeparator;
            String[] entries = cp.split(sepChar);
            return Arrays.asList(entries);
        }
    }

    public static List<String> getClassPathResource(String regex) throws Exception {
        List<String> cps = WebAppInfo.getWebAppInfo().getWebAppClassPathEntries();
        if (cps == null || cps.isEmpty()) {
            cps = getClassPath();
        }

        if (cps != null && !cps.isEmpty()) {
            List<String> res = new ArrayList();
            Iterator var4 = cps.iterator();

            while(var4.hasNext()) {
                String file = (String)var4.next();
                File fl = new File(file);
                if (fl.isDirectory()) {
                    transpass(fl, regex, res, fl.getAbsolutePath());
                } else if (fl.isFile()) {
                    transpassJar(fl, regex, res);
                }
            }

            return res;
        } else {
            return null;
        }
    }

    private static void transpass(File dir, String regex, List<String> res, String prefix) {
        File[] files = dir.listFiles();
        File[] var8 = files;
        int var7 = files.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            File ff = var8[var6];
            if (!ff.getName().equals(".") && !ff.getName().equals("..")) {
                if (ff.isFile()) {
                    String fn = ff.getName();
                    if (StringHelper.isEmptyByTrim(regex) || fn.matches(regex)) {
                        String entryName = ff.getAbsolutePath().substring(prefix.length() + 1);
                        entryName = entryName.replaceAll("\\\\", "/");
                        res.add(entryName);
                    }
                } else if (ff.isDirectory()) {
                    transpass(ff, regex, res, prefix);
                }
            }
        }

    }

    private static void transpassJar(File jarFile, String regex, List<String> res) throws IOException {
        URL url = new URL("jar:" + jarFile.toURI() + "!/");
        JarURLConnection con = (JarURLConnection)url.openConnection();
        JarFile file = con.getJarFile();
        Enumeration<JarEntry> enu = file.entries();
        String entryName = "";

        while(enu.hasMoreElements()) {
            JarEntry element = (JarEntry)enu.nextElement();
            if (!element.isDirectory()) {
                entryName = element.getName();
                entryName = entryName.replaceAll("\\\\", "/");
                int idx = entryName.lastIndexOf(47);
                String fileName = entryName;
                if (idx != -1) {
                    fileName = entryName.substring(idx + 1);
                }

                if (fileName.matches(regex)) {
                    res.add(entryName);
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
        List<String> cps = getClassPathResource(".*xml");
        Iterator var3 = cps.iterator();

        while(var3.hasNext()) {
            String cp = (String)var3.next();
            System.out.println(cp);
        }

    }
}
