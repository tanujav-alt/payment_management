package com.zeta.payment.dao;


import com.zeta.payment.helper.DbConnector;

import java.sql.*;
import java.util.logging.*;

public class AuditDAO {
    private static final Logger logger = Logger.getLogger(AuditDAO.class.getName());


    //Method to print the audit trail
    public void printAuditTrail() {
        String sql = "SELECT * FROM audit_log ORDER BY timestamp DESC"; //sql query
        try (Connection conn = DbConnector.getConnection(); // to start the db connection
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))  {
            //query result will be stored in ResultSet
            System.out.println("\n==== AUDIT TRAIL ====");
            while (rs.next()) {
                System.out.printf("[%s] %s by %s for %s%n",
                        rs.getTimestamp("timestamp"),
                        rs.getString("action"),
                        rs.getString("performed_by"),
                        rs.getString("transaction_id"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error reading audit log", e);
        }
    }
}