package com.hong.manager.module.domain;

import com.hong.core.generic.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-11-25
 * Time: 下午2:31
 * To change this template use File | Settings | File Templates.
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sys_module")
public class Module extends IdEntity{
    @Transient
    private Long parentId;  // 父模块ID

    @Column(length = 50)
    private String name;    // 模块名称
    private Integer authority;  // 权限等级
    private String actionUrl;   // action地址
    private Boolean isMenu;   // 是否主菜单显示
    private Integer level;  // 菜单所在层次
    private Boolean isDisplay;  // 该功能模块是否显示
    private Integer sequence;  // 排列序号
    private Boolean isSys;  // 是否系统模块

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "parentId")
    private Module parent;    // 父模块ID
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parent")
    private Set<Module> children = new HashSet<Module>();       // 子模块

    public Module getParent() {
        return parent;
    }

    public void setParent(Module parent) {
        this.parent = parent;
    }

    public Set<Module> getChildren() {
        return children;
    }

    public void setChildren(Set<Module> children) {
        this.children = children;
    }

    public Boolean getSys() {
        return isSys;
    }

    public void setSys(Boolean sys) {
        isSys = sys;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Boolean getMenu() {
        return isMenu;
    }

    public void setMenu(Boolean menu) {
        isMenu = menu;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getDisplay() {
        return isDisplay;
    }

    public void setDisplay(Boolean display) {
        isDisplay = display;
    }
}
