package model;

import java.sql.Date;
import java.math.BigDecimal;

/**
 * Member Model Class
 * Represents a member in the Sangam microfinance system
 */
public class Member {
    private int memberId;
    private int userId;
    private String name;
    private String email;
    private String phone;
    private Date joinDate;
    private BigDecimal monthlySavingsAmount;
    private BigDecimal totalSavings;
    private String emergencyContact;
    private String occupation;
    private BigDecimal annualIncome;
    private String status;
    
    // Additional fields for display
    private int totalLoans;
    private BigDecimal activeLoanAmount;
    
    // Constructors
    public Member() {}
    
    public Member(int memberId, int userId, Date joinDate, BigDecimal monthlySavingsAmount) {
        this.memberId = memberId;
        this.userId = userId;
        this.joinDate = joinDate;
        this.monthlySavingsAmount = monthlySavingsAmount;
    }
    
    // Getters and Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }
    
    public BigDecimal getMonthlySavingsAmount() { return monthlySavingsAmount; }
    public void setMonthlySavingsAmount(BigDecimal monthlySavingsAmount) { this.monthlySavingsAmount = monthlySavingsAmount; }
    
    public BigDecimal getTotalSavings() { return totalSavings; }
    public void setTotalSavings(BigDecimal totalSavings) { this.totalSavings = totalSavings; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public BigDecimal getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(BigDecimal annualIncome) { this.annualIncome = annualIncome; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getTotalLoans() { return totalLoans; }
    public void setTotalLoans(int totalLoans) { this.totalLoans = totalLoans; }
    
    public BigDecimal getActiveLoanAmount() { return activeLoanAmount; }
    public void setActiveLoanAmount(BigDecimal activeLoanAmount) { this.activeLoanAmount = activeLoanAmount; }
    
    /**
     * Check if member is active
     * @return true if active
     */
    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }
    
    @Override
    public String toString() {
        return "Member [memberId=" + memberId + ", name=" + name + ", email=" + email + "]";
    }
}
