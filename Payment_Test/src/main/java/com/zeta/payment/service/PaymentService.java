package com.zeta.payment.service;

import com.zeta.payment.dao.PaymentDAO;
import com.zeta.payment.entity.Payment;
import com.zeta.payment.entity.enums.*;
import com.zeta.payment.helper.IdGenerator;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class PaymentService {
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
    private final PaymentDAO paymentDAO;

    // Lock for thread-safe payment updates
    private final ReentrantLock lock = new ReentrantLock();

    // Executor for background report generation & concurrent payment tasks
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    /**
     * Adds a new payment safely using explicit locking.
     * This prevents race conditions when multiple threads try to insert payments.
     */
    public void addPayment(double amount, PaymentType type, PaymentCategory category, String performedBy) {
        executor.submit(() -> {
            lock.lock();
            try {
                String id = IdGenerator.generateTransactionId();
                Payment payment = new Payment(id, amount, type, category, PaymentStatus.PENDING);
                boolean success = paymentDAO.addPayment(payment, performedBy);
                if (success)
                    logger.info("Payment added: " + payment);
                else
                    logger.warning("Failed to add payment: " + payment);
            } finally {
                lock.unlock();
            }
        });
    }

    /**
     * Updates payment status safely across threads.
     */
    public void updatePaymentStatus(String id, PaymentStatus status, String performedBy) {
        executor.submit(() -> {
            lock.lock();
            try {
                boolean success = paymentDAO.updatePaymentStatus(id, status, performedBy);
                if (success)
                    logger.info(() -> Thread.currentThread().getName() + " updating payment " + id + " → " + status);
                else
                    logger.warning(" Payment not found: " + id);
            } finally {
                lock.unlock();
            }
        });
    }

    /**
     * Simple read operation — no synchronization needed.
     */
    public void viewPayments() {
        List<Payment> payments = paymentDAO.getAllPayments();
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }

        System.out.printf("%-12s %-10s %-20s %-10s %-12s %-15s%n",
                "TransactionID", "Type", "Category", "Amount", "Status", "Performed By");
        System.out.println("--------------------------------------------------------------------------------");

        for (Payment p : payments) {
            String performedBy = paymentDAO.getPerformedBy(p.getTransactionId());
            System.out.printf("%-12s %-10s %-20s %-10.2f %-12s %-15s%n",
                    p.getTransactionId(),
                    p.getPaymentType(),
                    p.getPaymentCategory(),
                    p.getAmount(),
                    p.getPaymentStatus(),
                    performedBy != null ? performedBy : "-");
        }
    }

    /**
     * Generates a monthly report asynchronously in background.
     */
    public void generateMonthlyReport(int year, int month) {
        executor.submit(() -> {
            List<Payment> payments = paymentDAO.getMonthlyReport(year, month);
            printReport(payments, "Monthly", year, month, 0);
        });
    }

    /**
     * Generates a quarterly report asynchronously in background.
     */
    public void generateQuarterlyReport(int year, int quarter) {
        executor.submit(() -> {
            List<Payment> payments = paymentDAO.getQuarterlyReport(year, quarter);
            printReport(payments, "Quarterly", year, 0, quarter);
        });
    }

    /**
     * Prints report details to console.
     */
    private void printReport(List<Payment> payments, String type, int year, int month, int quarter) {
        double totalIncoming = 0, totalOutgoing = 0;
        int countIncoming = 0, countOutgoing = 0;

        for (Payment p : payments) {
            if (p.getPaymentType() == PaymentType.INCOMING) {
                totalIncoming += p.getAmount();
                countIncoming++;
            } else {
                totalOutgoing += p.getAmount();
                countOutgoing++;
            }
        }

        System.out.println("\n=== " + type + " Report ===");
        if (type.equals("Monthly"))
            System.out.println("Year-Month: " + year + "-" + String.format("%02d", month));
        else
            System.out.println("Year-Quarter: " + year + " Q" + quarter);

        System.out.printf("Total Incoming: %.2f (%d transactions)%n", totalIncoming, countIncoming);
        System.out.printf("Total Outgoing: %.2f (%d transactions)%n", totalOutgoing, countOutgoing);

        System.out.println("\nDetailed Transactions:");
        System.out.printf("%-12s %-10s %-20s %-10s %-12s %-15s%n",
                "TransactionID", "Type", "Category", "Amount", "Status", "Performed By");
        System.out.println("--------------------------------------------------------------------------------");

        for (Payment p : payments) {
            String performedBy = paymentDAO.getPerformedBy(p.getTransactionId());
            System.out.printf("%-12s %-10s %-20s %-10.2f %-12s %-15s%n",
                    p.getTransactionId(),
                    p.getPaymentType(),
                    p.getPaymentCategory(),
                    p.getAmount(),
                    p.getPaymentStatus(),
                    performedBy != null ? performedBy : "-");
        }
    }

    /**
     * Prints audit logs.
     */
    public void viewAuditTrail() {
        paymentDAO.printAuditTrail();
    }

    /**
     * Gracefully shuts down all background threads.
     */
    public void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
