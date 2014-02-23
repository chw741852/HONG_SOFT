package com.hong.core.generic.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-29
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
public interface IGenericService {
    /**
     * 执行查询对象HQL语句(若position=0,length=0时表示查询表中所有记录)
     * @param sql 对象SQL语句
     * @param position 当前页位置
     * @param length   改页面包括的记录数
     * @return
     */
    public List executeObjectSql(final String sql, final int position, final int length);

    /**
     * 执行SQL语句(可分页取出某段记录，若position=0,length=0时表示查询表中所有记录)
     * @param sql
     * @param position
     * @param length
     * @return
     */
    public List executeSql(final String sql, final int position, final int length);

    /**
     * 执行sql语句，并返回所有查询结果
     * @param sql
     * @return
     */
    public List executeSql(final String sql);

    /**
     * 执行hql,并返回所有查询结果
     * @param hql
     * @return
     */
    public List executeObjectSql(final String hql);

    /**
     * 执行Sql语句，不返回结果集(如添加、修改、删除功能)
     * @param sql
     * @return
     * @throws Exception
     */
    public boolean execute(final String sql) throws Exception;

    /**
     * 取得sql语句记录数
     * @param sql
     * @return
     */
    public long getSqlRecordCount(final String sql);

    /**
     * 取得对象hql语句记录数
     * @param hql
     * @return
     */
    public long getObjectSqlRecordCount(final String hql);

    /**
     * 保存对象
     * @param obj
     * @return 返回保存后的对象
     */
    public Object saveObject(final Object obj);

    /**
     * 更新对象
     * @param obj
     * @return 返回更新后的对象
     */
    public Object updateObject(final Object obj);

    /**
     * 删除单个对象
     * @param obj
     * @return 返回boolean，删除成功放回true，删除失败返回false
     */
    public boolean deleteObject(final Object obj);

    /**
     * 批量执行Hsql语句
     * @param hql
     * @return 布尔类型.执行sql正确就返回true,否则返回false
     */
    public int batchExecuteHql(final String hql);

    /**
     * 删除list集合中的所有对象
     * @param list
     * @return
     */
    public boolean deleteList(List list);

    /**
     * 删除Collection集合中所有对象,用来替换deleteList函数功能
     * @param list
     * @return
     */
    public boolean deleteCollection(Collection list);

    /**
     * 根据HSQL语句批量删除对象
     * @param hql
     * @return  返回布尔类型,删除成功为tru,否则为false
     */
    public boolean deleteObjectHql(final String hql);

    /**
     * 根据主键查询对象
     * @param objectClass 对象的class
     * @param id 主键
     * @return 返回查询的对象,若为空表示查询不成功
     */
    public Object lookUp(Class objectClass, Serializable id);

    public Object refreshCache();

    /**
     * 根据bean中设置的HibernateLoadPropertyBean元数据，加载对象中属性对象
     * @param bean 要加载的bean对象
     */
    public void loadPropertyBean(Object bean);

    /**
     * 根据bean中设置的HibernateLoadPropertyBean元数据，加载对象中属性对象
     * @param list
     */
    public void loadCollectionPropertyBean(Collection list);

    /**
     * 执行报表的sql语句，并返回所有查询结果数据集
     * @param sql
     * @param position
     * @param length
     * @param returnParams
     * @return
     */
    public List executeReportSql(final String sql, final int position, final int length, final Object[] returnParams);
    public List executeReportSql(final String sql,final Object[] returnParams);
    public List executeReportSql(final String sql, final int position,final int length);
    public List executeReportSql(final String sql);

    public List<Map> executeSqlToRecordMap(final String sql, final int position,final int length,final Object[] returnParams);
    public List<Map> executeSqlToRecordMap(final String sql,final Object[] returnParams);
    public List<Map> executeSqlToRecordMap(final String sql, final int position,final int length);
    public List<Map> executeSqlToRecordMap(final String sql);

    /**
     * 取得所数据库名称
     * @return
     */
    public List getTableSchemas();

    /**
     * 取得当前数据库中所有表名
     * @param schema
     * @return
     */
    public List getTablesInSchema(final String schema, final String tableName);

    /**
     * 取得当前表中所有字段
     * @param table
     * @return
     */
    public List getColumnsInTable(final String table, final String column);
}
