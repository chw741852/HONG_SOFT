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
    public List executeNoLazyObjectSql(String sql, int position, int length) {
        return genericDao.executeNoLazyObjectSql(sql, position, length);
    }

    @Override
    public List<String[]> executeSql(String sql, int position, int length) {
        return genericDao.executeSql(sql, position, length);
    }

    @Override
    public List<String[]> executeSql(String sql, int position, int length, Map fieldsMap) {
        return genericDao.executeSql(sql, position, length, fieldsMap);
    }

    @Override
    public List<String[]> executeSql(String sql, Map fieldsMap) {
        return genericDao.executeSql(sql, fieldsMap);
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
    public List executeNoLazyObjectSql(String hql) {
        return genericDao.executeNoLazyObjectSql(hql);
    }

    @Override
    public List executeDepthNoLazyObjectSql(String hql) {
        return genericDao.executeDepthNoLazyObjectSql(hql);
    }

    @Override
    public boolean execute(String sql) throws Exception {
        return genericDao.execute(sql);
    }

    @Override
    public int getSqlRecordCount(String sql) {
        return genericDao.getSqlRecordCount(sql);
    }

    @Override
    public int getObjectSqlRecordCount(String hql) {
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
    public boolean batchExecuteHql(String hql) {
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
    public boolean deleteObject(String hql) {
        return genericDao.deleteObject(hql);
    }

    @Override
    public Object lookUp(Class objectClass, Serializable id) {
        return genericDao.lookUp(objectClass, id);
    }

    @Override
    public Object findNoLazyObject(Class objClass, Serializable id, String[] setFieldNames) {
        return genericDao.findNoLazyObject(objClass, id, setFieldNames);
    }

    @Override
    public Object findNoLazyObject(Class objClass, Serializable id, String methodName) {
        return genericDao.findNoLazyObject(objClass, id, methodName);
    }

    @Override
    public Object findNoLazyObject(Class objClass, Serializable id) {
        return genericDao.findNoLazyObject(objClass, id);
    }

    @Override
    public Object lookNoLazyObject(Class objClass, Serializable id) {
        return genericDao.lookDepthNoLazyObject(objClass, id);
    }

    @Override
    public Object lookDepthNoLazyObject(Class objClass, Serializable id) {
        return genericDao.lookDepthNoLazyObject(objClass, id);
    }

    @Override
    public Object refreshCache() {
        return genericDao.refreshCache();
    }

    @Override
    public Object mergeUpdate(Object obj) {
        return genericDao.mergeUpdate(obj);
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
    public Object saveAutoIdObject(Object obj) {
        String tableBeanClassName = obj.getClass().getName();

        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List getTablesInSchema(String schema) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List getColumnsInTable(String table) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
