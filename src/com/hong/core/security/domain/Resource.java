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
 * Date: 13-12-17
 * Time: 上午10:06
 * 资源类
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sys_resource")
public class Resource extends IdEntity {
    @Column(length = 50, nullable = false, unique = true)
    private String name;
    @Column(length = 10)
    private String type;        // 类型：action、url
    @Column(length = 100)
    private String url;
    private Integer sequence;   // 排序
    private Boolean enabled;

    @ManyToMany(mappedBy = "resources", targetEntity = Authority.class, fetch = FetchType.EAGER)
    private Set<Authority> authorities = new TreeSet<Authority>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
