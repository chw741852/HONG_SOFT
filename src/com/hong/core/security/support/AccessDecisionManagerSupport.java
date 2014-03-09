package com.hong.core.security.support;

import com.hong.core.security.domain.User;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-17
 * Time: 下午4:29
 * AccessDecisionManager被AbstractSecurityInterceptor调用，它用来作最终访问控制的决定。
 * 如果访问被拒绝，实现将抛出一个AccessDeniedException异常。
 */
public class AccessDecisionManagerSupport implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes == null)   return;

        Iterator<ConfigAttribute> configAttributeIterator = configAttributes.iterator();
        while (configAttributeIterator.hasNext()) {
            ConfigAttribute ca = configAttributeIterator.next();
            String needRole = ((SecurityConfig)ca).getAttribute();

            // grantedAuthority为用户所赋予的权限，needRole为访问相应的资源所需要的权限。
            for (GrantedAuthority grantedAuthority:authentication.getAuthorities()) {
                if (needRole.trim().equals(grantedAuthority.getAuthority().trim()))
                    return;
            }
        }

        throw new AccessDeniedException("您没有该资源的访问权限！");
    }

    /*
     * 在启动的时候被AbstractSecurityInterceptor调用,
     * 来决定AccessDecisionManager是否可以执行传递ConfigAttribute。
     */
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    // 被安全拦截器实现调用
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
