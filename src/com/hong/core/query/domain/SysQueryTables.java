package com.hong.core.query.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-29
 * Time: 下午5:35
 * To change this template use File | Settings | File Templates.
 */

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "hong_query_tables")
public class SysQueryTables implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    @Column(length = 32)
    private Long id;
    @Version
    private Integer version;

    @Column(length = 50)
    private String tableName;   // 表名
    @Column(length = 50)
    private String tableLabel;  // 表的标签名
    @Column(length = 50)
    private String beanPackageName; // 表对应的对象全路径名
    @Column(length = 50)
    private String primaryFieldName;    // 表主键名称,默认为 "id"
    @Column(length = 50)
    private String primaryPropertyName; // 主键对应bean属性名称
    private Long primaryFieldValue;  // 最新主键值
    @Column(length = 50)
    private String numberName;      // 表的主编码名称
    @Column(length = 50)
    private String numberPropertyName;  // 主编码对应bean属性名
    @Column(length = 50)
    private String numberValue;     // 最新主编码值
    @Column(length = 50)
    private String subNumberName;   // 第二编码名称
    @Column(length = 50)
    private String subNumberPropertyName;   // 第二编码对应bean属性名
    @Column(length = 50)
    private String subNumberValue;  // 第二编码最新值
    @Column(length = 50)
    private String codeGenerateRule;    // 编码产生规则
    @Column(length = 50)
    private String springServiceName;   // 配置针对IImportData数据导入接口提供的spring服务实例名称
    @Column(length = 50)
    private String comments;        // 备注等
    private String importDataExcelFile; // 导入数据excel模板文件路径
    @Column(length = 50)
    private String repeatCondition; // 导入数据时判断记录重复的条件
    @Column(length = 50)
    private String insertGenerateEventSql;  // 添加记录后需要产生相应的其他时间sql语句
    @Column(length = 50)
    private String needUpdateOldRecord;     // 导入数据时发现相同记录后是否需要更新原记录

    // 编码规则参数设置
    private Boolean isHql;   // 是否通过HQL查询来生成编码
    private String sqlCondition;    // 条件语句
    @Column(length = 50)
    private String prefix;      // 编码前缀
    @Column(length = 50)
    private String suffix;      // 编码后缀
    @Column(length = 50)
    private String dateFormat;  // 编码时间格式,一般yyyyMM,前6位
    private Integer sequenceNoLen;  // 序列号位数,一般为6位
    @Column(length = 50)
    private String sequenceFormat;  // 序列号格式,一般为"000000"
    private Boolean isShare;     // 是否其他共享管理该表

    @Transient
    private Set tableFields; // TODO 不清楚字段意思,先不管

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableLabel() {
        return tableLabel;
    }

    public void setTableLabel(String tableLabel) {
        this.tableLabel = tableLabel;
    }

    public String getBeanPackageName() {
        return beanPackageName;
    }

    public void setBeanPackageName(String beanPackageName) {
        this.beanPackageName = beanPackageName;
    }

    public String getPrimaryFieldName() {
        return primaryFieldName;
    }

    public void setPrimaryFieldName(String primaryFieldName) {
        this.primaryFieldName = primaryFieldName;
    }

    public String getPrimaryPropertyName() {
        return primaryPropertyName;
    }

    public void setPrimaryPropertyName(String primaryPropertyName) {
        this.primaryPropertyName = primaryPropertyName;
    }

    public Long getPrimaryFieldValue() {
        return primaryFieldValue;
    }

    public void setPrimaryFieldValue(Long primaryFieldValue) {
        this.primaryFieldValue = primaryFieldValue;
    }

    public String getNumberName() {
        return numberName;
    }

    public void setNumberName(String numberName) {
        this.numberName = numberName;
    }

    public String getNumberPropertyName() {
        return numberPropertyName;
    }

    public void setNumberPropertyName(String numberPropertyName) {
        this.numberPropertyName = numberPropertyName;
    }

    public String getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(String numberValue) {
        this.numberValue = numberValue;
    }

    public String getSubNumberName() {
        return subNumberName;
    }

    public void setSubNumberName(String subNumberName) {
        this.subNumberName = subNumberName;
    }

    public String getSubNumberPropertyName() {
        return subNumberPropertyName;
    }

    public void setSubNumberPropertyName(String subNumberPropertyName) {
        this.subNumberPropertyName = subNumberPropertyName;
    }

    public String getSubNumberValue() {
        return subNumberValue;
    }

    public void setSubNumberValue(String subNumberValue) {
        this.subNumberValue = subNumberValue;
    }

    public String getCodeGenerateRule() {
        return codeGenerateRule;
    }

    public void setCodeGenerateRule(String codeGenerateRule) {
        this.codeGenerateRule = codeGenerateRule;
    }

    public String getSpringServiceName() {
        return springServiceName;
    }

    public void setSpringServiceName(String springServiceName) {
        this.springServiceName = springServiceName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getImportDataExcelFile() {
        return importDataExcelFile;
    }

    public void setImportDataExcelFile(String importDataExcelFile) {
        this.importDataExcelFile = importDataExcelFile;
    }

    public String getRepeatCondition() {
        return repeatCondition;
    }

    public void setRepeatCondition(String repeatCondition) {
        this.repeatCondition = repeatCondition;
    }

    public String getInsertGenerateEventSql() {
        return insertGenerateEventSql;
    }

    public void setInsertGenerateEventSql(String insertGenerateEventSql) {
        this.insertGenerateEventSql = insertGenerateEventSql;
    }

    public String getNeedUpdateOldRecord() {
        return needUpdateOldRecord;
    }

    public void setNeedUpdateOldRecord(String needUpdateOldRecord) {
        this.needUpdateOldRecord = needUpdateOldRecord;
    }

    public Boolean getHql() {
        return isHql;
    }

    public void setHql(Boolean hql) {
        isHql = hql;
    }

    public String getSqlCondition() {
        return sqlCondition;
    }

    public void setSqlCondition(String sqlCondition) {
        this.sqlCondition = sqlCondition;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Integer getSequenceNoLen() {
        return sequenceNoLen;
    }

    public void setSequenceNoLen(Integer sequenceNoLen) {
        this.sequenceNoLen = sequenceNoLen;
    }

    public String getSequenceFormat() {
        return sequenceFormat;
    }

    public void setSequenceFormat(String sequenceFormat) {
        this.sequenceFormat = sequenceFormat;
    }

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
    }

    public Set getTableFields() {
        return tableFields;
    }

    public void setTableFields(Set tableFields) {
        this.tableFields = tableFields;
    }
}
