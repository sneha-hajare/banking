# Banking Management System

## Project Description

The **Banking Management System** is a web-based banking application developed using Java and Spring Boot. It provides a secure platform for customers and bank staff to manage accounts, transactions, fund transfers, loans, and account statements.

The application uses **Spring Security** for authentication and role-based access, **Spring Data JPA** for database operations, **MySQL** for data storage, and **Thymeleaf** for the web interface.

The system also provides a downloadable **PDF account statement** containing account and transaction information.

---

## Features

### Customer Features

* Customer registration and login
* Secure authentication using Spring Security
* Create and manage bank accounts
* View account details
* View account balance
* Deposit money
* Withdraw money
* View transaction history
* Transfer money between accounts
* Apply for loans
* View loan status
* View account statement
* Download account statement as PDF
* Customer dashboard
* Logout

### Staff Features

* Staff login
* Staff dashboard
* View pending loan applications
* Approve loan applications
* Reject loan applications
* View bank accounts

### Security Features

* Spring Security authentication
* Role-based authorization
* BCrypt password encryption
* Customer account ownership validation
* Protected customer, staff, and admin routes
* Login and logout functionality

---

## User Roles

The system supports the following roles:

### CUSTOMER

Customers can:

* Manage their accounts
* Perform deposits and withdrawals
* Transfer funds
* View transactions
* Apply for loans
* View loan information
* Download account statements

### STAFF

Staff members can:

* Access the staff dashboard
* View pending loans
* Approve loans
* Reject loans
* View accounts

### ADMIN

The application includes role-based support for administrators through Spring Security.

---

## Technology Stack

| Technology      | Version / Usage                  |
| --------------- | -------------------------------- |
| Java            | 21                               |
| Spring Boot     | 4.1.1                            |
| Spring Data JPA | Database persistence             |
| Spring Security | Authentication and authorization |
| Thymeleaf       | Web interface                    |
| MySQL           | Database                         |
| Hibernate       | ORM                              |
| Gradle          | Build and dependency management  |
| OpenPDF         | PDF statement generation         |
| HTML            | Frontend                         |
| Maven Central   | Dependency repository            |

---

## Main Dependencies

The project uses the following major dependencies:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
implementation 'org.springframework.boot:spring-boot-starter-validation'
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
runtimeOnly 'com.mysql:mysql-connector-j'
implementation 'com.github.librepdf:openpdf:1.3.39'
```

---

## Main Modules

### 1. Authentication Module

Provides:

* Customer registration
* Login
* Logout
* Password encryption
* Role-based access control

---

### 2. Account Management Module

Provides:

* Account creation
* Account details
* Account number
* Account type
* Account balance
* Account status

Accounts are associated with registered users.

---

### 3. Transaction Module

Supports:

* Deposits
* Withdrawals
* Transaction history
* Balance updates
* Balance-after-transaction tracking

The system validates transaction amounts and prevents withdrawals when the account has insufficient balance.

---

### 4. Fund Transfer Module

Allows customers to transfer money between accounts.

The system validates:

* Transfer amount
* Account status
* Available balance
* Source and destination accounts
* Account ownership

Both account balances are updated after a successful transfer.

---

### 5. Loan Management Module

Customers can apply for loans by providing:

* Loan type
* Loan amount
* Interest rate
* Tenure

Loan applications initially receive:

```text
PENDING
```

Staff members can then:

* View pending loans
* Approve loans
* Reject loans

Approved loans receive an approval date and due date.

---

### 6. Dashboard Module

The customer dashboard displays:

* Total number of accounts
* Total balance
* Account details
* Recent transactions
* Quick actions
* Loan access
* Statement access

Separate dashboard routes are available for customer, staff, and admin roles.

---

### 7. Account Statement & Reporting

Customers can view a complete account statement containing:

* Account number
* Account type
* Current balance
* Transaction date
* Transaction type
* Transaction amount
* Balance after transaction
* Transaction description

The statement can also be downloaded as a **PDF file**.

---

## Database

The application uses **MySQL** as the relational database.

The main entities include:

```text
User
Account
Transaction
FundTransfer
Loan
```

These entities are connected using JPA relationships.

---

## Project Structure

The project follows a standard Spring Boot structure:

```text
src
└── main
    ├── java
    │   └── com
    │       └── banking
    │           └── banking
    │               ├── config
    │               ├── controller
    │               ├── model
    │               ├── repository
    │               ├── service
    │               └── BankingApplication.java
    │
    └── resources
        ├── templates
        │   ├── accounts
        │   ├── dashboard
        │   ├── loans
        │   ├── transfers
        │   ├── login.html
        │   ├── register.html
        │   └── statement.html
        │
        └── application.properties
