package model;

import java.math.BigDecimal;

/**
 * Dashboard Statistics Model Class
 * Contains all statistics for admin dashboard
 */
public class DashboardStats {
    
    // Member Statistics
    private int totalMembers;
    private int activeMembers;
    private int newMembersThisMonth;
    
    // Loan Statistics
    private int totalLoans;
    private int activeLoans;
    private int pendingLoans;
    private int completedLoans;
    private BigDecimal totalLoanAmount;
    private BigDecimal activeLoanAmount;
    
    // Payment Statistics
    private BigDecimal totalCollection;
    private BigDecimal monthlyCollection;
    private BigDecimal todayCollection;
    private int totalTransactions;
    
    // EMI Statistics
    private int totalEmis;
    private int paidEmis;
    private int pendingEmis;
    private int overdueEmis;
    private BigDecimal pendingEmiAmount;
    
    // Savings Statistics
    private BigDecimal totalSavings;
    private BigDecimal monthlySavings;
    
    // Constructors
    public DashboardStats() {
        // Initialize BigDecimal fields to avoid null
        this.totalLoanAmount = BigDecimal.ZERO;
        this.activeLoanAmount = BigDecimal.ZERO;
        this.totalCollection = BigDecimal.ZERO;
        this.monthlyCollection = BigDecimal.ZERO;
        this.todayCollection = BigDecimal.ZERO;
        this.pendingEmiAmount = BigDecimal.ZERO;
        this.totalSavings = BigDecimal.ZERO;
        this.monthlySavings = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public int getTotalMembers() { return totalMembers; }
    public void setTotalMembers(int totalMembers) { this.totalMembers = totalMembers; }
    
    public int getActiveMembers() { return activeMembers; }
    public void setActiveMembers(int activeMembers) { this.activeMembers = activeMembers; }
    
    public int getNewMembersThisMonth() { return newMembersThisMonth; }
    public void setNewMembersThisMonth(int newMembersThisMonth) { this.newMembersThisMonth = newMembersThisMonth; }
    
    public int getTotalLoans() { return totalLoans; }
    public void setTotalLoans(int totalLoans) { this.totalLoans = totalLoans; }
    
    public int getActiveLoans() { return activeLoans; }
    public void setActiveLoans(int activeLoans) { this.activeLoans = activeLoans; }
    
    public int getPendingLoans() { return pendingLoans; }
    public void setPendingLoans(int pendingLoans) { this.pendingLoans = pendingLoans; }
    
    public int getCompletedLoans() { return completedLoans; }
    public void setCompletedLoans(int completedLoans) { this.completedLoans = completedLoans; }
    
    public BigDecimal getTotalLoanAmount() { return totalLoanAmount; }
    public void setTotalLoanAmount(BigDecimal totalLoanAmount) { this.totalLoanAmount = totalLoanAmount; }
    
    public BigDecimal getActiveLoanAmount() { return activeLoanAmount; }
    public void setActiveLoanAmount(BigDecimal activeLoanAmount) { this.activeLoanAmount = activeLoanAmount; }
    
    public BigDecimal getTotalCollection() { return totalCollection; }
    public void setTotalCollection(BigDecimal totalCollection) { this.totalCollection = totalCollection; }
    
    public BigDecimal getMonthlyCollection() { return monthlyCollection; }
    public void setMonthlyCollection(BigDecimal monthlyCollection) { this.monthlyCollection = monthlyCollection; }
    
    public BigDecimal getTodayCollection() { return todayCollection; }
    public void setTodayCollection(BigDecimal todayCollection) { this.todayCollection = todayCollection; }
    
    public int getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(int totalTransactions) { this.totalTransactions = totalTransactions; }
    
    public int getTotalEmis() { return totalEmis; }
    public void setTotalEmis(int totalEmis) { this.totalEmis = totalEmis; }
    
    public int getPaidEmis() { return paidEmis; }
    public void setPaidEmis(int paidEmis) { this.paidEmis = paidEmis; }
    
    public int getPendingEmis() { return pendingEmis; }
    public void setPendingEmis(int pendingEmis) { this.pendingEmis = pendingEmis; }
    
    public int getOverdueEmis() { return overdueEmis; }
    public void setOverdueEmis(int overdueEmis) { this.overdueEmis = overdueEmis; }
    
    public BigDecimal getPendingEmiAmount() { return pendingEmiAmount; }
    public void setPendingEmiAmount(BigDecimal pendingEmiAmount) { this.pendingEmiAmount = pendingEmiAmount; }
    
    public BigDecimal getTotalSavings() { return totalSavings; }
    public void setTotalSavings(BigDecimal totalSavings) { this.totalSavings = totalSavings; }
    
    public BigDecimal getMonthlySavings() { return monthlySavings; }
    public void setMonthlySavings(BigDecimal monthlySavings) { this.monthlySavings = monthlySavings; }
    
    /**
     * Calculate EMI completion percentage
     * @return percentage of paid EMIs
     */
    public double getEmiCompletionPercentage() {
        if (totalEmis > 0) {
            return (double) paidEmis / totalEmis * 100;
        }
        return 0;
    }
    
    /**
     * Get loan approval rate
     * @return approval percentage
     */
    public double getLoanApprovalRate() {
        int decidedLoans = activeLoans + completedLoans;
        if (decidedLoans > 0) {
            return (double) decidedLoans / (totalLoans - pendingLoans) * 100;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "DashboardStats [totalMembers=" + totalMembers + ", totalLoans=" + totalLoans + 
               ", totalCollection=" + totalCollection + "]";
    }
}
