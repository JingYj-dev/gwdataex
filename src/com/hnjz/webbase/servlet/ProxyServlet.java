package com.hnjz.webbase.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class ProxyServlet extends HttpServlet {
    private String url;

    public ProxyServlet() {
    }

    private void process(HttpServletRequest req, HttpServletResponse resp, String[] target) throws MalformedURLException, IOException {
        HttpURLConnection huc = (HttpURLConnection)(new URL(this.url + target[0])).openConnection();
        huc.setDoOutput(true);
        huc.setRequestMethod("POST");
        huc.setUseCaches(false);
        huc.setInstanceFollowRedirects(true);
        huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        huc.connect();
        OutputStream targetOS = huc.getOutputStream();
        targetOS.write(target[1].getBytes());
        targetOS.flush();
        targetOS.close();
        resp.setContentType(huc.getContentType());
        resp.setHeader("Cache-Control", huc.getHeaderField("Cache-Control"));
        resp.setHeader("Pragma", huc.getHeaderField("Pragma"));
        resp.setHeader("Expires", huc.getHeaderField("Expires"));
        OutputStream os = resp.getOutputStream();
        InputStream targetIS = huc.getInputStream();

        int r;
        while((r = targetIS.read()) != -1) {
            os.write(r);
        }

        targetIS.close();
        os.flush();
        os.close();
        huc.disconnect();
    }

    private String[] parse(Map map) throws UnsupportedEncodingException {
        String[] arr = new String[]{"", ""};
        Iterator iter = map.entrySet().iterator();

        while(true) {
            while(iter.hasNext()) {
                Map.Entry me = (Map.Entry)iter.next();
                String[] varr = (String[])me.getValue();
                if ("servletName".equals(me.getKey())) {
                    arr[0] = varr[0];
                } else {
                    for(int i = 0; i < varr.length; ++i) {
                        arr[1] = arr[1] + "&" + me.getKey() + "=" + URLEncoder.encode(varr[i], "utf-8");
                    }
                }
            }

            arr[1] = arr[1].replaceAll("^&", "");
            return arr;
        }
    }

    public void init() throws ServletException {
        this.url = this.getInitParameter("url");
        if (!this.url.endsWith("/")) {
            this.url = this.url + "/";
        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] target = this.parse(req.getParameterMap());
        this.process(req, resp, target);
    }
}
