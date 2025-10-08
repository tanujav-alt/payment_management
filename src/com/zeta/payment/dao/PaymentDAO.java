//package com.zeta.payment.dao;
//
//import com.zeta.payment.entity.Payment;
//import com.zeta.payment.entity.enums.PaymentCategory;
//import com.zeta.payment.entity.enums.PaymentStatus;
//import com.zeta.payment.entity.enums.PaymentType;
//import com.zeta.payment.helper.DbConnector;
//import com.zeta.payment.helper.LoggerHelper;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Logger;
//
//public class PaymentDAO {
//    private static final Logger logger = LoggerHelper.getLogger(PaymentDAO.class);
//
//    public void addPayment(Payment payment) {
//        String query = "INSERT INTO payments (transaction_id, amount, payment_type, payment_category, payment_status) VALUES (?, ?, ?, ?, ?)";
//
//        try (Connection conn = DbConnector.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//
//            ps.setString(1, payment.getTransactionId());
//            ps.setDouble(2, payment.getAmount());
//            ps.setString(3, payment.getPaymentType().name());
//            ps.setString(4, payment.getPaymentCategory().name());
//            ps.setString(5, payment.getPaymentStatus().name());
//
//            ps.executeUpdate();
//            logger.info("Payment inserted into database: " + payment.getTransactionId());
//
//        } catch (SQLException e) {
//            logger.severe("Error inserting payment: " + e.getMessage());
//        }
//    }
//
//    public void updatePaymentStatus(String transactionId, String newStatus) {
//        String query = "UPDATE payments SET payment_status = ? WHERE transaction_id = ?";
//
//        try (Connection conn = DbConnector.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//
//            ps.setString(1, newStatus);
//            ps.setString(2, transactionId);
//
//            int rows = ps.executeUpdate();
//            if (rows > 0) {
//                logger.info("Payment status updated for: " + transactionId);
//            } else {
//                logger.warning("No payment found with ID: " + transactionId);
//            }
//
//        } catch (SQLException e) {
//            logger.severe("Error updating payment status: " + e.getMessage());
//        }
//    }
//
//    public List<Payment> getAllPayments() {
//        List<Payment> payments = new ArrayList<>();
//        String query = "SELECT * FROM payments";
//
//        try (Connection conn = DbConnector.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                Payment p = new Payment();
//                p.setTransactionId(rs.getString("transaction_id"));
//                p.setAmount(rs.getDouble("amount"));
//
//                // ✅ Convert strings from DB into enum values
//                p.setPaymentType(PaymentType.valueOf(rs.getString("payment_type")));
//                p.setPaymentCategory(PaymentCategory.valueOf(rs.getString("payment_category")));
//                p.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
//
//                payments.add(p);
//            }
//
//        } catch (SQLException e) {
//            logger.severe("Error fetching payments: " + e.getMessage());
//        }
//
//        return payments;
//    }
//}
package com.zeta.payment.dao;

import com.zeta.payment.entity.Payment;
import com.zeta.payment.helper.DbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//public class PaymentDAO {
//    private static final Logger logger = Logger.getLogger(PaymentDAO.class.getName());
//
//    // Insert payment record
//    public boolean addPayment(Payment payment) {
//        String sql = "INSERT INTO payments (transaction_id, amount, payment_type, payment_category, payment_status) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DbConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            System.out.println(">>> Inserting into DB: " + payment); // DEBUG LINE
//
//            stmt.setString(1, payment.getTransactionId());
//            stmt.setDouble(2, payment.getAmount());
//            stmt.setString(3, payment.getPaymentType().name());
//            stmt.setString(4, payment.getPaymentCategory().name());
//            stmt.setString(5, payment.getPaymentStatus().name());
//
//            int rows = stmt.executeUpdate();
//            if (rows > 0) {
//                logger.info("Payment inserted into DB: " + payment.getTransactionId());
//                return true;
//            }
//        } catch (SQLException e) {
//            logger.severe("Error adding payment: " + e.getMessage());
//        }
//        return false;
//    }
//
//    // Update payment status
//    public boolean updatePaymentStatus(String transactionId, String newStatus) {
//        String sql = "UPDATE payments SET payment_status = ? WHERE transaction_id = ?";
//        try (Connection conn = DbConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, newStatus);
//            stmt.setString(2, transactionId);
//            int rows = stmt.executeUpdate();
//
//            if (rows > 0) {
//                logger.info("Updated payment status for " + transactionId + " → " + newStatus);
//                return true;
//            }
//        } catch (SQLException e) {
//            logger.severe(" Error updating payment: " + e.getMessage());
//        }
//        return false;
//    }
//
//    // Fetch all payments
//    public List<Payment> getAllPayments() {
//        List<Payment> payments = new ArrayList<>();
//        String sql = "SELECT * FROM payments";
//        try (Connection conn = DbConnector.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                Payment p = new Payment();
//                p.setTransactionId(rs.getString("transaction_id"));
//                p.setAmount(rs.getDouble("amount"));
//                p.setPaymentType(Enum.valueOf(com.zeta.payment.entity.enums.PaymentType.class, rs.getString("payment_type")));
//                p.setPaymentCategory(Enum.valueOf(com.zeta.payment.entity.enums.PaymentCategory.class, rs.getString("payment_category")));
//                p.setPaymentStatus(Enum.valueOf(com.zeta.payment.entity.enums.PaymentStatus.class, rs.getString("payment_status")));
//                payments.add(p);
//            }
//        } catch (SQLException e) {
//            logger.severe(" Error fetching payments: " + e.getMessage());
//        }
//        return payments;
//    }
//}
//package com.zeta.payment.dao;

import com.zeta.payment.helper.DbConnector;
import com.zeta.payment.entity.Payment;
import com.zeta.payment.entity.enums.PaymentStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PaymentDAO {
    private static final Logger logger = Logger.getLogger(PaymentDAO.class.getName());

    // ✅ Insert Payment
    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payments (transaction_id, amount, payment_type, payment_category, payment_status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment.getTransactionId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentType().name());
            stmt.setString(4, payment.getPaymentCategory().name());
            stmt.setString(5, payment.getPaymentStatus().name());

            int rows = stmt.executeUpdate();
            logger.info("Rows inserted: " + rows);


            if (rows > 0) {
                logger.info(" Payment inserted into DB: " + payment.getTransactionId());
                return true;
            } else {
                logger.warning("️ Payment insertion returned 0 rows.");
                return false;
            }

        } catch (SQLException e) {
            logger.severe(" Error inserting payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //  Update Payment Status
    public boolean updatePaymentStatus(String transactionId, String newStatus) {
        String sql = "UPDATE payments SET payment_status = ? WHERE transaction_id = ?";

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, transactionId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.severe(" Error updating payment status: " + e.getMessage());
            return false;
        }
    }

    //  Fetch all payments
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
            logger.severe(" Error fetching payments: " + e.getMessage());
        }

        return payments;
    }
}
