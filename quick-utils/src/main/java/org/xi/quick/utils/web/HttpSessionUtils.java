package org.xi.quick.utils.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class HttpSessionUtils {

    /**
     * 设置session
     *
     * @param request
     * @param name    session名称
     * @param value   session值
     */
    public static void setSession(HttpServletRequest request, String name, String value) {

        HttpSession session = request.getSession();
        session.setAttribute(name, value);
    }

    /**
     * 获取session
     *
     * @param request
     * @param name    session名称
     * @return
     */
    public static Object getSession(HttpServletRequest request, String name) {

        HttpSession session = request.getSession();
        return session.getAttribute(name);
    }

    /**
     * 删除 session
     *
     * @param request
     * @param names   要删除的session名
     */
    public static void removeSession(HttpServletRequest request, String... names) {

        HttpSession session = request.getSession();
        for (String name : names) {
            session.removeAttribute(name);
        }
    }

    /**
     * 清除所有 session
     *
     * @param request
     */
    public static void clearSessions(HttpServletRequest request) {

        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            session.removeAttribute(attributeNames.nextElement());
        }
    }
}
