package com.hong.manager.sys.queryModule.domain;

import com.hong.core.generic.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-4
 * Time: 下午1:44
 * To change this template use File | Settings | File Templates.
 * 查询生成器
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sys_query_module")
public class SysQueryModule extends IdEntity {
    @Column(length = 50)
    private String name;
    private Boolean showQueryCondition;    // 是否显示查询条件
    private Integer pageSize;   // 每页记录数
    private String headPicPath; // 导航图片路径
    private String pagePosition;    // 导航位置描述

    @OneToOne(targetEntity = SysListLink.class, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "dbClickRowLinkId")
    private SysListLink dbClickRowLink = new SysListLink();    // 行双击操作

    private String relationSql;     // 表之间关系SQL
    private String dialogResultFormat;    // 格式：${fieldName}*${fieldName1},专为对话框需返回值用的
    private String listTransferFieldFormat; // 格式：${fieldName}*${fieldName1},专为操作用的（如新建，修改、查看、删除、审核等操作)

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sysQueryModule")
    private Set<SysListLink> sysListLinks = new HashSet<SysListLink>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sysQueryModule")
    private Set<SysField> sysFields = new HashSet<SysField>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getShowQueryCondition() {
        return showQueryCondition;
    }

    public void setShowQueryCondition(Boolean showQueryCondition) {
        this.showQueryCondition = showQueryCondition;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getHeadPicPath() {
        return headPicPath;
    }

    public void setHeadPicPath(String headPicPath) {
        this.headPicPath = headPicPath;
    }

    public String getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(String pagePosition) {
        this.pagePosition = pagePosition;
    }

    public SysListLink getDbClickRowLink() {
        return dbClickRowLink;
    }

    public void setDbClickRowLink(SysListLink dbClickRowLink) {
        this.dbClickRowLink = dbClickRowLink;
    }

    public String getRelationSql() {
        return relationSql;
    }

    public void setRelationSql(String relationSql) {
        this.relationSql = relationSql;
    }

    public String getDialogResultFormat() {
        return dialogResultFormat;
    }

    public void setDialogResultFormat(String dialogResultFormat) {
        this.dialogResultFormat = dialogResultFormat;
    }

    public String getListTransferFieldFormat() {
        return listTransferFieldFormat;
    }

    public void setListTransferFieldFormat(String listTransferFieldFormat) {
        this.listTransferFieldFormat = listTransferFieldFormat;
    }

    public Set<SysListLink> getSysListLinks() {
        return sysListLinks;
    }

    public void setSysListLinks(Set<SysListLink> sysListLinks) {
        this.sysListLinks = sysListLinks;
    }

    public Set<SysField> getSysFields() {
        return sysFields;
    }

    public void setSysFields(Set<SysField> sysFields) {
        this.sysFields = sysFields;
    }
}
