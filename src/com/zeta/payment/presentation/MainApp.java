
package com.zeta.payment.presentation;

import com.zeta.payment.dao.PaymentDAO;
import com.zeta.payment.dao.UserDAO;
import com.zeta.payment.entity.Payment;
import com.zeta.payment.entity.User;
import com.zeta.payment.entity.enums.*;
import com.zeta.payment.service.PaymentService;
import com.zeta.payment.service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp {
    private static final Logger logger = Logger.getLogger(MainApp.class.getName());

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Logger.getLogger("com.zeta.payment.helper.DbConnector").setLevel(Level.SEVERE);

        PaymentDAO paymentDAO = new PaymentDAO();
        UserDAO userDAO = new UserDAO();

        PaymentService paymentService = new PaymentService(paymentDAO);
        UserService userService = new UserService(userDAO);

        while (true) {
            System.out.println("\n=== Welcome to Payments Management System ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> register(userService);
                case 2 -> login(userService, paymentService);
                case 3 -> {
                    System.out.println("Thank you for using Payments Management System");
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    //To register a new user
    private static void register(UserService userService) {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        System.out.print("Role (ADMIN/FINANCE_MANAGER/VIEWER): ");
        UserRole role = UserRole.valueOf(sc.nextLine().toUpperCase());

        if (userService.registerUser(username, password, role)) {
            logger.info("User registered successfully: " + username);
        } else {
            logger.warning("Failed to register user: " + username);
        }
    }

    //to login to the payment management app
    private static void login(UserService userService, PaymentService paymentService) {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!userService.login(username, password)) {
            System.out.println("Invalid credentials!");
            return;
        }

        UserRole role = userService.getUserRole(username);
        logger.info("Login successful for user: " + username);

        switch (role) {
            case ADMIN -> adminMenu(userService, paymentService);
            case FINANCE_MANAGER -> managerMenu(paymentService);
            case VIEWER -> viewerMenu(paymentService);
            default -> System.out.println("Unknown role!");
        }
    }

    // To display the admin menu
    private static void adminMenu(UserService userService, PaymentService paymentService) {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Payment");
            System.out.println("2. Update Status");
            System.out.println("3. View Payments");
            System.out.println("4. Monthly Report");
            System.out.println("5. Quarterly Report");
            System.out.println("6. View Audit Trail");
            System.out.println("7. Add User");
            System.out.println("8. Remove User");
            System.out.println("9. View All Users");
            System.out.println("10. Logout");
            System.out.print("Choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> addPayment(paymentService);
                case "2" -> updatePaymentStatus(paymentService);
                case "3" -> paymentService.viewPayments();
                case "4" -> generateMonthlyReport(paymentService);
                case "5" -> generateQuarterlyReport(paymentService);
                case "6" -> paymentService.viewAuditTrail();
                case "7" -> addUser(userService);
                case "8" -> removeUser(userService);
                case "9" -> viewUsers(userService);
                case "10" -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    //To add a user
    private static void addUser(UserService userService) {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        System.out.print("Role (FINANCE_MANAGER/VIEWER): ");
        UserRole role = UserRole.valueOf(sc.nextLine().toUpperCase());

        if (userService.registerUser(username, password, role)) {
            logger.info("User added: " + username);
        } else {
            logger.warning("Failed to add user: " + username);
        }
    }

    //to remove a user that accepts UserService object
    private static void removeUser(UserService userService) {
        System.out.print("Username to remove: ");
        String username = sc.nextLine();

        if (userService.removeUser(username)) {
            logger.info("User removed: " + username);
        } else {
            logger.warning("Failed to remove user: " + username);
        }
    }

    //to view the user that accepts UserService object
    private static void viewUsers(UserService userService) {
        List<User> users = userService.getAllUsers();
        System.out.println("=== Users List ===");
        for (User u : users) {
            System.out.println(u.getUsername() + " | " + u.getRole());
        }
    }

    //to display the manager menu
    private static void managerMenu(PaymentService paymentService) {
        while (true) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. Add Payment");
            System.out.println("2. Update Status");
            System.out.println("3. View Payments");
            System.out.println("4. Monthly Report");
            System.out.println("5. Quarterly Report");
            System.out.println("6. View Audit Trail");
            System.out.println("7. Logout");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addPayment(paymentService);
                case 2 -> updatePaymentStatus(paymentService);
                case 3 -> paymentService.viewPayments();
                case 4 -> generateMonthlyReport(paymentService);
                case 5 -> generateQuarterlyReport(paymentService);
                case 6 -> paymentService.viewAuditTrail();
                case 7 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // To display the viewer menu
    private static void viewerMenu(PaymentService paymentService) {
        while (true) {
            System.out.println("\n=== Viewer Menu ===");
            System.out.println("1. View Payments");
            System.out.println("2. Monthly Report");
            System.out.println("3. Quarterly Report");
            System.out.println("4. Logout");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> paymentService.viewPayments();
                case 2 -> generateMonthlyReport(paymentService);
                case 3 -> generateQuarterlyReport(paymentService);
                case 4 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    //To add the payment that accepts PaymentService object
    private static void addPayment(PaymentService paymentService) {
        System.out.print("Amount: ");
        double amount = Double.parseDouble(sc.nextLine());

        System.out.print("Type (INCOMING/OUTGOING): ");
        PaymentType type = PaymentType.valueOf(sc.nextLine().toUpperCase());

        System.out.print("Category (SALARY/VENDOR_PAYMENT/CLIENT_INVOICE): ");
        PaymentCategory category = PaymentCategory.valueOf(sc.nextLine().toUpperCase());

        System.out.print("Performed by: ");
        String performedBy = sc.nextLine();

        paymentService.addPayment(amount, type, category, performedBy);
    }

    //To update the payment that accepts PaymentService object
    private static void updatePaymentStatus(PaymentService paymentService) {
        System.out.print("Enter Transaction ID: ");
        String id = sc.nextLine();

        System.out.print("Enter new status (PENDING/PROCESSING/COMPLETED): ");
        PaymentStatus status = PaymentStatus.valueOf(sc.nextLine().toUpperCase());

        System.out.print("Performed by: ");
        String performedBy = sc.nextLine();

        paymentService.updatePaymentStatus(id, status, performedBy);
    }

    //to generate monthly report that accepts PaymentService object
    private static void generateMonthlyReport(PaymentService paymentService) {
        System.out.print("Year (e.g. 2025): ");
        int year = Integer.parseInt(sc.nextLine());
        System.out.print("Month (1–12): ");
        int month = Integer.parseInt(sc.nextLine());

        paymentService.generateMonthlyReport(year, month);
    }

    //to generate quarterly report that accepts PaymentService object
    private static void generateQuarterlyReport(PaymentService paymentService) {
        System.out.print("Year (e.g. 2025): ");
        int year = Integer.parseInt(sc.nextLine());
        System.out.print("Quarter (1–4): ");
        int quarter = Integer.parseInt(sc.nextLine());

        paymentService.generateQuarterlyReport(year, quarter);
    }

}


