package servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for managing database connections and resources
 * Provides centralized database configuration and connection management
 */
public class DatabaseUtil {
    
    // Database configuration constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vehicle_service_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "2005";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Establishes and returns a database connection
     * @return Connection object or null if connection fails
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER_CLASS);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
    
    /**
     * Safely closes a database connection
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Safely closes a PreparedStatement
     * @param stmt PreparedStatement to close
     */
    public static void closeStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Safely closes a ResultSet
     * @param rs ResultSet to close
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing result set: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
