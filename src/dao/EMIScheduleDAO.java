package dao;

import model.EMISchedule;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EMI Schedule Data Access Object
 * Handles all database operations for EMI Schedule entity
 */
public class EMIScheduleDAO {
    
    /**
     * Create EMI schedule entry
     * @param emi EMI Schedule object
     * @return Created EMI ID or -1 if failed
     */
    public int createEMI(EMISchedule emi) {
        String sql = "INSERT INTO emi_schedule (loan_id, emi_number, due_date, emi_amount, " +
                     "principal_amount, interest_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, emi.getLoanId());
            pstmt.setInt(2, emi.getEmiNumber());
            pstmt.setDate(3, emi.getDueDate());
            pstmt.setBigDecimal(4, emi.getEmiAmount());
            pstmt.setBigDecimal(5, emi.getPrincipalAmount());
            pstmt.setBigDecimal(6, emi.getInterestAmount());
            pstmt.setString(7, emi.getStatus() != null ? emi.getStatus() : "pending");
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating EMI: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get EMI by ID
     * @param emiId EMI ID
     * @return EMI Schedule object or null
     */
    public EMISchedule getEMIById(int emiId) {
        String sql = "SELECT e.*, u.name as member_name FROM emi_schedule e " +
                     "JOIN loans l ON e.loan_id = l.loan_id " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id WHERE e.emi_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, emiId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEMI(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting EMI: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get EMIs by loan ID
     * @param loanId Loan ID
     * @return List of EMI schedules
     */
    public List<EMISchedule> getEMIsByLoanId(int loanId) {
        List<EMISchedule> emis = new ArrayList<>();
        String sql = "SELECT * FROM emi_schedule WHERE loan_id = ? ORDER BY emi_number";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, loanId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                emis.add(mapResultSetToEMI(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting EMIs by loan: " + e.getMessage());
        }
        return emis;
    }
    
    /**
     * Get pending EMIs by member ID
     * @param memberId Member ID
     * @return List of pending EMIs
     */
    public List<EMISchedule> getPendingEMIsByMemberId(int memberId) {
        List<EMISchedule> emis = new ArrayList<>();
        String sql = "SELECT e.*, u.name as member_name FROM emi_schedule e " +
                     "JOIN loans l ON e.loan_id = l.loan_id " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id " +
                     "WHERE l.member_id = ? AND e.status = 'pending' ORDER BY e.due_date";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                emis.add(mapResultSetToEMI(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending EMIs: " + e.getMessage());
        }
        return emis;
    }
    
    /**
     * Get all pending EMIs
     * @return List of pending EMIs
     */
    public List<EMISchedule> getAllPendingEMIs() {
        List<EMISchedule> emis = new ArrayList<>();
        String sql = "SELECT e.*, u.name as member_name FROM emi_schedule e " +
                     "JOIN loans l ON e.loan_id = l.loan_id " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id " +
                     "WHERE e.status = 'pending' ORDER BY e.due_date";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                emis.add(mapResultSetToEMI(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all pending EMIs: " + e.getMessage());
        }
        return emis;
    }
    
    /**
     * Get overdue EMIs
     * @return List of overdue EMIs
     */
    public List<EMISchedule> getOverdueEMIs() {
        List<EMISchedule> emis = new ArrayList<>();
        String sql = "SELECT e.*, u.name as member_name, DATEDIFF(CURRENT_DATE, e.due_date) as days_overdue " +
                     "FROM emi_schedule e " +
                     "JOIN loans l ON e.loan_id = l.loan_id " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id " +
                     "WHERE e.status = 'pending' AND e.due_date < CURRENT_DATE ORDER BY e.due_date";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                EMISchedule emi = mapResultSetToEMI(rs);
                emi.setDaysOverdue(rs.getInt("days_overdue"));
                emis.add(emi);
            }
        } catch (SQLException e) {
            System.err.println("Error getting overdue EMIs: " + e.getMessage());
        }
        return emis;
    }
    
    /**
     * Mark EMI as paid
     * @param emiId EMI ID
     * @return true if updated successfully
     */
    public boolean markEMIPaid(int emiId) {
        String sql = "UPDATE emi_schedule SET status = 'paid', paid_date = CURRENT_DATE WHERE emi_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, emiId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking EMI as paid: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mark EMI as paid with payment ID
     * @param emiId EMI ID
     * @param paymentId Payment ID
     * @return true if updated successfully
     */
    public boolean markEMIPaid(int emiId, int paymentId) {
        String sql = "UPDATE emi_schedule SET status = 'paid', paid_date = CURRENT_DATE WHERE emi_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, emiId);
            int result = pstmt.executeUpdate();
            
            // Check if all EMIs are paid and update loan status
            if (result > 0) {
                updateLoanStatusIfCompleted(emiId);
            }
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error marking EMI as paid: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update EMI status
     * @param emiId EMI ID
     * @param status New status
     * @return true if updated successfully
     */
    public boolean updateEMIStatus(int emiId, String status) {
        String sql = "UPDATE emi_schedule SET status = ? WHERE emi_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, emiId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating EMI status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update overdue EMIs
     * Marks all pending EMIs with past due dates as overdue
     * @return Number of EMIs updated
     */
    public int updateOverdueEMIs() {
        String sql = "UPDATE emi_schedule SET status = 'overdue' " +
                     "WHERE status = 'pending' AND due_date < CURRENT_DATE";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error updating overdue EMIs: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get total EMIs count
     * @return Count of EMIs
     */
    public int getTotalEMIsCount() {
        String sql = "SELECT COUNT(*) FROM emi_schedule";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting EMI count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get EMIs count by status
     * @param status EMI status
     * @return Count of EMIs
     */
    public int getEMIsCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM emi_schedule WHERE status = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting EMI count by status: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get pending EMI amount
     * @return Total pending EMI amount
     */
    public java.math.BigDecimal getPendingEMIAmount() {
        String sql = "SELECT SUM(emi_amount) FROM emi_schedule WHERE status = 'pending'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending EMI amount: " + e.getMessage());
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Generate EMI schedule for a loan
     * @param loanId Loan ID
     * @param loanAmount Loan amount
     * @param interestRate Interest rate
     * @param tenureMonths Tenure in months
     * @param startDate Start date
     * @return true if generated successfully
     */
    public boolean generateEMISchedule(int loanId, java.math.BigDecimal loanAmount, 
                                       java.math.BigDecimal interestRate, int tenureMonths, Date startDate) {
        try {
            // Calculate EMI details
            java.math.BigDecimal timeInYears = new java.math.BigDecimal(tenureMonths)
                    .divide(new java.math.BigDecimal(12), 4, java.math.BigDecimal.ROUND_HALF_UP);
            java.math.BigDecimal totalInterest = loanAmount.multiply(interestRate)
                    .multiply(timeInYears).divide(new java.math.BigDecimal(100), 2, java.math.BigDecimal.ROUND_HALF_UP);
            java.math.BigDecimal totalAmount = loanAmount.add(totalInterest);
            java.math.BigDecimal emiAmount = totalAmount.divide(new java.math.BigDecimal(tenureMonths), 2, java.math.BigDecimal.ROUND_HALF_UP);
            java.math.BigDecimal monthlyInterest = totalInterest.divide(new java.math.BigDecimal(tenureMonths), 2, java.math.BigDecimal.ROUND_HALF_UP);
            
            // Generate EMI entries
            for (int i = 1; i <= tenureMonths; i++) {
                EMISchedule emi = new EMISchedule();
                emi.setLoanId(loanId);
                emi.setEmiNumber(i);
                emi.setDueDate(new Date(startDate.getTime() + (i * 30L * 24 * 60 * 60 * 1000))); // Approximate month
                emi.setEmiAmount(emiAmount);
                emi.setInterestAmount(monthlyInterest);
                emi.setPrincipalAmount(emiAmount.subtract(monthlyInterest));
                emi.setStatus("pending");
                
                createEMI(emi);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error generating EMI schedule: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update loan status if all EMIs are paid
     * @param emiId EMI ID
     */
    private void updateLoanStatusIfCompleted(int emiId) {
        String sql = "UPDATE loans SET status = 'completed' WHERE loan_id = " +
                     "(SELECT loan_id FROM emi_schedule WHERE emi_id = ?) AND NOT EXISTS " +
                     "(SELECT 1 FROM emi_schedule WHERE loan_id = loans.loan_id AND status != 'paid')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, emiId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating loan status: " + e.getMessage());
        }
    }
    
    /**
     * Map ResultSet to EMI Schedule object
     * @param rs ResultSet
     * @return EMI Schedule object
     * @throws SQLException
     */
    private EMISchedule mapResultSetToEMI(ResultSet rs) throws SQLException {
        EMISchedule emi = new EMISchedule();
        emi.setEmiId(rs.getInt("emi_id"));
        emi.setLoanId(rs.getInt("loan_id"));
        emi.setEmiNumber(rs.getInt("emi_number"));
        emi.setDueDate(rs.getDate("due_date"));
        emi.setEmiAmount(rs.getBigDecimal("emi_amount"));
        emi.setPrincipalAmount(rs.getBigDecimal("principal_amount"));
        emi.setInterestAmount(rs.getBigDecimal("interest_amount"));
        emi.setStatus(rs.getString("status"));
        emi.setPaidDate(rs.getDate("paid_date"));
        
        // Optional field
        try {
            emi.setMemberName(rs.getString("member_name"));
        } catch (SQLException e) {
            // Column may not exist in all queries
        }
        
        return emi;
    }
}
