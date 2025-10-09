package com.zeta.payment.service;

import com.zeta.payment.dao.PaymentDAO;
import com.zeta.payment.entity.Payment;
import com.zeta.payment.entity.enums.*;

import java.util.List;
import java.util.logging.Logger;

public class PaymentService {
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
    private final PaymentDAO paymentDAO;

    //constructor accepting the PaymentDAO object
    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    // Add Payment
    public void addPayment(double amount, PaymentType type, PaymentCategory category, String performedBy) {
        String id = "TRANS" + (int)(Math.random() * 100000); // short unique id
        Payment payment = new Payment(id, amount, type, category, PaymentStatus.PENDING);

        if (paymentDAO.addPayment(payment, performedBy)) {
            logger.info("Payment added: " + payment);
        } else {
            logger.warning("Failed to add payment: " + payment);
        }
    }

    // Update Payment Status
    public void updatePaymentStatus(String id, PaymentStatus status, String performedBy) {
        if (paymentDAO.updatePaymentStatus(id, status, performedBy)) {
            logger.info("Payment updated: " + id + " → " + status);
        } else {
            logger.warning("Payment not found: " + id);
        }
    }

    // View All Payments - neat and readable
    public void viewPayments() {
        List<Payment> payments = paymentDAO.getAllPayments();

        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }

        // Header
        System.out.printf("%-12s %-10s %-20s %-10s %-12s %-15s%n",
                "TransactionID", "Type", "Category", "Amount", "Status", "Performed By");
        System.out.println("--------------------------------------------------------------------------------");

        // Data rows
        for (Payment p : payments) {
            String performedBy = paymentDAO.getPerformedBy(p.getTransactionId()); // fetch from DAO
            System.out.printf("%-12s %-10s %-20s %-10.2f %-12s %-15s%n",
                    p.getTransactionId(),
                    p.getPaymentType(),
                    p.getPaymentCategory(),
                    p.getAmount(),
                    p.getPaymentStatus(),
                    performedBy != null ? performedBy : "-");
        }
    }



    public void generateMonthlyReport(int year, int month) {
        List<Payment> payments = paymentDAO.getMonthlyReport(year, month);
        double totalIncoming = 0, totalOutgoing = 0;
        int countIncoming = 0, countOutgoing = 0;

        // Calculate totals
        for (Payment p : payments) {
            if (p.getPaymentType() == PaymentType.INCOMING) {
                totalIncoming += p.getAmount();
                countIncoming++;
            } else {
                totalOutgoing += p.getAmount();
                countOutgoing++;
            }
        }

        System.out.println("=== Monthly Report for " + year + "-" + String.format("%02d", month) + " ===\n");

        // Summary Section
        System.out.println("Summary:");
        System.out.printf("  Total Incoming : %.2f (%d transactions)%n", totalIncoming, countIncoming);
        System.out.printf("  Total Outgoing : %.2f (%d transactions)%n%n", totalOutgoing, countOutgoing);

        // Breakdown by category
        System.out.println("By Category:");
        System.out.printf("%-20s %-15s %-10s%n", "Category", "Total Amount", "Transactions");
        for (PaymentCategory cat : PaymentCategory.values()) {
            double sum = payments.stream()
                    .filter(p -> p.getPaymentCategory() == cat)
                    .mapToDouble(Payment::getAmount)
                    .sum();
            long count = payments.stream()
                    .filter(p -> p.getPaymentCategory() == cat)
                    .count();
            if (count > 0) { // only show categories that exist
                System.out.printf("%-20s %-15.2f %-10d%n", cat, sum, count);
            }
        }

        // Detailed Transactions Section
        System.out.println("\nDetailed Transactions:");
        System.out.printf("%-12s %-10s %-20s %-10s %-12s %-15s %-12s%n",
                "TransactionID", "Type", "Category", "Amount", "Status", "Performed By", "Report Type");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Payment p : payments) {
            String performedBy = paymentDAO.getPerformedBy(p.getTransactionId()); // Fetch from DAO
            System.out.printf("%-12s %-10s %-20s %-10.2f %-12s %-15s %-12s%n",
                    p.getTransactionId(),
                    p.getPaymentType(),
                    p.getPaymentCategory(),
                    p.getAmount(),
                    p.getPaymentStatus(),
                    performedBy != null ? performedBy : "-",
                    "Monthly");
        }
    }


    public void generateQuarterlyReport(int year, int quarter) {
        List<Payment> payments = paymentDAO.getQuarterlyReport(year, quarter);
        double totalIncoming = 0, totalOutgoing = 0;
        int countIncoming = 0, countOutgoing = 0;

        // Calculate totals
        for (Payment p : payments) {
            if (p.getPaymentType() == PaymentType.INCOMING) {
                totalIncoming += p.getAmount();
                countIncoming++;
            } else {
                totalOutgoing += p.getAmount();
                countOutgoing++;
            }
        }

        System.out.println("=== Quarterly Report for " + year + " Q" + quarter + " ===\n");

        // Summary Section
        System.out.println("Summary:");
        System.out.printf("  Total Incoming : %.2f (%d transactions)%n", totalIncoming, countIncoming);
        System.out.printf("  Total Outgoing : %.2f (%d transactions)%n%n", totalOutgoing, countOutgoing);

        // Breakdown by category
        System.out.println("By Category:");
        System.out.printf("%-20s %-15s %-10s%n", "Category", "Total Amount", "Transactions");
        for (PaymentCategory cat : PaymentCategory.values()) {
            double sum = payments.stream()
                    .filter(p -> p.getPaymentCategory() == cat)
                    .mapToDouble(Payment::getAmount)
                    .sum();
            long count = payments.stream()
                    .filter(p -> p.getPaymentCategory() == cat)
                    .count();
            if (count > 0) { // Show only existing categories
                System.out.printf("%-20s %-15.2f %-10d%n", cat, sum, count);
            }
        }

        // Detailed Transactions Section
        System.out.println("\nDetailed Transactions:");
        System.out.printf("%-12s %-10s %-20s %-10s %-12s %-15s %-12s%n",
                "TransactionID", "Type", "Category", "Amount", "Status", "Performed By", "Report Type");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Payment p : payments) {
            String performedBy = paymentDAO.getPerformedBy(p.getTransactionId());
            System.out.printf("%-12s %-10s %-20s %-10.2f %-12s %-15s %-12s%n",
                    p.getTransactionId(),
                    p.getPaymentType(),
                    p.getPaymentCategory(),
                    p.getAmount(),
                    p.getPaymentStatus(),
                    performedBy != null ? performedBy : "-",
                    "Quarterly");
        }
    }

    // View Audit Trail
    public void viewAuditTrail() {
        paymentDAO.printAuditTrail();
    }



}
