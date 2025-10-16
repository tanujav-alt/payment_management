package com.zeta.payment.service;

import com.zeta.payment.dao.PaymentDAO;
import com.zeta.payment.entity.FinancialReport;

// ReportingService handles high-level background report generation.
// It delegates work to PaymentService which uses its ExecutorService.

public class ReportingService {
    private final PaymentService paymentService;

    //Instantiates a PaymentService with a PaymentDAO.
    public ReportingService() {
        this.paymentService = new PaymentService(new PaymentDAO());
    }

    //for monthly report
    public FinancialReport monthly(int year, int month) {
        paymentService.generateMonthlyReport(year, month);
        return null;
    }

    //for quarterly report
    public FinancialReport quarterly(int year, int quarter) {
        paymentService.generateQuarterlyReport(year, quarter);
        return null;
    }

    public void shutdown() {
        paymentService.shutdownExecutor();
    }
}