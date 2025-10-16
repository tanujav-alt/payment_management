package com.zeta.payment.entity;

import java.math.BigDecimal;
import java.util.List;

public class FinancialReport {
    private String periodLabel; // e.g., "2025-10" or "Q1-2025"
    private BigDecimal totalIncoming;
    private BigDecimal totalOutgoing;
    private int countIncoming;
    private int countOutgoing;
    private List<CategorySummary> byCategory;

    //constructor to initialize the values
    public FinancialReport(String periodLabel, BigDecimal totalIncoming, BigDecimal totalOutgoing,
                           int countIncoming, int countOutgoing, List<CategorySummary> byCategory) {
        this.periodLabel = periodLabel;
        this.totalIncoming = totalIncoming;
        this.totalOutgoing = totalOutgoing;
        this.countIncoming = countIncoming;
        this.countOutgoing = countOutgoing;
        this.byCategory = byCategory;
    }

    // getters and toString()
    public String getPeriodLabel() { return periodLabel; }
    public BigDecimal getTotalIncoming() { return totalIncoming; }
    public BigDecimal getTotalOutgoing() { return totalOutgoing; }
    public int getCountIncoming() { return countIncoming; }
    public int getCountOutgoing() { return countOutgoing; }
    public List<CategorySummary> getByCategory() { return byCategory; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Report for ").append(periodLabel).append("\n");
        sb.append("Incoming: ").append(totalIncoming).append(" (").append(countIncoming).append(")\n");
        sb.append("Outgoing: ").append(totalOutgoing).append(" (").append(countOutgoing).append(")\n");
        sb.append("By Category:\n");
        for (CategorySummary cs : byCategory) sb.append("  ").append(cs).append("\n");
        return sb.toString();
    }
}