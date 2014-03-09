package com.hong.core.listener;

import com.hong.core.security.support.ISecurityManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Collection;
import java.util.Map;

/**
 * Created by hong on 14-3-8 下午12:52.
 * web服务启动时初始化载入所有已分配权限的资源
 */
public class ServletContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ISecurityManager securityManager = getSecurityManager(servletContext);
        Map<String, Collection<ConfigAttribute>> resourceMap = securityManager.loadResource();
        servletContext.setAttribute("resourceMap", resourceMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().removeAttribute("resourceMap");
    }

    private ISecurityManager getSecurityManager(ServletContext servletContext) {
        return (ISecurityManager) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean("userDetailsService");
    }
}