```

---

## Application Configuration

The application uses MySQL for database connectivity.

Configure your database details in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081
```

Replace `YOUR_PASSWORD` with your local MySQL password.

---

## Database Setup

Create the database in MySQL:

```sql
CREATE DATABASE banking_db;
```

Then configure the database username and password in:

```text
application.properties
```

The application uses Hibernate/JPA to manage the required tables.

---

## How to Run the Project

### Prerequisites

Install the following:

* Java JDK 21
* MySQL
* IntelliJ IDEA or another Java IDE
* Gradle support

### Steps

1. Clone or download the project.

2. Open the project in IntelliJ IDEA.

3. Make sure Java 21 is configured.

4. Create the MySQL database:

```sql
CREATE DATABASE banking_db;
```

5. Update the MySQL username and password in:

```text
src/main/resources/application.properties
```

6. Reload the Gradle project.

7. Run:

```text
BankingApplication.java
```

8. Open the application in a browser:

```text
http://localhost:8081
```

---

## Login

The application provides separate access based on user roles.

### Customer

Customers can register through the registration page and then log in using their registered email and password.

### Staff

For the configured test staff account:

```text
Email: staff@bankcore.com
Password: Staff@123
Role: STAFF
```

**Note:** Change test credentials before deploying the application publicly.

---

## Application Flow

```text
User
 │
 ├── Register
 │
 └── Login
      │
      ├── Customer
      │    │
      │    ├── Dashboard
      │    ├── Accounts
      │    ├── Deposit
      │    ├── Withdrawal
      │    ├── Fund Transfer
      │    ├── Transactions
      │    ├── Loans
      │    └── Account Statement
      │              │
      │              └── Download PDF
      │
      └── Staff
           │
           ├── Staff Dashboard
           ├── View Accounts
           └── Loan Management
                ├── Approve
                └── Reject
```

---

## Validation and Security

The application performs validation for important banking operations.

Examples include:

* Deposit amount must be greater than zero.
* Withdrawal amount must be greater than zero.
* Withdrawal cannot exceed available balance.
* Transfer amount must be valid.
* Source and destination accounts cannot be the same.
* Inactive accounts cannot perform transactions.
* Customers can access only their own accounts and statements.
* Staff-only loan management routes are protected.
* Passwords are stored using BCrypt encryption.

---

## Testing Completed

The following functionality has been tested:

* Customer registration and login
* Customer dashboard
* Account creation
* Deposit
* Withdrawal
* Transaction history
* Fund transfer
* Loan application
* Loan approval
* Loan rejection flow
* Account statement
* PDF statement download
* Logout
* Protected dashboard access
* Staff authentication
* Role-based access

---

## PDF Statement

The application uses **OpenPDF** to generate account statements.

The generated statement contains:

* Account information
* Current balance
* Transaction date
* Transaction type
* Amount
* Balance after transaction
* Description

The statement is downloaded as:

```text
account-statement.pdf
```

---

## Future Enhancements

The following features can be added in future versions:

* Admin management dashboard
* Beneficiary management
* Fixed deposits
* Cheque management
* Debit and credit card management
* Interest calculation
* E-statements
* Advanced transaction filtering
* Date-range reports
* REST API
* Email notifications
* Enhanced dashboard charts
* Pagination for large transaction histories

---

## Project Status

```text
Core Banking Management System: COMPLETED
Functional Testing: COMPLETED
Dashboard & Reporting: COMPLETED
PDF Statement: COMPLETED
```

The project is ready for final documentation, screenshots, GitHub preparation, and internship submission.

---

## Author

**Sneha Hajare**

### Internship Project

**Data Alcott Systems — Free Java Full Stack Internship**

**Task:** Banking Management System

**Task ID:** JV-FN-001

---

## License

This project was developed as an internship/academic project for learning and demonstration purposes.
