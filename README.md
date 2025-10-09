# Payment Management System


## Overview

The **Payments Management System** is a secure, role-based internal platform designed for a fintech startup to track and manage all financial transactions — including **incoming payments from clients** and **outgoing settlements** such as vendor payments and salary disbursements.

This system ensures:
- **Security** through role-based access control  
- **Traceability** via audit logging  
- **Comprehensive financial reporting** (monthly and quarterly)  
- **Data integrity** and maintainable design following clean code principles

---

## System Architecture

The project follows a **multi-layered architecture** for better modularity and maintainability:<br/>
Layers : <br/>
Entity Layer – Payment, User, PaymentAuditLog <br/>
DAO Layer – Handles database operations (PaymentDAO, UserDAO, AuditDAO) <br/>
Service Layer – Business logic (PaymentService, UserService, ReportingService) <br/>
Presentation Layer – Console-based menu (MainApp) <br/>
Helper & Enums – IdGenerator, PaymentType, PaymentCategory, PaymentStatus, UserRole <br/>


---

##  Technologies Used

| Component | Technology |
|------------|-------------|
| **Language** | Java (Core Java, JDBC) |
| **Database** | PostgreSQL |
| **Logging** | java.util.logging |
| **Design Patterns** | DAO, Service, Helper, Entity |
| **Utilities** | Enums, SecureRandom, Scanner |

---

## Core Features

###  **Role-Based Access**
| Role | Capabilities |
|------|---------------|
| **Admin** | Manage users (Add/Remove/View), view reports, view audit trail |
| **Finance Manager** | Add/update payments, generate reports |
| **Viewer** | Read-only access to view payments and reports |

---

###  **Payment Management**
- Add **Incoming** or **Outgoing** payments  
- Categorize payments as:
  - `SALARY`
  - `VENDOR_PAYMENT`
  - `CLIENT_INVOICE`
- Update payment status:  
  - `PENDING`, `PROCESSING`, `COMPLETED`
- Generate unique, secure transaction IDs (e.g., `TRANS-9A1BC`)

---

###  **Reporting**
- **Monthly Report** – Totals and summaries for a selected month  
- **Quarterly Report** – Financial overview for a 4-month period  
- Report includes:
  - Transaction count
  - Total incoming/outgoing
  - Category-wise breakdown
  - Status overview

---

###  **Audit Trail**
Every action (e.g., `ADD_PAYMENT`, `UPDATE_STATUS`) is logged with:
- Timestamp  
- Action type  
- Transaction ID  
- Performed by (user)  
- Detailed change description  

Ensures **traceability** and **accountability** across all roles.

---

## Layer-wise Breakdown

###  **1. Presentation Layer**
- CLI-based user interface (via `Scanner`)
- Handles login, registration, and role-based menus
- Routes logic to services (never directly touches the DB)

###  **2. Service Layer**
- Contains core business logic  
- Classes:
  - `UserService` → user management  
  - `PaymentService` → payment logic and updates  
  - `ReportingService` → generates monthly and quarterly reports

###  **3. DAO Layer**
- Interacts directly with PostgreSQL  
- Uses JDBC `PreparedStatement` and `ResultSet` for secure DB operations  
- Classes:
  - `UserDAO` → CRUD on users  
  - `PaymentDAO` → manages payments and audits  
  - `AuditDAO` → retrieves and displays audit logs

###  **4. Entity Layer**
- Represents real-world objects/tables:
  - `User`
  - `Payment`
  - `FinancialReport`
  - `CategorySummary`

### **5. Helper & Enums**
- `DbConnector` → Database connection handler (PostgreSQL)  
- `IdGenerator` → Generates secure random transaction IDs  
- Enums:  
  - `UserRole` → ADMIN, FINANCE_MANAGER, VIEWER  
  - `PaymentType` → INCOMING, OUTGOING  
  - `PaymentCategory` → SALARY, VENDOR_PAYMENT, CLIENT_INVOICE  
  - `PaymentStatus` → PENDING, PROCESSING, COMPLETED  

---

##  Database Design

### Tables
| Table | Purpose |
|--------|----------|
| `users` | Stores login credentials and user roles |
| `payments` | Records all transaction details |
| `payment_audit_log` | Tracks all system actions (add/update/view) |

---

##  Getting Started

<img width="867" height="579" alt="Screenshot 2025-10-09 at 10 15 12 PM" src="https://github.com/user-attachments/assets/c4c23ed8-2fe8-4b0d-812e-b608953892b5" />
<img width="974" height="701" alt="Screenshot 2025-10-09 at 10 05 40 PM" src="https://github.com/user-attachments/assets/d05771b3-0f44-433a-8383-67517eb84f5c" />
<img width="602" height="571" alt="Screenshot 2025-10-09 at 6 33 54 PM" src="https://github.com/user-attachments/assets/b1c87a99-04c7-41b8-8702-380583d8ea18" />
<img width="817" height="425" alt="Screenshot 2025-10-09 at 6 56 12 PM" src="https://github.com/user-attachments/assets/153bcdb2-4d22-4cb2-a68d-ee2c88d3623c" />
<img width="670" height="430" alt="Screenshot 2025-10-09 at 7 02 12 PM" src="https://github.com/user-attachments/assets/eb1914bb-3c26-410e-9c83-dc58a2271ebf" />
<img width="887" height="690" alt="Screenshot 2025-10-09 at 10 00 29 PM" src="https://github.com/user-attachments/assets/d3756b51-1437-4d45-a52c-158ab5d46c89" />
<img width="542" height="528" alt="Screenshot 2025-10-09 at 6 33 43 PM" src="https://github.com/user-attachments/assets/4cde6069-3e73-4acc-ae4b-c5e70963ba3e" />


## Set-Up Instructions :
1. Clone the Repo
git clone https://github.com/tanujav-alt/payment_management_system.git
2. Open in IntelliJ IDEA :
Go to File → Open and select the project folder.
Ensure Java SDK and PostgreSQL JDBC driver are configured.
3. Set Up Database
Create a database named test_payment in PostgreSQL.
Run the provided SQL scripts to create tables:
users, payments, and payment_audit_log.
4. Update DB Credentials
In DbConnector.java, replace with your PostgreSQL username and password.
5. Build Project
Use Build → Build Project or press Ctrl/Cmd + F9.
6. Run the App
Run MainApp.java from IntelliJ.




### Database Setup
```sql
CREATE DATABASE test_payment;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL
);

CREATE TABLE payments (
    transaction_id VARCHAR(20) PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    payment_type VARCHAR(20),
    payment_category VARCHAR(30),
    payment_status VARCHAR(20),
    performed_by VARCHAR(50)
);

CREATE TABLE payment_audit_log (
    id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(20),
    action VARCHAR(50),
    details TEXT,
    performed_by VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


