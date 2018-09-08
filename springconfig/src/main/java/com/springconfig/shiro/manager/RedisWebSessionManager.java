package com.springconfig.shiro.manager;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;

/**
 * Created by wangmin on 2018/3/26.
 */
public class RedisWebSessionManager extends DefaultWebSessionManager {

    @Override
    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);
        ServletRequest request = WebUtils.getRequest(context);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
    }
}
