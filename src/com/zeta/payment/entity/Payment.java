package com.zeta.payment.entity;

import com.zeta.payment.entity.enums.PaymentCategory;
import com.zeta.payment.entity.enums.PaymentStatus;
import com.zeta.payment.entity.enums.PaymentType;

public class Payment {
    private String transactionId;
    private double amount;
    private PaymentType paymentType;
    private PaymentCategory paymentCategory;
    private PaymentStatus paymentStatus;

    public Payment() {}


    //Constructor to initialize the values
    public Payment(String transactionId, double amount, PaymentType paymentType,
                   PaymentCategory paymentCategory, PaymentStatus paymentStatus) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paymentCategory = paymentCategory;
        this.paymentStatus = paymentStatus;
    }

    //Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentType getPaymentType() { return paymentType; }
    public void setPaymentType(PaymentType paymentType) { this.paymentType = paymentType; }

    public PaymentCategory getPaymentCategory() { return paymentCategory; }
    public void setPaymentCategory(PaymentCategory paymentCategory) { this.paymentCategory = paymentCategory; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    @Override
    public String toString() {
        return "Payment{" +
                "transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", paymentType=" + paymentType +
                ", paymentCategory=" + paymentCategory +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
