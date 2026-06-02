package model;

import java.sql.Date;
import java.math.BigDecimal;

/**
 * EMI Schedule Model Class
 * Represents an EMI installment in the schedule
 */
public class EMISchedule {
    private int emiId;
    private int loanId;
    private int emiNumber;
    private Date dueDate;
    private BigDecimal emiAmount;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private String status;
    private Date paidDate;
    
    // Additional fields for display
    private String memberName;
    private int daysOverdue;
    
    // Constructors
    public EMISchedule() {}
    
    public EMISchedule(int emiId, int loanId, int emiNumber, Date dueDate, 
                       BigDecimal emiAmount, String status) {
        this.emiId = emiId;
        this.loanId = loanId;
        this.emiNumber = emiNumber;
        this.dueDate = dueDate;
        this.emiAmount = emiAmount;
        this.status = status;
    }
    
    // Getters and Setters
    public int getEmiId() { return emiId; }
    public void setEmiId(int emiId) { this.emiId = emiId; }
    
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    
    public int getEmiNumber() { return emiNumber; }
    public void setEmiNumber(int emiNumber) { this.emiNumber = emiNumber; }
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public BigDecimal getEmiAmount() { return emiAmount; }
    public void setEmiAmount(BigDecimal emiAmount) { this.emiAmount = emiAmount; }
    
    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }
    
    public BigDecimal getInterestAmount() { return interestAmount; }
    public void setInterestAmount(BigDecimal interestAmount) { this.interestAmount = interestAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getPaidDate() { return paidDate; }
    public void setPaidDate(Date paidDate) { this.paidDate = paidDate; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public int getDaysOverdue() { return daysOverdue; }
    public void setDaysOverdue(int daysOverdue) { this.daysOverdue = daysOverdue; }
    
    /**
     * Check if EMI is paid
     * @return true if paid
     */
    public boolean isPaid() {
        return "paid".equalsIgnoreCase(status);
    }
    
    /**
     * Check if EMI is pending
     * @return true if pending
     */
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    /**
     * Check if EMI is overdue
     * @return true if overdue
     */
    public boolean isOverdue() {
        return "overdue".equalsIgnoreCase(status);
    }
    
    @Override
    public String toString() {
        return "EMISchedule [emiId=" + emiId + ", loanId=" + loanId + ", emiNumber=" + emiNumber + ", status=" + status + "]";
    }
}
