package com.hong.manager.sys.queryModule.domain;

import com.hong.core.generic.domain.IdEntity;
import com.hong.manager.sys.queryModule.domain.SysQueryModule;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-5
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 * 查询列表链接
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sys_list_link")
public class SysListLink extends IdEntity {
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 100)
    private String url; // 链接
    private Integer sequence;   // 排序
    @Column(length = 50)
    private String linkType;    // 操作类型
    private Boolean display;    // 是否显示
    @Column(length = 50)
    private String opType;  // 操作类型，单挑操作、多条操作
    private Boolean verify; // 是否需要验证
    private String verifyCondition; // 验证表达式

    @OneToOne(mappedBy = "dbClickRowLink", cascade = CascadeType.REFRESH)
    private SysQueryModule queryModule;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "sysQueryModuleId")
    private SysQueryModule sysQueryModule;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }

    public String getVerifyCondition() {
        return verifyCondition;
    }

    public void setVerifyCondition(String verifyCondition) {
        this.verifyCondition = verifyCondition;
    }

    public SysQueryModule getQueryModule() {
        return queryModule;
    }

    public void setQueryModule(SysQueryModule queryModule) {
        this.queryModule = queryModule;
    }

    public SysQueryModule getSysQueryModule() {
        return sysQueryModule;
    }

    public void setSysQueryModule(SysQueryModule sysQueryModule) {
        this.sysQueryModule = sysQueryModule;
    }
}
