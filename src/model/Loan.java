package model;

import java.sql.Date;
import java.math.BigDecimal;

/**
 * Loan Model Class
 * Represents a loan in the system
 */
public class Loan {
    private int loanId;
    private int memberId;
    private String memberName;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private int tenureMonths;
    private BigDecimal totalInterest;
    private BigDecimal totalAmount;
    private BigDecimal emiAmount;
    private String purpose;
    private String status;
    private Date requestDate;
    private Date approvalDate;
    private Date startDate;
    private Date endDate;
    private Integer approvedBy;
    private String remarks;
    
    // EMI tracking fields
    private int totalEmis;
    private int paidEmis;
    private int pendingEmis;
    private int overdueEmis;
    
    // Constructors
    public Loan() {}
    
    public Loan(int loanId, int memberId, BigDecimal loanAmount, BigDecimal interestRate, 
                int tenureMonths, String status) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.status = status;
    }
    
    // Getters and Setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
    
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    
    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    
    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest = totalInterest; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getEmiAmount() { return emiAmount; }
    public void setEmiAmount(BigDecimal emiAmount) { this.emiAmount = emiAmount; }
    
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }
    
    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public Integer getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Integer approvedBy) { this.approvedBy = approvedBy; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public int getTotalEmis() { return totalEmis; }
    public void setTotalEmis(int totalEmis) { this.totalEmis = totalEmis; }
    
    public int getPaidEmis() { return paidEmis; }
    public void setPaidEmis(int paidEmis) { this.paidEmis = paidEmis; }
    
    public int getPendingEmis() { return pendingEmis; }
    public void setPendingEmis(int pendingEmis) { this.pendingEmis = pendingEmis; }
    
    public int getOverdueEmis() { return overdueEmis; }
    public void setOverdueEmis(int overdueEmis) { this.overdueEmis = overdueEmis; }
    
    /**
     * Calculate loan details using simple interest formula
     * Interest = (P × R × T) / 100
     */
    public void calculateLoanDetails() {
        if (loanAmount != null && interestRate != null && tenureMonths > 0) {
            // Simple Interest: I = (P * R * T) / 100
            // T is in years, so divide months by 12
            BigDecimal timeInYears = new BigDecimal(tenureMonths).divide(new BigDecimal(12), 4, BigDecimal.ROUND_HALF_UP);
            this.totalInterest = loanAmount.multiply(interestRate).multiply(timeInYears).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            this.totalAmount = loanAmount.add(totalInterest);
            this.emiAmount = totalAmount.divide(new BigDecimal(tenureMonths), 2, BigDecimal.ROUND_HALF_UP);
        }
    }
    
    /**
     * Check if loan is pending approval
     * @return true if pending
     */
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    /**
     * Check if loan is active
     * @return true if active
     */
    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }
    
    /**
     * Check if loan is completed
     * @return true if completed
     */
    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(status);
    }
    
    @Override
    public String toString() {
        return "Loan [loanId=" + loanId + ", memberId=" + memberId + ", amount=" + loanAmount + ", status=" + status + "]";
    }
}
