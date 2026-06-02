package dao;

import model.Member;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Member Data Access Object
 * Handles all database operations for Member entity
 */
public class MemberDAO {
    
    /**
     * Create new member
     * @param member Member object
     * @return Created member ID or -1 if failed
     */
    public int createMember(Member member) {
        String sql = "INSERT INTO members (user_id, join_date, monthly_savings_amount, " +
                     "emergency_contact, occupation, annual_income, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, member.getUserId());
            pstmt.setDate(2, member.getJoinDate());
            pstmt.setBigDecimal(3, member.getMonthlySavingsAmount());
            pstmt.setString(4, member.getEmergencyContact());
            pstmt.setString(5, member.getOccupation());
            pstmt.setBigDecimal(6, member.getAnnualIncome());
            pstmt.setString(7, member.getStatus() != null ? member.getStatus() : "active");
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating member: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get member by ID
     * @param memberId Member ID
     * @return Member object or null
     */
    public Member getMemberById(int memberId) {
        String sql = "SELECT m.*, u.name, u.email, u.phone FROM members m " +
                     "JOIN users u ON m.user_id = u.user_id WHERE m.member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting member: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get member by user ID
     * @param userId User ID
     * @return Member object or null
     */
    public Member getMemberByUserId(int userId) {
        String sql = "SELECT m.*, u.name, u.email, u.phone FROM members m " +
                     "JOIN users u ON m.user_id = u.user_id WHERE m.user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting member by user ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all members
     * @return List of members
     */
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.*, u.name, u.email, u.phone FROM members m " +
                     "JOIN users u ON m.user_id = u.user_id ORDER BY m.join_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all members: " + e.getMessage());
        }
        return members;
    }
    
    /**
     * Get active members
     * @return List of active members
     */
    public List<Member> getActiveMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.*, u.name, u.email, u.phone FROM members m " +
                     "JOIN users u ON m.user_id = u.user_id WHERE m.status = 'active' ORDER BY u.name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting active members: " + e.getMessage());
        }
        return members;
    }
    
    /**
     * Update member
     * @param member Member object
     * @return true if updated successfully
     */
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET monthly_savings_amount = ?, emergency_contact = ?, " +
                     "occupation = ?, annual_income = ?, status = ? WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, member.getMonthlySavingsAmount());
            pstmt.setString(2, member.getEmergencyContact());
            pstmt.setString(3, member.getOccupation());
            pstmt.setBigDecimal(4, member.getAnnualIncome());
            pstmt.setString(5, member.getStatus());
            pstmt.setInt(6, member.getMemberId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update member savings
     * @param memberId Member ID
     * @param amount Amount to add (positive) or subtract (negative)
     * @return true if updated successfully
     */
    public boolean updateSavings(int memberId, java.math.BigDecimal amount) {
        String sql = "UPDATE members SET total_savings = total_savings + ? WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, memberId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating savings: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete member
     * @param memberId Member ID
     * @return true if deleted successfully
     */
    public boolean deleteMember(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get total members count
     * @return Count of members
     */
    public int getTotalMembersCount() {
        String sql = "SELECT COUNT(*) FROM members";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting member count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get active members count
     * @return Count of active members
     */
    public int getActiveMembersCount() {
        String sql = "SELECT COUNT(*) FROM members WHERE status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active member count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get new members this month
     * @return Count of new members
     */
    public int getNewMembersThisMonth() {
        String sql = "SELECT COUNT(*) FROM members WHERE MONTH(join_date) = MONTH(CURRENT_DATE) " +
                     "AND YEAR(join_date) = YEAR(CURRENT_DATE)";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting new members count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get total savings
     * @return Total savings amount
     */
    public java.math.BigDecimal getTotalSavings() {
        String sql = "SELECT SUM(total_savings) FROM members";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total savings: " + e.getMessage());
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Search members by name or email
     * @param searchTerm Search term
     * @return List of matching members
     */
    public List<Member> searchMembers(String searchTerm) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.*, u.name, u.email, u.phone FROM members m " +
                     "JOIN users u ON m.user_id = u.user_id " +
                     "WHERE u.name LIKE ? OR u.email LIKE ? OR u.phone LIKE ? ORDER BY u.name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching members: " + e.getMessage());
        }
        return members;
    }
    
    /**
     * Map ResultSet to Member object
     * @param rs ResultSet
     * @return Member object
     * @throws SQLException
     */
    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setUserId(rs.getInt("user_id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setJoinDate(rs.getDate("join_date"));
        member.setMonthlySavingsAmount(rs.getBigDecimal("monthly_savings_amount"));
        member.setTotalSavings(rs.getBigDecimal("total_savings"));
        member.setEmergencyContact(rs.getString("emergency_contact"));
        member.setOccupation(rs.getString("occupation"));
        member.setAnnualIncome(rs.getBigDecimal("annual_income"));
        member.setStatus(rs.getString("status"));
        return member;
    }
}
