package com.springconfig.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wangmin on 2018/3/17.
 */
public class SsoFilter extends AccessControlFilter {

    private String ssoLoginUrl;

    public SsoFilter(String ssoLoginUrl) {
        this.ssoLoginUrl = ssoLoginUrl;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，重定向到单点登录系统
            HttpServletRequest req = (HttpServletRequest) request;
            //todo 希望在请求发现没有登录时，把原链接放进redis，该操作在上一次请求被shiro拦截的时候做，放入一个filter里
            String reqUrl = req.getScheme() + "://127.0.0.1:" + req.getServerPort() + req.getContextPath() + req.getRequestURI();
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendRedirect(ssoLoginUrl + "?redirectUrl="+reqUrl);
            return false;
        }
        return true;
    }
}
