package com.hong.core.security.support;

import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;
import java.util.Map;

/**
 * Created by hong on 14-3-8 下午2:15.
 * 定义loadResource方法，UserDetailsServiceSupport继承
 */
public interface ISecurityManager {
    public Map<String, Collection<ConfigAttribute>> loadResource();
}
