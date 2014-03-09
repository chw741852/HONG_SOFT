package com.hong.core.security.support;

import com.hong.core.generic.service.IGenericService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-17
 * Time: 下午3:01
 * 核心地方，提供某个资源对应的权限定义，即getAttributes方法返回的结果。
 * 此类在初始化时，应取到所有资源及其对应角色的定义。
 */
public class InvocationSecurityMetadataSourceSupport implements FilterInvocationSecurityMetadataSource {
    private IGenericService genericService;

    public IGenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(IGenericService genericService) {
        this.genericService = genericService;
    }

    private PathMatcher pathMatcher = new AntPathMatcher();
//    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    public InvocationSecurityMetadataSourceSupport(IGenericService genericService) {
        this.genericService = genericService;
//        loadResourceDefine();
    }

//    private void loadResourceDefine() {
//        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
//        // web服务器启动时，提取系统所有权限
//        String sql = "select id from sys_authority where enabled=1";
//        List<String[]> authorities = genericService.executeSql(sql);
//
//        for (String[] authority:authorities) {
//            ConfigAttribute configAttribute = new SecurityConfig(authority[0]);
//
//            sql = "select b.url from sys_authority a, sys_resource b, sys_authority_resource c" +
//                    " where a.id=c.authority_id and b.id=c.resource_id and a.id='" + authority[0] + "'";
//            List<String[]> resources = genericService.executeSql(sql);
//
//            for(String[] resource:resources) {
//                String url = resource[0];
//                /*
//                 * 判断资源文件和权限的对应关系
//                 * 如果已存在相关的资源URL，则要通过改URL为key提取出权限集合，将权限增加到权限集合中。
//                 */
//                if (resourceMap.containsKey(url)) {
//                    Collection<ConfigAttribute> value = resourceMap.get(url);
//                    value.add(configAttribute);
//                    resourceMap.put(url, value);
//                } else {
//                    Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
//                    attributes.add(configAttribute);
//                    resourceMap.put(url, attributes);
//                    System.out.println("url:" + url + ",attr:" + attributes.toArray()[0].toString());
//                }
//            }
//        }
//    }

    // 根据URL找到相关配置
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // object 是一个被用户请求的URL
        FilterInvocation filterInvocation = (FilterInvocation) o;
        String url = filterInvocation.getRequestUrl();
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }
        Map<String, Collection<ConfigAttribute>> resourceMap = getResources(filterInvocation);
        if (resourceMap != null) {
            Iterator<String> iterator = resourceMap.keySet().iterator();
            while (iterator.hasNext()) {
                String resource = iterator.next();
                if (pathMatcher.match(url, resource)) {
                    return resourceMap.get(resource);
                }
            }
        }

        return null;
    }

    private Map<String, Collection<ConfigAttribute>> getResources(FilterInvocation filterInvocation) {
        return (Map<String, Collection<ConfigAttribute>>)
                filterInvocation.getHttpRequest().getSession().getServletContext().getAttribute("resourceMap");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
