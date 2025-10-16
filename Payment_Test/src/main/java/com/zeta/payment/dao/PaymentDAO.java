package com.zeta.payment.dao;

import com.zeta.payment.entity.Payment;
import com.zeta.payment.entity.enums.*;

import com.zeta.payment.entity.enums.PaymentStatus;
import com.zeta.payment.helper.DbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PaymentDAO {
    private static final Logger logger = Logger.getLogger(PaymentDAO.class.getName());

    // Add Payment with Audit
    public boolean addPayment(Payment payment, String performedBy) {
        String sql = "INSERT INTO payments (transaction_id, amount, payment_type, payment_category, payment_status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment.getTransactionId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentType().name());
            stmt.setString(4, payment.getPaymentCategory().name());
            stmt.setString(5, payment.getPaymentStatus().name());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Payment added: " + payment.getTransactionId());

                // Insert audit log
                insertAudit(conn, "ADD_PAYMENT", payment.getTransactionId(),
                        "Added payment of " + payment.getAmount() + " (" + payment.getPaymentType() + ")",
                        performedBy);
                //insertAudit(payment.getTransactionId(), "CREATED", performedBy);


                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error adding payment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    // Update Payment Status with Audit

    public boolean updatePaymentStatus(String transactionId, PaymentStatus newStatus, String performedBy) {
        String sql = "UPDATE payments SET payment_status = ? WHERE transaction_id = ?";

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.name());
            stmt.setString(2, transactionId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Payment status updated: " + transactionId + " → " + newStatus);

                // Insert audit log
                insertAudit(conn, "UPDATE_STATUS", transactionId,
                        "Updated status to " + newStatus,
                        performedBy);

                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error updating payment: " + e.getMessage());
        }
        return false;
    }


    // Insert Audit Log Helper

    private void insertAudit(Connection conn, String action, String transactionId, String details, String performedBy) {
        String sql = "INSERT INTO payment_audit_log (transaction_id, action, details, performed_by) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            ps.setString(2, action);
            ps.setString(3, details);       //  Add details
            ps.setString(4, performedBy);

            ps.executeUpdate();
            logger.info("Audit logged: " + action + " → " + transactionId);
        } catch (SQLException e) {
            logger.severe("Error inserting audit log: " + e.getMessage());
        }
    }


    // Fetch All Payments

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT transaction_id, amount, payment_type, payment_category, payment_status FROM payments";

        try (Connection conn = DbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment p = new Payment(
                        rs.getString("transaction_id"),
                        rs.getDouble("amount"),
                        Enum.valueOf(com.zeta.payment.entity.enums.PaymentType.class, rs.getString("payment_type")),
                        Enum.valueOf(com.zeta.payment.entity.enums.PaymentCategory.class, rs.getString("payment_category")),
                        Enum.valueOf(PaymentStatus.class, rs.getString("payment_status"))
                );
                payments.add(p);
            }

        } catch (SQLException e) {
            logger.severe("Error fetching payments: " + e.getMessage());
        }

        return payments;
    }


    // Monthly Report
    public List<Payment> getMonthlyReport(int year, int month) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT transaction_id, amount, payment_type, payment_category, payment_status " +
                "FROM payments WHERE EXTRACT(YEAR FROM created_at)=? AND EXTRACT(MONTH FROM created_at)=?";

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapRowToPayment(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error generating monthly report: " + e.getMessage());
        }
        return payments;
    }

    // Quarterly Report (1-4)
    public List<Payment> getQuarterlyReport(int year, int quarter) {
        List<Payment> payments = new ArrayList<>();
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = startMonth + 2;
        String sql = "SELECT transaction_id, amount, payment_type, payment_category, payment_status " +
                "FROM payments WHERE EXTRACT(YEAR FROM created_at)=? AND EXTRACT(MONTH FROM created_at) BETWEEN ? AND ?";

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            stmt.setInt(2, startMonth);
            stmt.setInt(3, endMonth);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapRowToPayment(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error generating quarterly report: " + e.getMessage());
        }
        return payments;
    }

    // Map ResultSet row to Payment object
    private Payment mapRowToPayment(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getString("transaction_id"),
                rs.getDouble("amount"),
                Enum.valueOf(PaymentType.class, rs.getString("payment_type")),
                Enum.valueOf(PaymentCategory.class, rs.getString("payment_category")),
                Enum.valueOf(PaymentStatus.class, rs.getString("payment_status"))
        );
    }


    public void printAuditTrail() {
        String sql = "SELECT action, transaction_id, details, performed_by, created_at FROM payment_audit_log ORDER BY created_at DESC";

        try (Connection conn = DbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Header
            System.out.println("=== Audit Trail ===");
            System.out.printf("%-25s %-15s %-12s %-40s %-15s%n",
                    "Timestamp", "Action", "TransactionID", "Details", "Performed By");
            System.out.println("---------------------------------------------------------------------------------------------");

            // Data rows
            while (rs.next()) {
                System.out.printf("%-25s %-15s %-12s %-40s %-15s%n",
                        rs.getString("created_at"),
                        rs.getString("action"),
                        rs.getString("transaction_id"),
                        rs.getString("details"),
                        rs.getString("performed_by"));
            }
        } catch (SQLException e) {
            logger.severe("Error reading audit trail: " + e.getMessage());
        }
    }


    public String getPerformedBy(String transactionId) {
        String performedBy = null;
        String sql = "SELECT performed_by FROM payment_audit_log WHERE transaction_id = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) performedBy = rs.getString("performed_by");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return performedBy;
    }



}