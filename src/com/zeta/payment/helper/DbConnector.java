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
    private static Connection connection = null;

    //  Update with your actual DB details
    private static final String URL = "jdbc:postgresql://localhost:5432/test_payment"; //name of the db is test_payment
    private static final String USER = "postgres";
    private static final String PASSWORD = "";


    // Get a single shared connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.info("Connecting to DB: " + URL + " as user: " + USER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info(" Connected to actual database: test_payment");
            connection.setAutoCommit(true);
        }
        return connection;
    }

    // Close the shared connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("DB connection closed.");
            } catch (SQLException e) {
                logger.severe("Error closing connection: " + e.getMessage());
            }
        }
        connection = null;
    }


//    //  Simple method to test connection manually
//    public static void testConnection() {
//        try (Connection conn = getConnection()) {
//            if (conn != null && !conn.isClosed()) {
//                System.out.println(" PostgreSQL connection test: SUCCESS");
//            } else {
//                System.out.println("PostgreSQL connection test: FAILED");
//            }
//        } catch (SQLException e) {
//            System.out.println(" Error during connection test: " + e.getMessage());
//        }
//    }
}

