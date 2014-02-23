package com.hong.core.generic.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-29
 * Time: 下午2:20
 * To change this template use File | Settings | File Templates.
 */
public interface IGenericDao {
    public long getSqlRecordCount(final String sql);
    public long getObjectSqlRecordCount(final String hql);

    public List executeObjectSql(final String sql, final int position, final int length);

    public List executeObjectSql(final String hql);

    public List executeSql(final String sql, final int position,final int length);
    public List executeSql(final String sql);

    public boolean execute(final String sql);
    public Object saveObject(final Object obj);//保存对象
    public Object updateObject(final Object obj);//更新对象
    public boolean deleteObject(final Object obj);//删除对象
    public int batchExecuteHql(final String hql);//批量执行hql
    public int deleteObjectHql(final String hql);//根据SQL语句删除对象
    public boolean deleteList(List list);//删除list集合中所有对象
    public boolean deleteCollection(Collection list);//删除Collection集合中所有对象
    public Object lookUp(Class objClass, Serializable id);

    public Object refreshCache();

    public void loadPropertyBean(Object bean);
    public void loadCollectionPropertyBean(Collection list);

    public List executeReportSql(final String sql, final int position,final int length,final Object[] returnParams);
    public List<Map> executeSqlToRecordMap(final String sql, final int position,final int length,final Object[] returnParams);

    public List getTableSchemas();
    public List getTablesInSchema(final String schema, final String tableName);
    public List getColumnsInTable(final String table, final String column);
}
