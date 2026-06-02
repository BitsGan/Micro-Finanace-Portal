-- =====================================================
-- SANGAM MICRO FINANCE MANAGEMENT SYSTEM
-- Complete Database Schema
-- =====================================================

CREATE DATABASE IF NOT EXISTS sangam_microfinance;
USE sangam_microfinance;

-- =====================================================
-- TABLE 1: USERS (Authentication)
-- =====================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'cashier', 'member') DEFAULT 'member',
    phone VARCHAR(15),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('active', 'inactive') DEFAULT 'active'
);

-- =====================================================
-- TABLE 2: MEMBERS
-- =====================================================
CREATE TABLE members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    join_date DATE NOT NULL,
    monthly_savings_amount DECIMAL(10,2) DEFAULT 0.00,
    total_savings DECIMAL(10,2) DEFAULT 0.00,
    emergency_contact VARCHAR(15),
    occupation VARCHAR(100),
    annual_income DECIMAL(12,2),
    status ENUM('active', 'inactive', 'suspended') DEFAULT 'active',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- TABLE 3: LOANS
-- =====================================================
CREATE TABLE loans (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    loan_amount DECIMAL(10,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL DEFAULT 12.00,
    tenure_months INT NOT NULL,
    total_interest DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    emi_amount DECIMAL(10,2) NOT NULL,
    purpose TEXT,
    status ENUM('pending', 'approved', 'rejected', 'active', 'completed', 'defaulted') DEFAULT 'pending',
    request_date DATE DEFAULT (CURRENT_DATE),
    approval_date DATE,
    start_date DATE,
    end_date DATE,
    approved_by INT,
    remarks TEXT,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES users(user_id)
);

-- =====================================================
-- TABLE 4: EMI SCHEDULE
-- =====================================================
CREATE TABLE emi_schedule (
    emi_id INT PRIMARY KEY AUTO_INCREMENT,
    loan_id INT NOT NULL,
    emi_number INT NOT NULL,
    due_date DATE NOT NULL,
    emi_amount DECIMAL(10,2) NOT NULL,
    principal_amount DECIMAL(10,2) NOT NULL,
    interest_amount DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'paid', 'overdue') DEFAULT 'pending',
    paid_date DATE,
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id) ON DELETE CASCADE
);

-- =====================================================
-- TABLE 5: PAYMENTS
-- =====================================================
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    loan_id INT,
    emi_id INT,
    amount DECIMAL(10,2) NOT NULL,
    payment_mode ENUM('cash', 'online', 'cheque', 'bank_transfer') NOT NULL,
    payment_type ENUM('savings', 'emi', 'loan_repayment', 'other') NOT NULL,
    razorpay_order_id VARCHAR(100),
    razorpay_payment_id VARCHAR(100),
    razorpay_signature VARCHAR(255),
    transaction_id VARCHAR(100) UNIQUE,
    status ENUM('pending', 'success', 'failed', 'refunded') DEFAULT 'pending',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    recorded_by INT,
    notes TEXT,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id) ON DELETE SET NULL,
    FOREIGN KEY (emi_id) REFERENCES emi_schedule(emi_id) ON DELETE SET NULL,
    FOREIGN KEY (recorded_by) REFERENCES users(user_id)
);

-- =====================================================
-- TABLE 6: SAVINGS TRANSACTIONS
-- =====================================================
CREATE TABLE savings_transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_type ENUM('deposit', 'withdrawal') NOT NULL,
    payment_id INT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    recorded_by INT,
    notes TEXT,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (payment_id) REFERENCES payments(payment_id) ON DELETE SET NULL,
    FOREIGN KEY (recorded_by) REFERENCES users(user_id)
);

-- =====================================================
-- TABLE 7: NOTIFICATIONS
-- =====================================================
CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('info', 'warning', 'success', 'error') DEFAULT 'info',
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- TABLE 8: AUDIT LOG
-- =====================================================
CREATE TABLE audit_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id INT,
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- =====================================================
-- INSERT DEFAULT ADMIN USER
-- Password: admin123 (SHA-256 hashed)
-- =====================================================
INSERT INTO users (name, email, password, role, phone, status) VALUES 
('System Administrator', 'admin@sangam.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin', '9876543210', 'active');

