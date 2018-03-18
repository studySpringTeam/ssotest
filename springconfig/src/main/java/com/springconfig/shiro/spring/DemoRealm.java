package com.springconfig.shiro.spring;

import com.springconfig.shiro.exception.PasswordEmptyException;
import com.springconfig.shiro.exception.UsernameEmptyException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by wangmin on 2018/3/17.
 */
public class DemoRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("admin");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        if (username == null || "".equals(username)) {
            throw new UsernameEmptyException(
                    "用户名为空");
        }
        if (token.getPassword() == null || "".equals(token.getPassword())){
            throw new PasswordEmptyException("密码为空");
        }
        if((!"system".equals(username))){
            throw new IncorrectCredentialsException("用户名或者密码错误");
        }
        //假定用户名和密码就是system和123456
        String password = new Md5Hash("123456", username, 2).toString();
        return new SimpleAuthenticationInfo(new ShiroUser(username,"系统"), password, getName());
    }

    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        protected String loginName;
        protected String name;

        public ShiroUser(String loginName, String name) {
            this.loginName = loginName;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getLoggerinName() {
            return loginName;
        }

        /**
         * 作为默认的<shiro:principal/>输出.
         */
        @Override
        public String toString() {
            return loginName;
        }

        /**
         * 重载hashCode,只计算loginName;
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(loginName);
        }

        /**
         * 重载equals,只计算loginName;
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ShiroUser other = (ShiroUser) obj;
            if (loginName == null) {
                if (other.loginName != null) {
                    return false;
                }
            } else if (!loginName.equals(other.loginName)) {
                return false;
            }
            return true;
        }
    }
}
