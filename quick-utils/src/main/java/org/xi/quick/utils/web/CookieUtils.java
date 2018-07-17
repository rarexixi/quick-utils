package org.xi.quick.utils.web;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    /**
     * 设置cookie
     *
     * @param request
     * @param response
     * @param name     cookie名称
     * @param value    cookie值
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String name, String value) {

        setCookie(request, response, name, value, null, "/", null);
    }

    /**
     * 设置cookie
     *
     * @param request
     * @param response
     * @param name     cookie名称
     * @param value    cookie值
     * @param path     cookie路径
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String name, String value, String path) {

        setCookie(request, response, name, value, null, path, null);
    }

    /**
     * 设置cookie
     *
     * @param request
     * @param response
     * @param name     cookie名称
     * @param value    cookie值
     * @param domain   cookie域名
     * @param path     cookie路径
     * @param timeout  cookie过期时长
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String name, String value, String domain, String path, Integer timeout) {

        Cookie cookie = getCookie(request, name);
        if (cookie == null) {
            cookie = new Cookie(name, value);
        }

        if (timeout != null) cookie.setMaxAge(timeout);
        if (StringUtils.isNotBlank(path)) cookie.setPath(path);
        if (StringUtils.isNotBlank(domain)) cookie.setDomain(domain);

        response.addCookie(cookie);
    }

    /**
     * 获取cookie
     *
     * @param request
     * @param name    cookie名称
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) return cookie;
            }
        }
        return null;
    }

    /**
     * 获取cookie的值
     *
     * @param request
     * @param name    cookie名称
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {

        Cookie cookie = getCookie(request, name);
        return cookie == null ? null : cookie.getValue();
    }

    /**
     * 删除 cookie
     *
     * @param request
     * @param response
     * @param names    要删除的cookie名
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String... names) {

        for (String name : names) {
            Cookie cookie = getCookie(request, name);
            if (cookie == null) continue;
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 清除所有 cookie
     *
     * @param request
     * @param response
     */
    public static void clearCookies(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
