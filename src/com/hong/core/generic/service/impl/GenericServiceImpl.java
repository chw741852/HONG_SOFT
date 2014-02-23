package com.hong.core.generic.service.impl;

import com.hong.core.generic.dao.IGenericDao;
import com.hong.core.generic.service.IGenericService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-29
 * Time: 下午5:25
 * 公共service
 */

@Service("genericService")
@Transactional
public class GenericServiceImpl implements IGenericService {
    @Resource
    private IGenericDao genericDao;

    @Override
    public List executeObjectSql(String sql, int position, int length) {
        return genericDao.executeObjectSql(sql, position, length);
    }

    @Override
    public List<String[]> executeSql(String sql, int position, int length) {
        return genericDao.executeSql(sql, position, length);
    }

    @Override
    public List<String[]> executeSql(String sql) {
        return genericDao.executeSql(sql);
    }

    @Override
    public List executeObjectSql(String hql) {
        return genericDao.executeObjectSql(hql);
    }

    @Override
    public boolean execute(String sql) throws Exception {
        return genericDao.execute(sql);
    }

    @Override
    public long getSqlRecordCount(String sql) {
        return genericDao.getSqlRecordCount(sql);
    }

    @Override
    public long getObjectSqlRecordCount(String hql) {
        return genericDao.getObjectSqlRecordCount(hql);
    }

    @Override
    public Object saveObject(Object obj) {
        return genericDao.saveObject(obj);
    }

    @Override
    public Object updateObject(Object obj) {
        return genericDao.updateObject(obj);
    }

    @Override
    public boolean deleteObject(Object obj) {
        return genericDao.deleteObject(obj);
    }

    @Override
    public int batchExecuteHql(String hql) {
        return genericDao.batchExecuteHql(hql);
    }

    @Override
    public boolean deleteList(List list) {
        return genericDao.deleteList(list);
    }

    @Override
    public boolean deleteCollection(Collection list) {
        return genericDao.deleteCollection(list);
    }

    @Override
    public boolean deleteObjectHql(String hql) {
        return genericDao.deleteObject(hql);
    }

    @Override
    public Object lookUp(Class objectClass, Serializable id) {
        return genericDao.lookUp(objectClass, id);
    }

    @Override
    public Object refreshCache() {
        return genericDao.refreshCache();
    }

    @Override
    public void loadPropertyBean(Object bean) {
        genericDao.loadPropertyBean(bean);
    }

    @Override
    public void loadCollectionPropertyBean(Collection list) {
        genericDao.loadCollectionPropertyBean(list);
    }

    @Override
    public List executeReportSql(String sql, int position, int length, Object[] returnParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeReportSql(String sql, Object[] returnParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeReportSql(String sql, int position, int length) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeReportSql(String sql) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Map> executeSqlToRecordMap(String sql, int position, int length, Object[] returnParams) {
        return genericDao.executeSqlToRecordMap(sql, position, length, returnParams);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Map> executeSqlToRecordMap(String sql, Object[] returnParams) {
        return genericDao.executeSqlToRecordMap(sql, 0, 0, returnParams);
    }

    @Override
    public List<Map> executeSqlToRecordMap(String sql, int position, int length) {
        return genericDao.executeSqlToRecordMap(sql, position, length, null);
    }

    @Override
    public List<Map> executeSqlToRecordMap(String sql) {
        return genericDao.executeSqlToRecordMap(sql, 0, 0, null);
    }

    @Override
    public List getTableSchemas() {
        return genericDao.getTableSchemas();
    }

    @Override
    public List getTablesInSchema(String schema, String tableName) {
        return genericDao.getTablesInSchema(schema, tableName);
    }

    @Override
    public List getColumnsInTable(String table, String column) {
        return genericDao.getColumnsInTable(table, column);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
