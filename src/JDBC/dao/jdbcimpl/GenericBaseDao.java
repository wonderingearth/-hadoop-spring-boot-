package com.test.group_project.JDBC.dao.jdbcimpl;

import java.sql.*;
import com.test.group_project.JDBC.Utils.DBUtil;
public class GenericBaseDao {
    private static String driver = DBUtil.driver;
    private static String url = DBUtil.url;
    private static String user = DBUtil.user;
    private static String password = DBUtil.password;
    static{
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected Connection conn;
    protected PreparedStatement pstmt;
    protected ResultSet rs;
    protected int result = -1;

    public void getConnection() throws SQLException {
        conn = DriverManager.getConnection(url, user, password);
    }

    public void closeAll() throws SQLException {
        if (rs!=null && !rs.isClosed())
            rs.close();;
        if (pstmt!=null && !pstmt.isClosed())
            pstmt.close();
        if (conn!=null && !conn.isClosed())
            conn.close();
        if (result != -1)
            result = -1;
    }
    public void executeUpdate(final String sql, final Object... params) throws SQLException {
        pstmt = conn.prepareStatement(sql);
        if (params != null){
            for (int i = 0; i < params.length; i++)
                pstmt.setObject(i+1, params[i]);
        }
        result = pstmt.executeUpdate();
    }

    public void executeQuery(final String sql, final Object... params) throws SQLException {
        pstmt = conn.prepareStatement(sql);
        if (params != null){
            for (int i = 0; i < params.length; i++)
                pstmt.setObject(i+1, params[i]);
        }
        rs = pstmt.executeQuery();
    }
}

