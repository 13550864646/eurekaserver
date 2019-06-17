package com.cloud.mina.util;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashMap;

/**
 * 设备 id 处理工具类
 */
public class DeviceIDResolver {
    private static Logger log = Logger.getLogger(DeviceIDResolver.class);

    /**
     * 通过用户的设备号获取用户的孚机号
     *
     * @param device_id
     * @return
     */
    public static String searchPatientidByDeviceid(String device_id) {
        String patientID = "";
        //查询
        Connection conn = null;
        PreparedStatement pst = null;
        String sql = "SELECT patientID FROM usertbl WHERE deviceID=? and deviceUseFlag='l '";
        ResultSet rs = null;
        try {
            conn = C3P0Util.getConnection();
//          查询数据
            pst = conn.prepareStatement(sql);
            pst.setString(1, device_id);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                patientID = rs.getString("patientID");
            }

        } catch (Exception e) {
            log.error("数据库查询异常 获取 patientID 失败！！！" + e.getMessage());
        } finally {
            C3P0Util.releaseResource(conn, pst, rs);
        }
        return patientID;
    }

    /**
     * ＊通过用户的设备号和设备类型获取用户的手机号（目前不区分设备类型）
     *
     * @param deviceID
     * @param deviceType
     * @return
     */
    public static String searchPatientIDByDeviceidAndDeviceType(String deviceID, String deviceType) {
        String patientID = "";
        //查询
        Connection conn = null;
        PreparedStatement pst = null;
        String sql = "SELECT patientID FROM usertbl WHERE deviceID=? and deviceUseFlag= '1'";
        ResultSet rs = null;
        try {
            conn = C3P0Util.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, deviceID);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                patientID = rs.getString("patientI D");
            }
        } catch (Exception e) {
            log.error("数据库查询异常 获取 patientID 失败 ! !");
            log.error(e.getMessage());
        } finally {
            C3P0Util.releaseResource(conn, pst, rs);
        }
        return patientID;
    }

    /**
     * 通过用户的设备号获取对应用户信息（目前不区分设备类型）
     *
     * @param deviceID
     * @return
     */
    public static HashMap<String, String> searchPatientinfoByDeviceid(String deviceID) {
        HashMap<String, String> map = new HashMap<String, String>();
//        查询
        Connection conn = null;
        PreparedStatement pst = null;
        String sql = "SELECT * FROM usertbl WHERE deviceID=? and deviceUseFlag='l'";
        ResultSet rs = null;
        try {
            conn = C3P0Util.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, deviceID);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    map.put(rsmd.getColumnLabel(i + 1), rs.getString(i + 1));
                }
            }
        } catch (Exception e) {
            log.error("数据库查询异常，获取 patientlnfo 失败！！！");
            log.error(e.getMessage());
        } finally {
            C3P0Util.releaseResource(conn, pst, rs);
        }
        return map;
    }

    /**
     * 通过用户的设备号和设备类型获取用户的单位代号
     *
     * @param deviceID
     * @param deviceType
     * @return
     */
    public static String searchCompanyByDeviceidAndDeviceType(String deviceID, String deviceType) {
        String company = "";
//        查询
        Connection conn = null;
        PreparedStatement pst = null;
        String sql = "SELECT company FROM usertbl WHERE deviceID=? and deviceType=? and deviceUseFlag='1' ";
        ResultSet rs = null;
        try {
            conn = C3P0Util.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, deviceID);
            pst.setString(2, deviceType);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                company = rs.getString("company");
            }
        } catch (Exception e) {
            log.error("数据库查询异常 获取 company 失败！！！");
            log.error(e.getMessage());
        } finally {
            C3P0Util.releaseResource(conn, pst, rs);
        }
        return company;
    }

    /**
     * ＊通过用户的设备号判断用户所在单位
     *
     * @param device_id
     * @return
     */
    public static String searchCompanyByDeviceid(String device_id) {
        String company = "";
        if (device_id != null) {
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null;
            try {
                conn = C3P0Util.getConnection();
//                查询数据
                st = conn.createStatement();
                String query = "SELECT company FROM usertbl WHERE deviceUseFlag='1' and deviceID = '" + device_id + "'";
                rs = st.executeQuery(query);
                if (rs != null && rs.next()) {
                    company = rs.getString("company");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                C3P0Util.releaseResource(conn, st, rs);
            }
        }
        return company;
    }

    /**
     * ＊从字节数组中解析出设备号，忽略 null 、空格、因车、换行 null,ASCI 工码。换
     * 行， ASCII 码10 回车 ASCII 码13
     * ＊空格， ASCII 码32
     *
     * @param b
     * @param deviceIDBeginIndex
     * @return
     */
    public static String getDeviceIDFromBytes(byte[] b, int deviceIDBeginIndex) {
        StringBuffer sbid = new StringBuffer();
        for (int j = deviceIDBeginIndex; j < deviceIDBeginIndex + 16; j++) {
//            空格和回车忽略
            if (b[j] != 0 && b[j] != 13 && b[j] != 10 && b[j] != 32) {
                sbid.append((char) b[j]);
            }
        }
        log.info("The deviceID is" + sbid.toString());
        return sbid.toString();
    }

    /**
     * 从字节数组中解析出设备号，忽略 null 、空格、回车、换行 null,ASCII 换行，
     * ASCII 码10 回车， ASCII码 13
     * ＊空格， ASCII 码32
     *
     * @param b
     * @param passwordBeginIndex
     * @return
     */
    public static String getPasswordFromBytes(byte[] b, int passwordBeginIndex) {
        StringBuffer sbid = new StringBuffer();
        for (int j = passwordBeginIndex; j < passwordBeginIndex + 16; j++) {
//        空格和回车忽略
            if (b[j] != 0 && b[j] != 13 && b[j] != 10 && b[j] != 32) {
                sbid.append((char) b[j]);
            }
        }
        log.info("The password is" + sbid.toString());
        return sbid.toString();
    }

    public static String getDeviceIDPrefix(byte[] b, int prefixBeginIndex) {
        StringBuffer prefix = new StringBuffer();
        for (int i = prefixBeginIndex; i < (prefixBeginIndex + 5); i++) {
//            空格忽略
            if (b[i] != 0) {
                prefix.append((char) b[i]);
            }
        }
        log.info("The deviceID prefix is " + prefix.toString());
        return prefix.toString();
    }

    /**
     * ＊从字节数组中解析出固件版本
     *
     * @param b
     * @param versionBeginindex
     * @return
     */
    public static String getFirmwareVersion(byte[] b, int versionBeginindex) {
        StringBuffer version = new StringBuffer();
        for (int i = versionBeginindex; i < (versionBeginindex + 4); i++) {
            version.append(b[i]);
        }
        log.info("The firmware version is " + version.toString());
        return version.toString();
    }

    /**
     * add by RCM on 2014/02/13 获取 8号包方式二的设备号前缀
     *
     * @param b
     * @return
     */
    public static String getNo8PackageDevicePrefix(byte[] b) {
        byte length[] = new byte[4];
        length[0] = b[4];
        length[1] = b[5];
        length[2] = b[6];
        length[3] = b[7];
        long lengthvalue = DataTypeChangeHelper.unsigned4BytesToInt(length, 0);
        return getDeviceIDPrefix(b, (int) (lengthvalue - 23));
    }

    /**
     * ＊获取用户appType
     *
     * @param deviceid
     * @return
     */
    public static String searchAppTypeByDeviced(String deviceid) {
        String appType = "";
//        查询
        Connection conn = null;
        PreparedStatement pst = null;
        String sql = "SELECT appType FROM usertbl WHERE deviceID=? and deviceUseFlag='1'";
        ResultSet rs = null;
        try {
            conn = C3P0Util.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, deviceid);
//            pst .setString(2, deviceType);
            rs = pst.executeQuery();
            if (rs != null && rs.next()) {
                appType = rs.getString("appType");
            }
        } catch (Exception e) {
            log.error("数据库查询异常，获取 pp Type 失败 ！！");
            log.error(e.getMessage());
        } finally {
            C3P0Util.releaseResource(conn, pst, rs);
        }
        return appType;
    }
}
