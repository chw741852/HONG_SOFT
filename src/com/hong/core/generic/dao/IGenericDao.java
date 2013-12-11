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
    public int getSqlRecordCount(final String sql);
    public int getObjectSqlRecordCount(final String hql);

    public List executeObjectSql(final String sql, final int position, final int length);
    public List executeNoLazyObjectSql(final String sql, final int position, final int length);
    public List executeNoLazyObjectSql(final String hql, final int position, final int length,final String[] methodNames);
    public List executeNoLazyObjectSql(final String hql, final int position, final int length,final String methodName);

    public List executeObjectSql(final String hql);
    public List executeNoLazyObjectSql(final String hql);
    public List executeDepthNoLazyObjectSql(final String hql);
    public List executeNoLazyObjectSql(final String hql,final String[] methodNames);
    public List executeNoLazyObjectSql(final String hql,final String methodName);

    public List executeSql(final String sql, final int position,final int length);
    public List executeSql(final String sql, final int position,final int length,final Map fieldsMap);
    public List executeSql(final String sql, final Map fieldsMap);
    public List executeSql(final String sql);

    public boolean execute(final String sql);
    public Object saveObject(final Object obj);//保存对象
    public Object updateObject(final Object obj);//更新对象
    public Object mergeUpdate(final Object obj);
    public boolean deleteObject(final Object obj);//删除对象
    public boolean batchExecuteHql(final String sql);//批量执行hql
    public boolean deleteObject(final String sql);//根据SQL语句删除对象
    public boolean deleteList(List list);//删除list集合中所有对象
    public boolean deleteCollection(Collection list);//删除Collection集合中所有对象
    public Object lookUp(Class objClass, Serializable id);

    /**
     *根据传递进来的setFieldNames数组决定需要延时加载哪些子表类
     * @param objClass
     * @param mainId --主表Id
     * @param setFieldNames
     * @return
     */
    public Object findNoLazyObject(Class objClass,Serializable mainId,String[] setFieldNames);

    public Object findNoLazyObject(final Class objClass, final Serializable mainId, final String methodName);
    public Object findNoLazyObject(Class objClass,Serializable mainId);
    public Object lookNoLazyObject(final Class objClass, final Serializable mainId);
    public Object lookDepthNoLazyObject(final Class objClass, final Serializable mainId);

    public Object refreshCache();

    public void loadPropertyBean(Object bean);
    public void loadCollectionPropertyBean(Collection list);

    public List executeReportSql(final String sql, final int position,final int length,final Object[] returnParams);
    public  List<Map>  executeSqlToRecordMap(final String sql, final int position,final int length,final Object[] returnParams);

    public List getTableSchemas();
    public List getTablesInSchema(final String schema);
    public List getColumnsInTable(final String table);
}
