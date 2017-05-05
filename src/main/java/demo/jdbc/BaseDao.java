package demo.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {

    protected Connection con = null;
    protected PreparedStatement pre = null;
    protected ResultSet res = null;

    /**
     * 初始化数据库连接
     */
    protected void connect() {
        InputStream is = this.getClass().getResourceAsStream("mysql.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String driverClassName = properties.getProperty("driverClassName");
        String url = properties.getProperty("url");
        try {
            Class.forName(driverClassName);
            con = DriverManager.getConnection(url, properties);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库连接
     */
    protected void close() {
        try {
            if (con != null)
                con.close();
            if (pre != null)
                pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
