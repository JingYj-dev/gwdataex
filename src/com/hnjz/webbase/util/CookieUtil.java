package com.hnjz.webbase.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public CookieUtil() {
    }

    public static void SetCookies(String CookieName, String CookieValues, int CookieDay, HttpServletResponse response) {
        try {
            Cookie cookie = new Cookie(CookieName, CookieValues);
            cookie.setMaxAge(CookieDay);
            response.addCookie(cookie);
        } catch (Exception var5) {
            ;
        }

    }

    public static String GetCookies(String CookieName, HttpServletRequest request) {
        String cookie = "";

        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for(int i = 0; i < cookies.length; ++i) {
                    if (cookies[i].getName().equals(CookieName)) {
                        cookie = cookies[i].getValue();
                    }
                }
            }
        } catch (Exception var5) {
            ;
        }

        return cookie;
    }
}
