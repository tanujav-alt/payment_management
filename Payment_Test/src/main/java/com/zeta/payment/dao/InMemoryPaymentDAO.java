package com.zeta.payment.dao;

import com.zeta.payment.entity.Payment;
import com.zeta.payment.entity.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
    Creating an in-memory DAO implementation that mimics the real PaymentDAO but keeps data in a list.
    InMemoryPaymentDAO simulates DB without actual SQL.
*/
public class InMemoryPaymentDAO extends PaymentDAO {

    private final ConcurrentHashMap<String, Payment> payments = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> auditMap = new ConcurrentHashMap<>();

    @Override
    public boolean addPayment(Payment payment, String performedBy) {
        payments.put(payment.getTransactionId(), payment);
        auditMap.put(payment.getTransactionId(), performedBy);
        return true;
    }

    @Override
    public boolean updatePaymentStatus(String transactionId, PaymentStatus newStatus, String performedBy) {
        Payment p = payments.get(transactionId);
        if (p == null) return false;
        p.setPaymentStatus(newStatus);
        auditMap.put(transactionId, performedBy);
        return true;
    }

    @Override
    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments.values());
    }

    @Override
    public String getPerformedBy(String transactionId) {
        return auditMap.get(transactionId);
    }

    @Override
    public List<Payment> getMonthlyReport(int year, int month) {
        return getAllPayments();
    }

    @Override
    public List<Payment> getQuarterlyReport(int year, int quarter) {
        return getAllPayments();
    }
}
