package com.springconfig.config;

import com.springconfig.shiro.filter.SsoFilter;
import com.springconfig.shiro.spring.DemoRealm;
import com.springconfig.shiro.filter.MyAuthorizationFilter;
import com.springconfig.shiro.filter.MyFormAuthenticationFilter;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wangmin on 2018/3/17.
 */
@Configuration
public class ShiroConfig {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Value("${shiro.configLocation}")
    private String ehcacheXmlPath;

    @Value("${sso.otherSystem}")
    private boolean ssoOtherSystem;

    @Value("${sso.loginUrl}")
    private String ssoLoginUrl;

    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile(ehcacheXmlPath);
        return em;
    }

    /**
     * 注册DelegatingFilterProxy（Shiro）
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        // 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public DemoRealm demoRealm() {
        DemoRealm demoRealm = new DemoRealm();
        return demoRealm;
    }

    /**
     * 加载shiroFilter权限控制规则
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
//        filters.put("anyRoles", myAuthorizationFilter());
        filters.put("authc", myFormAuthenticationFilter());
        if(ssoOtherSystem) {
            //自定义filter直接采用在这个bean方法里new出来，该map的key可以自定义，然后对下面的filterChain相关的链接做过滤
            filters.put("ssoFilter", new SsoFilter(ssoLoginUrl));
        }
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/test/aaa", "anon");
        if(ssoOtherSystem) {
            filterChainDefinitionMap.put("/login", "authc, ssoFilter");
            filterChainDefinitionMap.put("/**", "user, ssoFilter");
        } else {
            filterChainDefinitionMap.put("/login", "authc");
            filterChainDefinitionMap.put("/**", "user");
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

//    @Bean
//    public MyAuthorizationFilter myAuthorizationFilter() {
//        return new MyAuthorizationFilter();
//    }

    @Bean
    public MyFormAuthenticationFilter myFormAuthenticationFilter() {
        MyFormAuthenticationFilter myFormAuthenticationFilter = new MyFormAuthenticationFilter();
        myFormAuthenticationFilter.setSsoOtherSystem(ssoOtherSystem);
        return myFormAuthenticationFilter;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(DemoRealm demoRealm) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(demoRealm);
        // <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 -->
        dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DemoRealm demoRealm) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager(demoRealm));
        return new AuthorizationAttributeSourceAdvisor();
    }
}
