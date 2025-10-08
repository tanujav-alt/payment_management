//package com.zeta.payment.helper;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.logging.Logger;
//
//public class DbConnector {
//    private static final Logger logger = Logger.getLogger(DbConnector.class.getName());
//    private static final String URL = "jdbc:postgresql://localhost:5432/fintech_payments";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "your_password_here";
//
//    public static Connection getConnection() throws SQLException {
//        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//        connection.setAutoCommit(true);
//        logger.info("Database connected successfully: " + connection.getCatalog());
//        return connection;
//    }
//}

package com.zeta.payment.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.sql.Statement;
import java.sql.ResultSet;


public class DbConnector {
    private static final Logger logger = Logger.getLogger(DbConnector.class.getName());

    //  Update with your actual DB details
    private static final String URL = "jdbc:postgresql://localhost:5432/test_payment";
    private static final String USER = "postgres";
    private static final String PASSWORD = "";

//    public static Connection getConnection() {
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//            conn.setAutoCommit(true); // ✅ ensure every INSERT is committed immediately
//            //logger.info("✅ Database connected successfully.");
//            return conn;
//        } catch (SQLException e) {
//            logger.severe("❌ Database connection failed: " + e.getMessage());
//            return null;
//        }
//    }

    /**
     * Establishes and returns a connection to the PostgreSQL database.
     */
    public static Connection getConnection() {
        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            logger.info("Connecting to DB: " + URL + " as user: " + USER);

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Log which DB we actually connected to (sanity check)
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT current_database()")) {
                if (rs.next()) {
                    logger.info("✅ Connected to actual database: " + rs.getString(1));
                }
            } catch (SQLException e) {
                logger.warning("Could not verify current database: " + e.getMessage());
            }

            // Confirm auto-commit mode
            logger.info("Auto-commit mode: " + conn.getAutoCommit());

            return conn;

        } catch (ClassNotFoundException e) {
            logger.severe("PostgreSQL Driver not found! Add it to your classpath.");
            throw new RuntimeException(e);

        } catch (SQLException e) {
            logger.severe("Database connection failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


//    // ✅ Simple method to test connection manually
//    public static void testConnection() {
//        try (Connection conn = getConnection()) {
//            if (conn != null && !conn.isClosed()) {
//                System.out.println("✅ PostgreSQL connection test: SUCCESS");
//            } else {
//                System.out.println("❌ PostgreSQL connection test: FAILED");
//            }
//        } catch (SQLException e) {
//            System.out.println("❌ Error during connection test: " + e.getMessage());
//        }
//    }
}

