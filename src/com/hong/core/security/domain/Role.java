package com.hong.core.security.domain;

import com.hong.core.generic.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;


/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-13
 * Time: 下午4:50
 * 角色类
 */

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sys_role")
public class Role extends IdEntity {
    @Column(length = 50, nullable = false, unique = true)
    private String name;
    private Integer sequence;   // 排序
    private Boolean enabled;    // 是否启用

    @ManyToMany(mappedBy = "roles", targetEntity = User.class, fetch = FetchType.EAGER)
    private Set<User> users = new TreeSet<User>();

    @ManyToMany(targetEntity = Authority.class, fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_authority", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new TreeSet<Authority>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
