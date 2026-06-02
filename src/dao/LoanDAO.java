package dao;

import model.Loan;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Loan Data Access Object
 * Handles all database operations for Loan entity
 */
public class LoanDAO {
    
    /**
     * Create new loan request
     * @param loan Loan object
     * @return Created loan ID or -1 if failed
     */
    public int createLoan(Loan loan) {
        String sql = "INSERT INTO loans (member_id, loan_amount, interest_rate, tenure_months, " +
                     "total_interest, total_amount, emi_amount, purpose, status, request_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, loan.getMemberId());
            pstmt.setBigDecimal(2, loan.getLoanAmount());
            pstmt.setBigDecimal(3, loan.getInterestRate());
            pstmt.setInt(4, loan.getTenureMonths());
            pstmt.setBigDecimal(5, loan.getTotalInterest());
            pstmt.setBigDecimal(6, loan.getTotalAmount());
            pstmt.setBigDecimal(7, loan.getEmiAmount());
            pstmt.setString(8, loan.getPurpose());
            pstmt.setString(9, loan.getStatus() != null ? loan.getStatus() : "pending");
            pstmt.setDate(10, new Date(System.currentTimeMillis()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating loan: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get loan by ID
     * @param loanId Loan ID
     * @return Loan object or null
     */
    public Loan getLoanById(int loanId) {
        String sql = "SELECT l.*, u.name as member_name FROM loans l " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id WHERE l.loan_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, loanId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLoan(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all loans
     * @return List of loans
     */
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, u.name as member_name FROM loans l " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id ORDER BY l.request_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all loans: " + e.getMessage());
        }
        return loans;
    }
    
    /**
     * Get loans by member ID
     * @param memberId Member ID
     * @return List of loans
     */
    public List<Loan> getLoansByMemberId(int memberId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, u.name as member_name FROM loans l " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id WHERE l.member_id = ? ORDER BY l.request_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting loans by member: " + e.getMessage());
        }
        return loans;
    }
    
    /**
     * Get loans by status
     * @param status Loan status
     * @return List of loans
     */
    public List<Loan> getLoansByStatus(String status) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, u.name as member_name FROM loans l " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN users u ON m.user_id = u.user_id WHERE l.status = ? ORDER BY l.request_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting loans by status: " + e.getMessage());
        }
        return loans;
    }
    
    /**
     * Approve loan
     * @param loanId Loan ID
     * @param approvedBy Admin user ID
     * @param remarks Approval remarks
     * @return true if approved successfully
     */
    public boolean approveLoan(int loanId, int approvedBy, String remarks) {
        String sql = "UPDATE loans SET status = 'approved', approval_date = ?, " +
                     "start_date = ?, end_date = DATE_ADD(?, INTERVAL tenure_months MONTH), " +
                     "approved_by = ?, remarks = ? WHERE loan_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            Date currentDate = new Date(System.currentTimeMillis());
            pstmt.setDate(1, currentDate);
            pstmt.setDate(2, currentDate);
            pstmt.setDate(3, currentDate);
            pstmt.setInt(4, approvedBy);
            pstmt.setString(5, remarks);
            pstmt.setInt(6, loanId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error approving loan: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Reject loan
     * @param loanId Loan ID
     * @param remarks Rejection remarks
     * @return true if rejected successfully
     */
    public boolean rejectLoan(int loanId, String remarks) {
        String sql = "UPDATE loans SET status = 'rejected', remarks = ? WHERE loan_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, remarks);
            pstmt.setInt(2, loanId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error rejecting loan: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update loan status
     * @param loanId Loan ID
     * @param status New status
     * @return true if updated successfully
     */
    public boolean updateLoanStatus(int loanId, String status) {
        String sql = "UPDATE loans SET status = ? WHERE loan_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, loanId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating loan status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get total loans count
     * @return Count of loans
     */
    public int getTotalLoansCount() {
        String sql = "SELECT COUNT(*) FROM loans";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get loans count by status
     * @param status Loan status
     * @return Count of loans
     */
    public int getLoansCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM loans WHERE status = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan count by status: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get total loan amount
     * @return Total loan amount
     */
    public java.math.BigDecimal getTotalLoanAmount() {
        String sql = "SELECT SUM(loan_amount) FROM loans";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total loan amount: " + e.getMessage());
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Get active loan amount
     * @return Active loan amount
     */
    public java.math.BigDecimal getActiveLoanAmount() {
        String sql = "SELECT SUM(loan_amount) FROM loans WHERE status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error getting active loan amount: " + e.getMessage());
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Map ResultSet to Loan object
     * @param rs ResultSet
     * @return Loan object
     * @throws SQLException
     */
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(rs.getInt("loan_id"));
        loan.setMemberId(rs.getInt("member_id"));
        loan.setMemberName(rs.getString("member_name"));
        loan.setLoanAmount(rs.getBigDecimal("loan_amount"));
        loan.setInterestRate(rs.getBigDecimal("interest_rate"));
        loan.setTenureMonths(rs.getInt("tenure_months"));
        loan.setTotalInterest(rs.getBigDecimal("total_interest"));
        loan.setTotalAmount(rs.getBigDecimal("total_amount"));
        loan.setEmiAmount(rs.getBigDecimal("emi_amount"));
        loan.setPurpose(rs.getString("purpose"));
        loan.setStatus(rs.getString("status"));
        loan.setRequestDate(rs.getDate("request_date"));
        loan.setApprovalDate(rs.getDate("approval_date"));
        loan.setStartDate(rs.getDate("start_date"));
        loan.setEndDate(rs.getDate("end_date"));
        loan.setApprovedBy(rs.getInt("approved_by"));
        if (rs.wasNull()) {
            loan.setApprovedBy(null);
        }
        loan.setRemarks(rs.getString("remarks"));
        return loan;
    }
}
