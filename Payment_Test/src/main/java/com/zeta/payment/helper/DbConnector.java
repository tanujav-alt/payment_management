
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

    //  Update with actual DB details
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
}