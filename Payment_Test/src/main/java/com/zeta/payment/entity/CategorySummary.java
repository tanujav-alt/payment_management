package com.zeta.payment.entity;

import java.math.BigDecimal;

public class CategorySummary {
    private String category;
    private BigDecimal totalAmount;
    private int count;

    //constructor to initialize the values
    public CategorySummary(String category, BigDecimal totalAmount, int count) {
        this.category = category;
        this.totalAmount = totalAmount;
        this.count = count;
    }
    // getters
    public String getCategory() { return category; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public int getCount() { return count; }

    @Override
    public String toString() {
        return category + " -> count=" + count + ", total=" + totalAmount;
    }
}