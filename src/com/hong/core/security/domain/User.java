package com.hong.core.security.domain;

import com.hong.core.generic.domain.IdEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-13
 * Time: 下午3:08
 * 用户类
 */

@Entity
@Table(name = "sys_user")
public class User extends IdEntity implements UserDetails {
    @Column(length = 50, nullable = false)
    private String name;    // 真实姓名
    @Column(length = 50, nullable = false, unique = true)
    private String username;    // 用户名
    @Column(length = 100, nullable = false)
    private String password;    // 密码
    @Column(length = 50)
    private String type;        // 用户类别 1-超级用户 2-管理员 3-普通用户

    @Transient
    private String confirmPassword;    // 重复密码

    @Column(length = 10)
    private String sex;     // 性别 1-男 2-女
    @Column(length = 20)
    private String idCard;  // 身份证
    @Column(length = 50)
    private String bornDate;  // 出生日期
    @Column(length = 50)
    private String phone;   // 电话
    @Column(length = 50)
    private String mobile;  // 手机
    @Column(length = 50)
    private String email;   // email
    @Column(length = 50)
    private String qq;  // QQ
    @Column(length = 50)
    private String msn; // MSN
    @Column(length = 50)
    private String beginTime; // 开始使用日期
    @Column(length = 50)
    private String endTime;   // 结束使用日期
    @Column(length = 50)
    private String lastLogoutTime;    // 上次登录时间
    @Column(length = 50)
    private String loginTime; // 登录时间
    @Column(length = 50)
    private String lastLogoutIp;    // 上次登录IP
    @Column(length = 50)
    private String loginIp; // 登录IP

    private boolean accountNonExpired;      // 账号是否过期
    private boolean accountNonLocked;       // 账号是否锁定
    private boolean credentialsNonExpired;  // 证书是否过期
    private boolean enabled;    // 账号是否启用

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "sys_user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new TreeSet<Role>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(String lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLastLogoutIp() {
        return lastLogoutIp;
    }

    public void setLastLogoutIp(String lastLogoutIp) {
        this.lastLogoutIp = lastLogoutIp;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(roles.size());
        for (Role role : roles) {
            for (Authority authority:role.getAuthorities())
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.getId().toString()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;  
    }

    @Override
    public String getUsername() {
        return username;  
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;  
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;  
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;  
    }

    @Override
    public boolean isEnabled() {
        return enabled;  
    }
}
