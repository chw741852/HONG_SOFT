package com.hong.core.security.support;

import com.hong.core.generic.service.IGenericService;
import com.hong.core.security.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-17
 * Time: 下午3:53
 * 为Spring Security提供一个经过用户认证后的UserDetails。
 */
public class UserDetailsServiceSupport implements UserDetailsService {
    private IGenericService genericService;

    public IGenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(IGenericService genericService) {
        this.genericService = genericService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String hql = "from " + User.class.getName() + " a where a.name='" + s + "'";
        System.out.println("UserDetails: " + hql);
        List userList = genericService.executeObjectSql(hql);
        if (userList != null && userList.size() > 0) {
            User user = (User)userList.get(0);
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), true, true, true, true, user.getAuthorities());
        }

        throw new UsernameNotFoundException(s);
    }
}
