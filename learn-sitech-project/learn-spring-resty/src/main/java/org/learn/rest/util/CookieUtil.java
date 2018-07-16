package org.learn.rest.util;

import javax.servlet.http.Cookie;

import cn.hutool.core.util.StrUtil;

/**
 * 操作cookie的工具类
 */
public class CookieUtil {

    private CookieUtil() {}

    public static String getCookieToken(Cookie[] cookies, String key) {
        String value = null;
        int length = cookies.length;
        if (cookies != null && length > 0) {

            for (int i = 0; i < length; i++) {
                if (cookies[i].getName().equals(key)) {
                    value = cookies[i].getValue();
                    break;
                }
            }
        }
        return  StrUtil.isBlank(value) ? "" : value;
    }

    public static Cookie setCookie(String name, String value, int seconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(seconds);
        return cookie;
    }

    public static Cookie setCookie(String name, String value,String path, int seconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(seconds);
        cookie.setPath(path);
        return cookie;
    }

    public static Cookie setCookie(String name, String value,String path,String domain, int seconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(seconds);
        cookie.setPath("/");
        cookie.setDomain(domain);
        return cookie;
    }

    /**
     * 删除指定的cookie
     */
    public static Cookie deleteCookie(String cookieName,String path) {
        Cookie cookie = new Cookie(cookieName, "");

        //设置maxAge为0 表示浏览器关闭删除cookie
        cookie.setMaxAge(0);

        if(StrUtil.isNotBlank(path)){
            cookie.setPath(path);
        }

        return cookie;
    }

}
