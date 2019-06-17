package com.cloud.mina.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * C3PO 工具类，用于操作 MySQL
 */
public class C3P0Util {
    private static Logger log = Logger.getLogger(C3P0Util.class);
    private static DataSource ds = null;

    static {
//       默认读取classpath下的 c3p0.config.xml
        ds = new ComboPooledDataSource();
    }

    /**
     * 获取一个数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("C3PO 连接池获取数据库连接失败！");
        }
        return null;
    }

    public static int getCount(String sql) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int i = 0;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                i = Integer.parseInt(rs.getString(1));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            releaseResource(conn, st, rs);
        }
        return i;
    }

    public static boolean executeUpdate(String sql) {
        Connection conn = null;
        Statement st = null;
        boolean ret = true;
        try {
            conn = getConnection();
            st = conn.createStatement();
            st.execute(sql);
            System.out.println(sql);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            ret = false;
        } finally {
            releaseResource(conn, st, null);
        }
        return ret;
    }

    /**
     * ＊可插入任意个数 value 参数 但sql 中的问号个数务必与 value 个数相同
     *
     * @param sql
     * @param value
     * @return
     */
    public static boolean insertOrUpdateData(String sql, String... value) {
        log.info(sql + Arrays.toString(value));
        Connection conn = null;
        PreparedStatement prst = null;
        boolean ret = false;
        int num = 0;
        try {
            conn = getConnection();
            prst = conn.prepareStatement(sql);
            for (int i = 0; i < value.length; i++) {
                prst.setString(i + 1, value[i]);
            }
            num = prst.executeUpdate();
            if (num > 0) {
                ret = true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            ret = false;
        } finally {
            releaseResource(conn, prst, null);
        }
        return ret;
    }

    /**
     * 执行删除操作，并返回影响的行数
     */
    public static int executeDelete(String sql) throws SQLException {
        Connection conn = null;
        Statement st = null;
        int result = 0;
        try {
            conn = getConnection();
            st = conn.createStatement();
            result = st.executeUpdate(sql);
            System.out.println(sql);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            releaseResource(conn, st, null);
        }
        return result;
    }

    /**
     * JDBC 获取数据 结果组织成 HashMap 形式 key 为列名 value 为数据 一条 HashMa
     * 应于查询到的 条数据
     *
     * @param sql
     * @return
     */
    public static List<HashMap<String, String>> getData(String sql) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    map.put(rsmd.getColumnLabel(i + 1), rs.getString(i + 1));
                }
                result.add(map);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            releaseResource(conn, st, rs);
        }
        return result;
    }

    public static List<HashMap<String, String>> getScollData(String sql, int pageno, int pagesize) {
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
//             conn.prepareStatement(sql,游标类型,能否更新记录）；
//             游标类型
//             ResultSet.TYPE FORWORD ONLY 只进游标
//             ResultSet . TYPE SCROLL INSENSITIVE 可滚动。但是不受其他用户对数
//             据库更改的影响
//             ResultSet . TYPE SCROLL SENSITIVE 可滚动。当其他用户更改数据库时这
//             个记录也会改变
//             能否更新记录
//             ResultSet . CONCUR READ ONLY 只读
//             ResultSet . CONCUR UPDATABLE 可更新
            conn = getConnection();
            pstat = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            最大查询的记录条数
            pstat.setMaxRows(pageno * pagesize);
            rs = pstat.executeQuery();
//            将游标移动到第一条记录
            rs.first();
//            游标移动到要输出的第一条记录
            rs.relative((pageno - 1) * pagesize - 1);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    map.put(rsmd.getColumnLabel(i + 1), rs.getString(i + 1));
                }
                result.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseResource(conn, pstat, rs);
        }
        return result;
    }

    /**
     * 重载释放数据库资源
     *
     * @param conn
     * @param stmt
     * @param rs
     */
    public static void releaseResource(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 判断 map 是否包含 key
     * @param map
     * @param key
     * @return
     */
    public static Object getMapElement(Map map, String key) {
        if (map != null){
            for (Iterator ite = map.entrySet().iterator(); ite.hasNext(); ) {
                Map. Entry entry =(Map. Entry) ite. next() ;
                if ((key.trim()) .equals (entry.getKey ()))
                    return entry . getValue();
            }
        }
        return null;
    }
}
