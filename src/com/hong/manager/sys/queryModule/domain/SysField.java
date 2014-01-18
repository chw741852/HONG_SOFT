package com.hong.manager.sys.queryModule.domain;

import com.hong.core.generic.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-5
 * Time: 上午11:52
 * To change this template use File | Settings | File Templates.
 * 查询字段
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sys_field")
public class SysField extends IdEntity {
    @Column(length = 50)
    private String displayName; // 字段显示名称
    @Column(length = 50)
    private String tableName;   // 表名
    @Column(length = 50)
    private String tableAliasName;  // 表别名
    @Column(length = 50)
    private String fieldOpType;     // 字段操作类别
    @Column(length = 50)
    private String fieldName;   // 字段名
    @Column(length = 50)
    private String fieldAliasName;  // 字段别名
    @Column(length = 50)
    private String replacementValue;    // 替换值
    @Column(length = 50)
    private String fieldDataType;   // 字段数据类型
    @Column(length = 100)
    private String hyperlink;   // 超链接
    private Boolean isQueryCondition;   // 是否查询条件
    private Boolean isDisplay;  // 是否显示结果
    private Boolean isPrint;    // 是否打印
    private Integer querySequence;  // 显示查询条件顺序
    private Integer displaySequence;    // 显示结果顺序
    private Integer printSequence;  // 打印顺序
    @Column(length = 50)
    private String rowWidth;    // 列宽
    @Column(length = 50)
    private String displayType; // 显示方式
    @Column(length = 50)
    private String orderBy; // 排序类型
    @Column(length = 50)
    private String controlKind; // 页面控件类型
    @Column(length = 50)
    private String dataSourceType;  // 数据来源类型
    @Column(length = 100)
    private String dataSourceSql;   // 数据来源SQL语句

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "sysQueryModuleId")
    private SysQueryModule sysQueryModule;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public String getFieldOpType() {
        return fieldOpType;
    }

    public void setFieldOpType(String fieldOpType) {
        this.fieldOpType = fieldOpType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldAliasName() {
        return fieldAliasName;
    }

    public void setFieldAliasName(String fieldAliasName) {
        this.fieldAliasName = fieldAliasName;
    }

    public String getReplacementValue() {
        return replacementValue;
    }

    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }

    public String getFieldDataType() {
        return fieldDataType;
    }

    public void setFieldDataType(String fieldDataType) {
        this.fieldDataType = fieldDataType;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public Boolean getQueryCondition() {
        return isQueryCondition;
    }

    public void setQueryCondition(Boolean queryCondition) {
        isQueryCondition = queryCondition;
    }

    public Boolean getDisplay() {
        return isDisplay;
    }

    public void setDisplay(Boolean display) {
        isDisplay = display;
    }

    public Boolean getPrint() {
        return isPrint;
    }

    public void setPrint(Boolean print) {
        isPrint = print;
    }

    public Integer getQuerySequence() {
        return querySequence;
    }

    public void setQuerySequence(Integer querySequence) {
        this.querySequence = querySequence;
    }

    public Integer getDisplaySequence() {
        return displaySequence;
    }

    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    public Integer getPrintSequence() {
        return printSequence;
    }

    public void setPrintSequence(Integer printSequence) {
        this.printSequence = printSequence;
    }

    public String getRowWidth() {
        return rowWidth;
    }

    public void setRowWidth(String rowWidth) {
        this.rowWidth = rowWidth;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getControlKind() {
        return controlKind;
    }

    public void setControlKind(String controlKind) {
        this.controlKind = controlKind;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDataSourceSql() {
        return dataSourceSql;
    }

    public void setDataSourceSql(String dataSourceSql) {
        this.dataSourceSql = dataSourceSql;
    }

    public SysQueryModule getSysQueryModule() {
        return sysQueryModule;
    }

    public void setSysQueryModule(SysQueryModule sysQueryModule) {
        this.sysQueryModule = sysQueryModule;
    }
}
