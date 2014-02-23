package com.hong.core.generic.dao.impl;

import com.hong.core.generic.dao.IGenericDao;
import com.hong.core.util.PublicConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager em;
//    @Resource
//    private EntityManagerFactory entityManagerFactory;
//    private EntityManager getEntityManager() {
//        EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
//        if (null == em || !em.isOpen()) {
//            em = entityManagerFactory.createEntityManager();
//        }
//        return em;
//    }

    // 取得JDBC连接
    private Connection getConnection() {
        Session hibernateSession = em.unwrap(Session.class);

        try {
            return SessionFactoryUtils.getDataSource(hibernateSession.getSessionFactory()).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public long getSqlRecordCount(String sql) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = getConnection();
        long count = 0;
        
        try {
            ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error(sql);
        } finally {
            if (rs != null) try {
                rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.error(sql);
            }
        }

        return count;
    }

    @Override
    public long getObjectSqlRecordCount(String hql) {
        List lt = executeObjectSql(hql, 0 , 0);
        if (lt != null && lt.size() > 0) {
            return ((Long)lt.get(0)).longValue();
        }

        return 0;
    }

    @Override
    public List executeObjectSql(String sql, int position, int length) {
        Query query = em.createQuery(sql);

        if (position >= 0 && length > 0) {
            query.setFirstResult((position - 1) * length);
            query.setMaxResults(length);
        }

        return query.getResultList();
    }

    @Override
    public List executeObjectSql(String hql) {
        Query query = em.createQuery(hql);

        return query.getResultList();
    }

    @Override
    public List<String[]> executeSql(final String sql, final int position, final int length) {
        List<String[]> lt = new ArrayList<String[]>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = getConnection();

        try {
            ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ps.setQueryTimeout(PublicConstants.TIME_OUT);
            rs = ps.executeQuery();
            int cols = rs.getMetaData().getColumnCount();
            if (position > 0 && length != 0) {
                int n = 1;
                if (rs.absolute((position - 1) * length + 1))
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
            e.printStackTrace();
            log.error(e.getMessage());
            log.error(sql);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.error(sql);
            }
        }

        return lt;
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
            e.printStackTrace();
            log.error(e.getMessage());
            log.error(sql);
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.error(sql);
            }
        }

        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveObject(Object obj) {
        em.persist(obj);
        return obj;
    }

    @Override
    public Object updateObject(Object obj) {
        return em.merge(obj);
    }

    @Override
    public boolean deleteObject(Object obj) {
        em.remove(obj);
        return true;
    }

    @Override
    public int batchExecuteHql(String hql) {
        int result = 0;

        Query query = em.createQuery(hql);
        query.setHint("org.hibernate.cacheable", true);
        result = query.executeUpdate();

        return result;
    }

    @Override
    public int deleteObjectHql(String hql) {
        int result = 0;
        if (hql.indexOf("delete") >= 0) {
            Query query = em.createQuery(hql);
            result = query.executeUpdate();
        } else {
            Query query = em.createQuery(" delete from " + hql);
            result = query.executeUpdate();
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
            return em.find(objClass, id);
        return null;
    }

    @Override
    public Object refreshCache() {
        em.flush();
        em.clear();
        return null;
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
                if (rs.absolute((position - 1) * length + 1)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error(sql);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.error(sql);
            }
        }

        return lt;
    }

    @Override
    public List<Map> executeSqlToRecordMap(String sql, int position, int length, Object[] returnParams) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map> lt = new ArrayList<Map>();
        Connection connection = getConnection();

        try {
            ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            int cols = rs.getMetaData().getColumnCount();
            if (returnParams != null && returnParams.length > 0)
                returnParams[0] = rs.getMetaData();
            if (position != 0 || length != 0) {
                int startPosition = position;
                int records = length;
                if (records <= 0)    records = Integer.MAX_VALUE;
                if (startPosition <= 0) startPosition = 1;
                int n = 1;
                if (rs.absolute((startPosition-1)*records + 1)) {
                    while (n <= records) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error(sql);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.error(sql);
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
            rs = connection.getMetaData().getSchemas();
            while (rs.next()) {
                String tableName = rs.getString(1);
                lt.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }

        return lt;
    }

    @Override
    public List<String> getTablesInSchema(String schema, String tableName) {
        ResultSet rs = null;
        List<String> lt = new ArrayList<String>();
        Connection connection = getConnection();

        DatabaseMetaData dbmd = null;
        String[] types = {"TABLE"};

        try {
            dbmd = connection.getMetaData();
            rs = dbmd.getTables(null, null, tableName, types);
            while (rs.next()) {
                String tName = rs.getString(3);
                String db = rs.getString(1);
                lt.add(tName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }

        return lt;
    }

    @Override
    public List<String> getColumnsInTable(String table, String Column) {
        ResultSet rs = null;
        List<String> lt = new ArrayList<String>();
        Connection connection = getConnection();

        DatabaseMetaData dbmd = null;

        try {
            dbmd = connection.getMetaData();
            rs = dbmd.getColumns(null, null, table, Column);
            while (rs.next()) {
                String tName = rs.getString(4);
                lt.add(tName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }

        return lt;
    }
}
