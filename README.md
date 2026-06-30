# Sangam Micro Finance Management System

A comprehensive web-based microfinance management system built with Java (JSP + Servlets), MySQL, and Bootstrap.

## Features

### Core Modules

1. **Authentication Module**
   - User login with role-based access control
   - Password encryption using SHA-256
   - Session management
   - Three user roles: Admin, Cashier, Member

2. **Member Management**
   - Add, edit, delete members
   - View member details and savings
   - Search and filter members

3. **Loan Management**
   - Loan application and approval workflow
   - Interest calculation using simple interest formula
   - EMI schedule generation
   - Loan status tracking

4. **Payment System**
   - Online payments via Razorpay integration
   - Offline cash payments (Cashier)
   - Payment history and tracking
   - Receipt generation

5. **Receipt System**
   - Auto-generated receipts after payment
   - Downloadable receipt (HTML format)
   - Transaction ID tracking

6. **Reports & Analytics**
   - Admin dashboard with statistics
   - Monthly collection charts (Chart.js)
   - Overdue EMI tracking
   - Payment reports

7. **Notification System**
   - Real-time notifications for members
   - Loan approval/rejection alerts
   - Payment success notifications
   - EMI due reminders

8. **Security Features**
   - Password hashing (SHA-256)
   - Session timeout (30 minutes)
   - Role-based access control
   - Authentication filter

## Technology Stack

- **Frontend:** HTML, CSS, Bootstrap 5, JavaScript, Font Awesome
- **Backend:** Java (JSP + Servlets)
- **Server:** Apache Tomcat 9+
- **Database:** MySQL 8.0+
- **Payment Gateway:** Razorpay
- **Charts:** Chart.js

## Prerequisites

1. Java JDK 8 or higher
2. Apache Tomcat 9 or higher
3. MySQL 8.0 or higher
4. Eclipse IDE (or any Java IDE)
5. MySQL Connector/J (JDBC Driver)

## Installation

### Step 1: Database Setup

1. Open MySQL Workbench or command line
2. Create the database:
   ```sql
   CREATE DATABASE sangam_microfinance;
   ```
3. Run the schema script:
   ```bash
   mysql -u root -p sangam_microfinance < database/schema.sql
   ```

### Step 2: Configure Database Connection

Edit `src/utils/DBConnection.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/sangam_microfinance?useSSL=false&serverTimezone=UTC";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password";
```

### Step 3: Configure Razorpay (Optional)

Edit `src/servlets/PaymentServlet.java`:
```java
private static final String RAZORPAY_KEY_ID = "rzp_test_YOUR_KEY_ID";
private static final String RAZORPAY_KEY_SECRET = "YOUR_KEY_SECRET";
```

### Step 4: Deploy to Tomcat

1. Export the project as a WAR file
2. Copy the WAR file to Tomcat's `webapps` directory
3. Start Tomcat server
4. Access the application at: `http://localhost:8080/SangamMicroFinance`

## Default Login Credentials

| Role   | Email                | Password    |
|--------|---------------------|-------------|
| Admin  | admin@sangam.com    | admin123    |
| Cashier| cashier@sangam.com  | cashier123  |

## Project Structure

```
SangamMicroFinance/
├── database/
│   └── schema.sql              # Database schema
├── src/
│   ├── servlets/               # Servlet classes
│   │   ├── LoginServlet.java
│   │   ├── RegisterServlet.java
│   │   ├── AdminDashboardServlet.java
│   │   ├── MemberDashboardServlet.java
│   │   ├── CashierDashboardServlet.java
│   │   ├── MemberServlet.java
│   │   ├── LoanServlet.java
│   │   ├── PaymentServlet.java
│   │   ├── ReceiptServlet.java
│   │   ├── NotificationServlet.java
│   │   └── LogoutServlet.java
│   ├── dao/                    # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── MemberDAO.java
│   │   ├── LoanDAO.java
│   │   ├── PaymentDAO.java
│   │   ├── EMIScheduleDAO.java
│   │   └── NotificationDAO.java
│   ├── model/                  # Model classes
│   │   ├── User.java
│   │   ├── Member.java
│   │   ├── Loan.java
│   │   ├── Payment.java
│   │   ├── EMISchedule.java
│   │   ├── Notification.java
│   │   └── DashboardStats.java
│   ├── utils/                  # Utility classes
│   │   ├── DBConnection.java
│   │   ├── PasswordHash.java
│   │   └── SessionManager.java
│   └── filter/                 # Filters
│       └── AuthFilter.java
├── WebContent/
│   ├── jsp/                    # JSP pages
│   │   ├── login.jsp
│   │   ├── register.jsp
│   │   ├── adminDashboard.jsp
│   │   ├── memberDashboard.jsp
│   │   ├── cashierDashboard.jsp
│   │   ├── members.jsp
│   │   ├── loans.jsp
│   │   ├── payEMI.jsp
│   │   ├── receipt.jsp
│   │   ├── error.jsp
│   │   └── unauthorized.jsp
│   ├── WEB-INF/
│   │   └── web.xml
│   └── index.jsp
└── README.md
```

## Key Features Explained

### Loan Interest Calculation
The system uses Simple Interest formula:
```
Interest = (P × R × T) / 100
Where:
P = Principal amount
R = Annual interest rate
T = Time in years
```

### EMI Calculation
```
EMI = (Principal + Total Interest) / Tenure in months
```

### Password Security
Passwords are hashed using SHA-256 algorithm before storing in the database.

## Screenshots

1. **Login Page** - Secure authentication with role-based access
2. **Admin Dashboard** - Complete overview with charts and statistics
3. **Member Management** - CRUD operations for members
4. **Loan Management** - Approve/reject loan applications
5. **Payment System** - Online and offline payment processing
6. **Receipt Generation** - Automatic receipt generation

## Future Enhancements

- [ ] SMS notifications using Twilio
- [ ] Email notifications
- [ ] Mobile app (Android/iOS)
- [ ] Advanced reporting with PDF export
- [ ] Multi-language support
- [ ] Cloud deployment
- [ ] AI-based loan approval prediction

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check database credentials in DBConnection.java
- Ensure MySQL Connector/J is in classpath

### Payment Gateway Issues
- Verify Razorpay keys are correct
- Check internet connectivity
- Review Razorpay dashboard for transaction status

### Session Timeout
- Default session timeout is 30 minutes
- Can be modified in web.xml

## Support

For any issues or queries, please contact:
- Email: ganeshadigoppula30@gmail.com


## Acknowledgments

- Bootstrap 5 for responsive UI
- Chart.js for data visualization
- Razorpay for payment gateway integration
- Font Awesome for icons

---

**Developed with for Sangam Micro Finance**
