package com.hong.manager.sys.dictionary.domain;

import com.hong.core.generic.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-8
 * Time: 下午6:02
 * To change this template use File | Settings | File Templates.
 * 字典表主表
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sys_dictionary")
public class SysDictionary extends IdEntity {
    private String name;    // 名称
    @Column(name = "`key`")
    private String key;     // 键
    private Integer sequence;   // 序号

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "parentId")
    private SysDictionary parent;   // 父节点
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parent")
    private Set<SysDictionary> children = new HashSet<SysDictionary>(); // 子节点
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sysDictionary")
    private Set<SysCode> sysCodes = new HashSet<SysCode>();     // 从表

    @Transient
    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public SysDictionary getParent() {
        return parent;
    }

    public void setParent(SysDictionary parent) {
        this.parent = parent;
    }

    public Set<SysDictionary> getChildren() {
        return children;
    }

    public void setChildren(Set<SysDictionary> children) {
        this.children = children;
    }

    public Set<SysCode> getSysCodes() {
        return sysCodes;
    }

    public void setSysCodes(Set<SysCode> sysCodes) {
        this.sysCodes = sysCodes;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
