package dao;

import model.Payment;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Payment Data Access Object
 * Handles all database operations for Payment entity
 */
public class PaymentDAO {
    
    /**
     * Create new payment
     * @param payment Payment object
     * @return Created payment ID or -1 if failed
     */
    public int createPayment(Payment payment) {
        String sql = "INSERT INTO payments (member_id, loan_id, emi_id, amount, payment_mode, " +
                     "payment_type, razorpay_order_id, razorpay_payment_id, razorpay_signature, " +
                     "transaction_id, status, recorded_by, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payment.getMemberId());
            pstmt.setObject(2, payment.getLoanId(), Types.INTEGER);
            pstmt.setObject(3, payment.getEmiId(), Types.INTEGER);
            pstmt.setBigDecimal(4, payment.getAmount());
            pstmt.setString(5, payment.getPaymentMode());
            pstmt.setString(6, payment.getPaymentType());
            pstmt.setString(7, payment.getRazorpayOrderId());
            pstmt.setString(8, payment.getRazorpayPaymentId());
            pstmt.setString(9, payment.getRazorpaySignature());
            pstmt.setString(10, payment.getTransactionId());
            pstmt.setString(11, payment.getStatus());
            pstmt.setObject(12, payment.getRecordedBy(), Types.INTEGER);
            pstmt.setString(13, payment.getNotes());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get payment by ID
     * @param paymentId Payment ID
     * @return Payment object or null
     */
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT p.*, u.name as member_name FROM payments p " +
                     "JOIN members m ON p.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id WHERE p.payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get payment by transaction ID
     * @param transactionId Transaction ID
     * @return Payment object or null
     */
    public Payment getPaymentByTransactionId(String transactionId) {
        String sql = "SELECT p.*, u.name as member_name FROM payments p " +
                     "JOIN members m ON p.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id WHERE p.transaction_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment by transaction ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all payments
     * @return List of payments
     */
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.*, u.name as member_name FROM payments p " +
                     "JOIN members m ON p.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id ORDER BY p.payment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all payments: " + e.getMessage());
        }
        return payments;
    }
    
    /**
     * Get payments by member ID
     * @param memberId Member ID
     * @return List of payments
     */
    public List<Payment> getPaymentsByMemberId(int memberId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.*, u.name as member_name FROM payments p " +
                     "JOIN members m ON p.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id WHERE p.member_id = ? ORDER BY p.payment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payments by member: " + e.getMessage());
        }
        return payments;
    }
    
    /**
     * Get payments by status
     * @param status Payment status
     * @return List of payments
     */
    public List<Payment> getPaymentsByStatus(String status) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.*, u.name as member_name FROM payments p " +
                     "JOIN members m ON p.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id WHERE p.status = ? ORDER BY p.payment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payments by status: " + e.getMessage());
        }
        return payments;
    }
    
    /**
     * Update payment status
     * @param paymentId Payment ID
     * @param status New status
     * @return true if updated successfully
     */
    public boolean updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, paymentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payment status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update Razorpay payment details
     * @param paymentId Payment ID
     * @param razorpayPaymentId Razorpay payment ID
     * @param razorpaySignature Razorpay signature
     * @return true if updated successfully
     */
    public boolean updateRazorpayDetails(int paymentId, String razorpayPaymentId, String razorpaySignature) {
        String sql = "UPDATE payments SET razorpay_payment_id = ?, razorpay_signature = ?, status = 'success' WHERE payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, razorpayPaymentId);
            pstmt.setString(2, razorpaySignature);
            pstmt.setInt(3, paymentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating Razorpay details: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get total collection
     * @return Total collection amount
     */
    public java.math.BigDecimal getTotalCollection() {
        String sql = "SELECT SUM(amount) FROM payments WHERE status = 'success'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total collection: " + e.getMessage());
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Get monthly collection
     * @return Monthly collection amount
     */
    public java.math.BigDecimal getMonthlyCollection() {
        String sql = "SELECT SUM(amount) FROM payments WHERE status = 'success' " +
                     "AND MONTH(payment_date) = MONTH(CURRENT_DATE) AND YEAR(payment_date) = YEAR(CURRENT_DATE)";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error getting monthly collection: " + e.getMessage());
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Get today's collection
     * @return Today's collection amount
     */
    public java.math.BigDecimal getTodayCollection() {
        String sql = "SELECT SUM(amount) FROM payments WHERE status = 'success' AND DATE(payment_date) = CURRENT_DATE";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error getting today's collection: " + e.getMessage());
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Get total transactions count
     * @return Count of transactions
     */
    public int getTotalTransactionsCount() {
        String sql = "SELECT COUNT(*) FROM payments WHERE status = 'success'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting transactions count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get monthly collection data for chart
     * @return List of monthly data
     */
    public List<Object[]> getMonthlyCollectionData() {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT MONTHNAME(payment_date) as month, SUM(amount) as total " +
                     "FROM payments WHERE status = 'success' AND payment_date >= DATE_SUB(CURRENT_DATE, INTERVAL 6 MONTH) " +
                     "GROUP BY MONTH(payment_date), MONTHNAME(payment_date) ORDER BY MONTH(payment_date)";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                data.add(new Object[]{rs.getString("month"), rs.getBigDecimal("total")});
            }
        } catch (SQLException e) {
            System.err.println("Error getting monthly collection data: " + e.getMessage());
        }
        return data;
    }
    
    /**
     * Map ResultSet to Payment object
     * @param rs ResultSet
     * @return Payment object
     * @throws SQLException
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setMemberId(rs.getInt("member_id"));
        payment.setMemberName(rs.getString("member_name"));
        payment.setLoanId(rs.getInt("loan_id"));
        if (rs.wasNull()) {
            payment.setLoanId(null);
        }
        payment.setEmiId(rs.getInt("emi_id"));
        if (rs.wasNull()) {
            payment.setEmiId(null);
        }
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentMode(rs.getString("payment_mode"));
        payment.setPaymentType(rs.getString("payment_type"));
        payment.setRazorpayOrderId(rs.getString("razorpay_order_id"));
        payment.setRazorpayPaymentId(rs.getString("razorpay_payment_id"));
        payment.setRazorpaySignature(rs.getString("razorpay_signature"));
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setStatus(rs.getString("status"));
        payment.setPaymentDate(rs.getTimestamp("payment_date"));
        payment.setRecordedBy(rs.getInt("recorded_by"));
        if (rs.wasNull()) {
            payment.setRecordedBy(null);
        }
        payment.setNotes(rs.getString("notes"));
        return payment;
    }
}
