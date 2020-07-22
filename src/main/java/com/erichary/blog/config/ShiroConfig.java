package com.erichary.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置类
 *
 * @author erichary
 */
@Slf4j
@Configuration
public class ShiroConfig {


    /**
     * ShiroFilterFactoryBean 处理拦截资源文件
     * 注意：单独一个ShiroFilterFactoryBean配置会报错，初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明：
     * <p>
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * <p>
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * <p>
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //访问的是后端url地址为 /login的接口
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断，anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html#", "anon");
        filterChainDefinitionMap.put("/api/v1/**", "anon");
        // authc:所有url都必须认证通过才可以访问
        filterChainDefinitionMap.put("/fine", "user");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        log.info("Shiro拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }

    /**
     * 自定义Realm创建
     */
    @Bean
    public ShiroRealm shiroRealm(CredentialsMatcher credentialsMatcher) {
        ShiroRealm myShiroRealm = new ShiroRealm();
        //将自定义的令牌set到了Realm
        myShiroRealm.setCredentialsMatcher(credentialsMatcher);
        return myShiroRealm;
    }

    /**
     * 交由SecurityManager管理
     */
    @Bean
    @DependsOn
    public SecurityManager securityManager(CredentialsMatcher credentialsMatcher) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm(credentialsMatcher));
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 功能增强
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //加密方式
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //加密迭代次数
        hashedCredentialsMatcher.setHashIterations(1024);
        //true加密用的hex编码，false用的base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * shiro session的管理
     */
    public DefaultWebSessionManager sessionManager() {
        return new DefaultWebSessionManager();
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        log.info("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    /**
     * cookie管理对象
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        log.info("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * Shiro生命周期处理器
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
}