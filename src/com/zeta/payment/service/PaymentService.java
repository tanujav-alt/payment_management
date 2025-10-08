//package com.zeta.payment.service;
////
////import com.zeta.payment.dao.PaymentDAO;
////import com.zeta.payment.entity.Payment;
////import com.zeta.payment.entity.enums.PaymentCategory;
////import com.zeta.payment.entity.enums.PaymentType;
////import com.zeta.payment.helper.IdGenerator;
////
////import java.util.logging.Logger;
////
////public class PaymentService {
////    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
////    private final PaymentDAO dao = new PaymentDAO();
////
////
////    //Add a new Payment Record
////    public void addPayment(double amount, PaymentType paymentType, PaymentCategory paymentCategory){
////        String id = IdGenerator.generateTransactionId();
////        Payment payment = new Payment(id, amount, paymentType, paymentCategory);
////        dao.save(payment);
////    }
////}
//
//import com.zeta.payment.dao.PaymentDAO;
//import com.zeta.payment.entity.Payment;
//import com.zeta.payment.entity.enums.*;
//import com.zeta.payment.helper.IdGenerator;
//import com.zeta.payment.helper.LoggerHelper;
//
//import java.util.List;
//import java.util.logging.Logger;
//
//public class PaymentService {
//    private static final Logger logger = LoggerHelper.getLogger(PaymentService.class);
//    private final PaymentDAO paymentDAO;
//
//    public PaymentService(PaymentDAO paymentDAO) {
//        this.paymentDAO = paymentDAO;
//    }
//
//    public void addPayment(double amount, PaymentType type, PaymentCategory category) {
//        String id = IdGenerator.generateTransactionId();
//        Payment payment = new Payment(id, amount, type, category, PaymentStatus.PENDING);
//        paymentDAO.addPayment(payment);
//        logger.info("Payment added: " + payment);
//    }
//
//    public void updateStatus(String paymentId, PaymentStatus status) {
//        paymentDAO.updatePaymentStatus(paymentId, status.name());
//        logger.info("Updated payment " + paymentId + " to " + status);
//    }
//
//    public void viewPayments() {
//        List<Payment> payments = paymentDAO.getAllPayments();
//        payments.forEach(System.out::println);
//    }
//}
package com.zeta.payment.service;

import com.zeta.payment.dao.PaymentDAO;
import com.zeta.payment.entity.Payment;
import com.zeta.payment.entity.enums.*;
import com.zeta.payment.helper.IdGenerator;
import com.zeta.payment.helper.LoggerHelper;

import java.util.List;
import java.util.logging.Logger;

public class PaymentService {
    private static final Logger logger = LoggerHelper.getLogger(PaymentService.class);
    private final PaymentDAO paymentDAO;

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public void addPayment(double amount, PaymentType type, PaymentCategory category) {
        String id = IdGenerator.generateTransactionId();
        Payment payment = new Payment(id, amount, type, category, PaymentStatus.PENDING);
        boolean isAdded = paymentDAO.addPayment(payment);
        if (isAdded)
            logger.info("Payment added: " + payment);
        else
            logger.warning("Failed to add payment: " + payment);
    }

    public void updateStatus(String paymentId, PaymentStatus status) {
        logger.info("Attempting to update payment: " + paymentId + " -> " + status);
        boolean success = paymentDAO.updatePaymentStatus(paymentId, status.name());
        if (success) logger.info("Updated payment " + paymentId + " → " + status);
        else logger.warning("Payment ID not found: " + paymentId);
    }

    public void viewPayments() {
        List<Payment> payments = paymentDAO.getAllPayments();
        if (payments.isEmpty()) System.out.println("No payments found.");
        else payments.forEach(System.out::println);
    }
}