-- =====================================================
-- INSERT SAMPLE CASHIER
-- Password: cashier123 (SHA-256 hashed)
-- =====================================================
INSERT INTO users (name, email, password, role, phone, status) VALUES 
('Main Cashier', 'cashier@sangam.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'cashier', '9876543211', 'active');

-- =====================================================
-- CREATE INDEXES FOR BETTER PERFORMANCE
-- =====================================================
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_members_user_id ON members(user_id);
CREATE INDEX idx_members_status ON members(status);
CREATE INDEX idx_loans_member_id ON loans(member_id);
CREATE INDEX idx_loans_status ON loans(status);
CREATE INDEX idx_emi_schedule_loan_id ON emi_schedule(loan_id);
CREATE INDEX idx_emi_schedule_status ON emi_schedule(status);
CREATE INDEX idx_emi_schedule_due_date ON emi_schedule(due_date);
CREATE INDEX idx_payments_member_id ON payments(member_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_date ON payments(payment_date);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);

-- =====================================================
-- CREATE VIEWS FOR REPORTS
-- =====================================================

-- View: Member Summary
CREATE VIEW member_summary AS
SELECT 
    m.member_id,
    u.name,
    u.email,
    u.phone,
    m.join_date,
    m.monthly_savings_amount,
    m.total_savings,
    m.status as member_status,
    COUNT(DISTINCT l.loan_id) as total_loans,
    SUM(CASE WHEN l.status = 'active' THEN l.loan_amount ELSE 0 END) as active_loan_amount
FROM members m
JOIN users u ON m.user_id = u.user_id
LEFT JOIN loans l ON m.member_id = l.member_id
GROUP BY m.member_id, u.name, u.email, u.phone, m.join_date, m.monthly_savings_amount, m.total_savings, m.status;

-- View: Loan Summary
CREATE VIEW loan_summary AS
SELECT 
    l.loan_id,
    m.member_id,
    u.name as member_name,
    l.loan_amount,
    l.interest_rate,
    l.tenure_months,
    l.total_amount,
    l.emi_amount,
    l.status,
    l.request_date,
    l.approval_date,
    COUNT(e.emi_id) as total_emis,
    SUM(CASE WHEN e.status = 'paid' THEN 1 ELSE 0 END) as paid_emis,
    SUM(CASE WHEN e.status = 'pending' THEN 1 ELSE 0 END) as pending_emis,
    SUM(CASE WHEN e.status = 'overdue' THEN 1 ELSE 0 END) as overdue_emis
FROM loans l
JOIN members m ON l.member_id = m.member_id
JOIN users u ON m.user_id = u.user_id
LEFT JOIN emi_schedule e ON l.loan_id = e.loan_id
GROUP BY l.loan_id, m.member_id, u.name, l.loan_amount, l.interest_rate, l.tenure_months, 
         l.total_amount, l.emi_amount, l.status, l.request_date, l.approval_date;

-- View: Daily Collection Report
CREATE VIEW daily_collection AS
SELECT 
    DATE(payment_date) as collection_date,
    payment_type,
    payment_mode,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount
FROM payments
WHERE status = 'success'
GROUP BY DATE(payment_date), payment_type, payment_mode;

-- View: Overdue EMIs
CREATE VIEW overdue_emis AS
SELECT 
    e.emi_id,
    e.loan_id,
    l.member_id,
    u.name as member_name,
    u.email,
    u.phone,
    e.emi_number,
    e.due_date,
    e.emi_amount,
    DATEDIFF(CURRENT_DATE, e.due_date) as days_overdue
FROM emi_schedule e
JOIN loans l ON e.loan_id = l.loan_id
JOIN members m ON l.member_id = m.member_id
JOIN users u ON m.user_id = u.user_id
WHERE e.status = 'pending' AND e.due_date < CURRENT_DATE;

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

DELIMITER //

-- Procedure: Generate EMI Schedule
CREATE PROCEDURE GenerateEMISchedule(IN p_loan_id INT)
BEGIN
    DECLARE v_loan_amount DECIMAL(10,2);
    DECLARE v_interest_rate DECIMAL(5,2);
    DECLARE v_tenure INT;
    DECLARE v_emi_amount DECIMAL(10,2);
    DECLARE v_monthly_interest DECIMAL(10,2);
    DECLARE v_principal DECIMAL(10,2);
    DECLARE v_start_date DATE;
    DECLARE i INT DEFAULT 1;
    
    SELECT loan_amount, interest_rate, tenure_months, emi_amount, start_date
    INTO v_loan_amount, v_interest_rate, v_tenure, v_emi_amount, v_start_date
    FROM loans WHERE loan_id = p_loan_id;
    
    SET v_monthly_interest = (v_loan_amount * v_interest_rate / 100) / 12;
    
    WHILE i <= v_tenure DO
        SET v_principal = v_emi_amount - v_monthly_interest;
        INSERT INTO emi_schedule (loan_id, emi_number, due_date, emi_amount, principal_amount, interest_amount)
        VALUES (p_loan_id, i, DATE_ADD(v_start_date, INTERVAL i MONTH), v_emi_amount, v_principal, v_monthly_interest);
        SET i = i + 1;
    END WHILE;
END //

-- Procedure: Update Member Savings
CREATE PROCEDURE UpdateMemberSavings(IN p_member_id INT, IN p_amount DECIMAL(10,2))
BEGIN
    UPDATE members 
    SET total_savings = total_savings + p_amount 
    WHERE member_id = p_member_id;
END //

-- Procedure: Mark EMI as Paid
CREATE PROCEDURE MarkEMIPaid(IN p_emi_id INT, IN p_payment_id INT)
BEGIN
    UPDATE emi_schedule 
    SET status = 'paid', paid_date = CURRENT_DATE 
    WHERE emi_id = p_emi_id;
    
    -- Check if all EMIs are paid
    UPDATE loans l
    SET status = 'completed'
    WHERE l.loan_id = (SELECT loan_id FROM emi_schedule WHERE emi_id = p_emi_id)
    AND NOT EXISTS (
        SELECT 1 FROM emi_schedule 
        WHERE loan_id = l.loan_id AND status != 'paid'
    );
END //

DELIMITER ;

-- =====================================================
-- TRIGGERS
-- =====================================================

DELIMITER //

-- Trigger: Update member savings after savings transaction
CREATE TRIGGER after_savings_transaction
AFTER INSERT ON savings_transactions
FOR EACH ROW
BEGIN
    IF NEW.transaction_type = 'deposit' THEN
        CALL UpdateMemberSavings(NEW.member_id, NEW.amount);
    ELSE
        CALL UpdateMemberSavings(NEW.member_id, -NEW.amount);
    END IF;
END //

-- Trigger: Create notification on loan approval
CREATE TRIGGER after_loan_approval
AFTER UPDATE ON loans
FOR EACH ROW
BEGIN
    IF OLD.status = 'pending' AND NEW.status = 'approved' THEN
        INSERT INTO notifications (user_id, title, message, type)
        SELECT m.user_id, 'Loan Approved', CONCAT('Your loan of ₹', NEW.loan_amount, ' has been approved.'), 'success'
        FROM members m WHERE m.member_id = NEW.member_id;
    END IF;
END //

DELIMITER ;

-- =====================================================
-- SAMPLE DATA (Optional - for testing)
-- =====================================================

-- Insert sample members
INSERT INTO users (name, email, password, role, phone, address) VALUES 
('Rahul Sharma', 'rahul@email.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'member', '9876543212', '123 Main St, Mumbai'),
('Priya Patel', 'priya@email.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'member', '9876543213', '456 Park Ave, Delhi'),
('Amit Kumar', 'amit@email.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'member', '9876543214', '789 Road St, Bangalore');

INSERT INTO members (user_id, join_date, monthly_savings_amount, emergency_contact, occupation, annual_income) VALUES 
(3, '2024-01-15', 1000.00, '9876543215', 'Teacher', 500000.00),
(4, '2024-02-20', 1500.00, '9876543216', 'Business Owner', 800000.00),
(5, '2024-03-10', 2000.00, '9876543217', 'Software Engineer', 1200000.00);
