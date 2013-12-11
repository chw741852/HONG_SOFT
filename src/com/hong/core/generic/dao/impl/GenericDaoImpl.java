package com.hong.core.generic.dao.impl;

import com.hong.core.generic.dao.IGenericDao;
import com.hong.core.util.PublicConstants;
import com.hong.core.util.ReflectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.Serializable;
import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-29
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
@Repository("genericDao")
public class GenericDaoImpl implements IGenericDao {
    private static final Log log = LogFactory.getLog(GenericDaoImpl.class);

    @Resource
    private EntityManagerFactory entityManagerFactory;
    private EntityManager getEntityManager() {
        EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        if (null == em || !em.isOpen()) {
            em = entityManagerFactory.createEntityManager();
        }
        return em;
    }

    // 取得JDBC连接
    private Connection getConnection() {
        Session hibernateSession = getEntityManager().unwrap(Session.class);
        try {
            return SessionFactoryUtils.getDataSource(hibernateSession.getSessionFactory()).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getSqlRecordCount(String sql) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        Connection connection = getConnection();

        try {
            ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            log.error(sql);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getObjectSqlRecordCount(String hql) {
        List lt = executeObjectSql(hql, 0 , 0);
        if (lt != null && lt.size() > 0) {
            if (lt.get(0) instanceof Integer) {
                return ((Integer)lt.get(0)).intValue();
            } else {
                return ((Long)lt.get(0)).intValue();
            }
        }

        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeObjectSql(String sql, int position, int length) {
        Query query = getEntityManager().createQuery(sql);

        if (position >= 0 || length > 0) {
            query.setFirstResult(position);
            query.setMaxResults(length);
        }

        return query.getResultList();
    }

    @Override
    public List executeNoLazyObjectSql(String sql, int position, int length) {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeNoLazyObjectSql(String hql, int position, int length, String[] methodNames) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeNoLazyObjectSql(String hql, int position, int length, String methodName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeObjectSql(String hql) {
        Query query = getEntityManager().createQuery(hql);

        return query.getResultList();
    }

    @Override
    public List executeNoLazyObjectSql(String hql) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeDepthNoLazyObjectSql(String hql) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeNoLazyObjectSql(String hql, String[] methodNames) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeNoLazyObjectSql(String hql, String methodName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeSql(final String sql, final int position, final int length) {
        List lt = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = getConnection();

        try {
            if (getConnection().isClosed())
                return lt;
            ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ps.setQueryTimeout(PublicConstants.TIME_OUT);
            rs = ps.executeQuery();
            int cols = rs.getMetaData().getColumnCount();
            if (position > 0 && length != 0) {
                int n = 1;
                if (rs.absolute(position + 1))
                    while (n <= length) {
                        String[] recordArray = new String[cols];
                        for (int i = 1; i <= cols; i++) {
                            recordArray[i - 1] = ((rs.getString(i) == null ? "" : rs.getString(i)));
                        }
                        lt.add(recordArray);
                        n++;
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
            } else {
                while (rs.next()) {
                    String recordArray[] = new String[cols];
                    for (int i = 1; i <= cols; i++) {
                        recordArray[i - 1] = ((rs.getString(i) == null ? "" : rs.getString(i)));
                    }
                    lt.add(recordArray);
                }
            }
        } catch (SQLException e) {
            log.error("error execute: " + sql);
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lt;
    }

    @Override
    public List executeSql(final String sql, final int position, final int length, final Map fieldsMap) {
//        List lt = new ArrayList();
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            if (getConnection().isClosed() == true)
//                return lt;
//            ps = getConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//            rs = ps.executeQuery();
//
//            int cols = rs.getMetaData().getColumnCount();
//            if (fieldsMap != null) {
//                for (int i=0; i<cols; i++) {
//                    TableColumnInfo tableColumnInfo = new TableColumnInfo();
//                    tableColumnInfo.setFieldIndex(new Integer(i));
//                    tableColumnInfo.setFieldName(rs.getMetaData().getColumnName(i + 1));
//                    tableColumnInfo.setFieldType(rs.getMetaData().getColumnTypeName(i + 1));
//                    tableColumnInfo.setFieldSize(rs.getMetaData().getColumnDisplaySize(i + 1));
//                    fieldsMap.put(tableColumnInfo.getFieldName(), tableColumnInfo);
//                }
//                if (position != 0 && length != 0) {
//                    int n = 1;
//                    if (rs.absolute(position + 1)) {
//                        while (n <= length) {
//                            String[] recordArray = new String[cols];
//                            for (int i = 1; i <= cols; i++) {
//                                recordArray[i - 1] = ((rs.getString(i) == null ? "" : rs.getString(i)));
//                            }
//                            lt.add(recordArray);
//                            n++;
//                            if (rs.isLast())    break;
//                            rs.next();
//                        }
//                    }
//                } else {
//                    while (rs.next()) {
//                        String[] recordArray = new String[cols];
//                        for (int i = 1; i <= cols; i++) {
//                            recordArray[i - 1] = ((rs.getString(i) == null ? "" : rs.getString(i)));
//                        }
//                        lt.add(recordArray);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            log.error("error execute: " + sql);
//            log.error(e.getMessage());
//            e.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (ps != null) ps.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeSql(String sql, Map fieldsMap) {
        return executeSql(sql, 0, 0, fieldsMap);
    }

    @Override
    public List executeSql(final String sql) {
        return executeSql(sql, 0, 0);
    }

    @Override
    public boolean execute(final String sql) {
        PreparedStatement ps = null;
        boolean result = false;
        Connection connection = getConnection();
        try {
            ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            result = ps.execute();
        } catch (SQLException e) {
            log.error("error execute: " + sql);
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveObject(Object obj) {
        getEntityManager().persist(obj);
        return obj;
    }

    @Override
    public Object updateObject(Object obj) {
        return getEntityManager().merge(obj);
    }

    @Override
    public boolean deleteObject(Object obj) {
        boolean result = false;
        try {
            getEntityManager().remove(obj);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean batchExecuteHql(String hql) {
        boolean result = false;
        try {
            Query query = getEntityManager().createQuery(hql);
            query.setHint("org.hibernate.cacheable", true);
            query.executeUpdate();
            getEntityManager().flush();
            result = true;
        } catch (Exception e) {
            log.error("batchExecuteHql: " + hql);
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteObject(String sql) {
        boolean result = false;
        try {
            if (sql.indexOf("delete") >= 0) {
                Query query = getEntityManager().createQuery(sql);
                query.executeUpdate();
            } else {
                Query query = getEntityManager().createQuery(" delete from " + sql);
                query.executeUpdate();
            }
            result = true;
        } catch (Exception e) {
            log.error("deleteObject: " + sql);
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteList(List list) {
        boolean result =false;
        if (list != null && list.size() > 0) {
            for (int i=0; i<list.size(); i++) {
                if (deleteObject(list.get(i)) == false)
                    return false;
            }
            result = true;
        }
        return result;
    }

    @Override
    public boolean deleteCollection(Collection list) {
        boolean result = false;
        if (list != null && list.size() > 0) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                if (deleteObject(iterator.next()) == false)
                    return false;
            }
            result = true;
        }
        return result;
    }

    @Override
    public Object lookUp(Class objClass, Serializable id) {
        if (id != null)
            return getEntityManager().find(objClass, id);
        return null;
    }

    /**
     * 根据传递进来的setFieldNames数组决定需要延时加载哪些子表类
     *
     * @param objClass
     * @param mainId        --主表Id
     * @param setFieldNames
     * @return
     */
    @Override
    public Object findNoLazyObject(Class objClass, Serializable mainId, String[] setFieldNames) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object findNoLazyObject(Class objClass, Serializable mainId, String methodName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object findNoLazyObject(Class objClass, Serializable mainId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object lookNoLazyObject(Class objClass, Serializable mainId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object lookDepthNoLazyObject(Class objClass, Serializable mainId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object refreshCache() {
        getEntityManager().flush();
        getEntityManager().clear();
        return null;
    }

    @Override
    public Object mergeUpdate(Object obj) {
        return getEntityManager().merge(obj);
    }

    @Override
    public void loadPropertyBean(Object bean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void loadCollectionPropertyBean(Collection list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List executeReportSql(String sql, int position, int length, Object[] returnParams) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Object[]> lt = new ArrayList<Object[]>();
        Connection connection = getConnection();
        try {
            ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ps.setQueryTimeout(PublicConstants.TIME_OUT);
            rs = ps.executeQuery();
            int cols = rs.getMetaData().getColumnCount();
            if (returnParams != null && returnParams.length > 0)
                returnParams[0] = rs.getMetaData();
            if (position != 0 && length != 0) {
                int n = 1;
                if (rs.absolute(position + 1)) {
                    while (n <= length) {
                        Object[] recordArray = new Object[cols];
                        for (int i=1; i<=cols; i++) {
                            recordArray[i - 1] = rs.getObject(i);
                        }
                        lt.add(recordArray);
                        n++;
                        if (rs.isLast())    break;
                        rs.next();
                    }
                }
            } else {
                while (rs.next()) {
                    Object[] recordArray = new Object[cols];
                    for (int i=1; i<=cols; i++) {
                        recordArray[i - 1] = rs.getObject(i);
                    }
                    lt.add(recordArray);
                }
            }
        } catch (Exception e) {
            getEntityManager().close();
            log.error("executeReportSql: " + sql);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lt;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Map> executeSqlToRecordMap(String sql, int position, int length, Object[] returnParams) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map> lt = new ArrayList<Map>();
        Connection connection = getConnection();
        try {
            if (connection.isClosed())
                return lt;
            ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            int cols = rs.getMetaData().getColumnCount();
            if (position != 0 || length != 0) {
                int startPosition = position;
                int records = length;
                if (records <= 0)    records = Integer.MAX_VALUE;
                if (startPosition <= 0) startPosition = 0;
                int n = 1;
                if (rs.absolute(startPosition + 1)) {
                    while (n < records) {
                        Map record = new HashMap();
                        for (int i=1; i<=cols; i++) {
                            record.put(rs.getMetaData().getColumnName(i),
                                    (rs.getObject(i) == null ? "" : rs.getObject(i)));
                        }
                        lt.add(record);
                        n++;
                        if (rs.isLast())    break;
                        rs.next();
                    }
                }
            } else {
                while (rs.next()) {
                    Map record = new HashMap();
                    for (int i=1; i <= cols; i++) {
                        record.put(rs.getMetaData().getColumnName(i),
                                (rs.getObject(i) == null ? "" : rs.getObject(i)));
                    }
                    lt.add(record);
                }
            }
        } catch (Exception e) {
            log.error(sql);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lt;
    }

    @Override
    public List getTableSchemas() {
        ResultSet rs = null;
        List lt = new ArrayList();
        Connection connection = getConnection();
        try {
            if (connection.isClosed() == true)
                return lt;
            rs = connection.getMetaData().getSchemas();
            while (rs.next()) {
                String tableName = rs.getString(1);
                lt.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lt;
    }

    @Override
    public List getTablesInSchema(String schema) {
        ResultSet rs = null;
        List lt = new ArrayList();
        Connection connection = getConnection();
        try {
            if (connection.isClosed())
                return lt;
            DatabaseMetaData dbmd = connection.getMetaData();
            String dbName = (String) ReflectUtils.getFieldValue(dbmd, "database");
            String[] types = {"TABLE"};
            rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                String tableName = rs.getString(3);
                String db = rs.getString(1);
                if (dbName.equals(db)) {
                    lt.add(tableName);
                }
                lt.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lt;
    }

    @Override
    public List getColumnsInTable(String table) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
