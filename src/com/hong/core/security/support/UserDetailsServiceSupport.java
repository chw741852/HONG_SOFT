package com.hong.core.security.support;

import com.hong.core.generic.service.IGenericService;
import com.hong.core.security.domain.User;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-17
 * Time: 下午3:53
 * 为Spring Security提供一个经过用户认证后的UserDetails。
 */
public class UserDetailsServiceSupport implements UserDetailsService, ISecurityManager {
    private IGenericService genericService;

    public IGenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(IGenericService genericService) {
        this.genericService = genericService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String hql = "from " + User.class.getName() + " a where a.username='" + s + "'";
//        System.out.println("UserDetails: " + hql);
        List userList = genericService.executeObjectSql(hql);
        if (userList != null && userList.size() > 0) {
            User user = (User)userList.get(0);
            return user;
        }

        throw new UsernameNotFoundException("账号不存在");
    }

    @Override
    public Map<String, Collection<ConfigAttribute>> loadResource() {
        Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

        String sql = "select id from sys_authority where enabled=1";
        List<String[]> authorities = genericService.executeSql(sql);

        for (String[] authority:authorities) {
            ConfigAttribute configAttribute = new SecurityConfig(authority[0]);

            sql = "select b.url from sys_authority a, sys_resource b, sys_authority_resource c" +
                    " where a.id=c.authority_id and b.id=c.resource_id and a.id='" + authority[0] + "'";
            List<String[]> resources = genericService.executeSql(sql);

            for(String[] resource:resources) {
                String url = resource[0];
                /*
                 * 判断资源文件和权限的对应关系
                 * 如果已存在相关的资源URL，则要通过改URL为key提取出权限集合，将权限增加到权限集合中。
                 */
                if (resourceMap.containsKey(url)) {
                    Collection<ConfigAttribute> value = resourceMap.get(url);
                    value.add(configAttribute);
                    resourceMap.put(url, value);
                } else {
                    Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
                    attributes.add(configAttribute);
                    resourceMap.put(url, attributes);
//                    System.out.println("url:" + url + ",attr:" + attributes.toArray()[0].toString());
                }
            }
        }

        return resourceMap;
    }
}
