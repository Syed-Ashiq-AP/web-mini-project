package servlets;

import java.sql.*;

public class DatabaseUtil {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/covid_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "2005";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER_CLASS);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
