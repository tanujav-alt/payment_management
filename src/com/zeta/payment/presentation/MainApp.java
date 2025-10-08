package com.zeta.payment.presentation;


import com.zeta.payment.dao.*;

import com.zeta.payment.entity.enums.PaymentStatus;

import com.zeta.payment.entity.*;
//import com.zeta.payment.entity.Payment.PaymentStatus;
import com.zeta.payment.entity.enums.PaymentType;
import com.zeta.payment.entity.enums.PaymentCategory;
import com.zeta.payment.entity.enums.UserRole;


import com.zeta.payment.service.*;
import java.util.*;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // In-memory DAO implementations
//        UserDAO userDAO = new UserDAO() {
//            private Map<String, User> users = new HashMap<>();
//            public boolean addUser(User user) { users.put(user.getUsername(), user); return true; }
//            public User getUserByUsername(String username) { return users.get(username); }
//        };

        UserDAO userDAO = new UserDAO();  // ✅ Use the real DAO class

        PaymentDAO paymentDAO = new PaymentDAO();

//        PaymentDAO paymentDAO = new PaymentDAO() {
//            private List<Payment> payments = new ArrayList<>();
//            public boolean addPayment(Payment payment) { payments.add(payment); return true; }
//            public boolean updatePaymentStatus(String id, String status) {
//                for (Payment p : payments) {
//                    if (p.getTransactionId().equals(id)) {
//                        p.setPaymentStatus(PaymentStatus.valueOf(status));
//                        return true;
//                    }
//                }
//                return false;
//            }
//            public List<Payment> getAllPayments() { return payments; }
//        };

        UserService userService = new UserService(userDAO);
        PaymentService paymentService = new PaymentService(paymentDAO);

        System.out.println("=== Payments Management System ===");

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Username: ");
                    String uname = sc.nextLine();
                    System.out.print("Password: ");
                    String pass = sc.nextLine();
                    System.out.print("Role (ADMIN/FINANCE_MANAGER/VIEWER): ");
                    UserRole role = UserRole.valueOf(sc.nextLine().toUpperCase());
                    userService.registerUser(uname, pass, role);
                }

                case 2 -> {
                    System.out.print("Username: ");
                    String uname = sc.nextLine();
                    System.out.print("Password: ");
                    String pass = sc.nextLine();

                    User user = userService.login(uname, pass);
                    if (user == null) break;

                    if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.FINANCE_MANAGER) {
                        boolean running = true;
                        while (running) {
                            System.out.println("\n1. Add Payment\n2. Update Status\n3. View Payments\n4. Logout");
                            int opt = sc.nextInt(); sc.nextLine();

                            switch (opt) {
                                case 1 -> {
                                    System.out.print("Amount: ");
                                    double amt = sc.nextDouble(); sc.nextLine();
                                    System.out.print("Type (INCOMING/OUTGOING): ");
                                    PaymentType type = PaymentType.valueOf(sc.nextLine().toUpperCase());
                                    System.out.print("Category (SALARY/VENDOR_PAYMENT/CLIENT_INVOICE): ");
                                    PaymentCategory cat = PaymentCategory.valueOf(sc.nextLine().toUpperCase());
                                    paymentService.addPayment(amt, type, cat);
                                }
                                case 2 -> {
                                    System.out.print("Payment ID: ");
                                    String pid = sc.nextLine();
                                    System.out.print("New Status (PENDING/PROCESSING/COMPLETED): ");
                                    PaymentStatus status = PaymentStatus.valueOf(sc.nextLine().toUpperCase());
                                    paymentService.updateStatus(pid, status);
                                }
                                case 3 -> paymentService.viewPayments();
                                case 4 -> running = false;
                            }
                        }
                    } else {
                        paymentService.viewPayments();
                    }
                }

                case 3 -> {
                    System.out.println("Exiting...");
                    return;
                }
            }
        }
    }
}
