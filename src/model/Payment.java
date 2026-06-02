package model;

import java.sql.Timestamp;
import java.math.BigDecimal;

/**
 * Payment Model Class
 * Represents a payment transaction in the system
 */
public class Payment {
    private int paymentId;
    private int memberId;
    private String memberName;
    private Integer loanId;
    private Integer emiId;
    private BigDecimal amount;
    private String paymentMode;
    private String paymentType;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String transactionId;
    private String status;
    private Timestamp paymentDate;
    private Integer recordedBy;
    private String notes;
    
    // Constructors
    public Payment() {}
    
    public Payment(int paymentId, int memberId, BigDecimal amount, String paymentMode, 
                   String paymentType, String status, Timestamp paymentDate) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.paymentType = paymentType;
        this.status = status;
        this.paymentDate = paymentDate;
    }
    
    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public Integer getLoanId() { return loanId; }
    public void setLoanId(Integer loanId) { this.loanId = loanId; }
    
    public Integer getEmiId() { return emiId; }
    public void setEmiId(Integer emiId) { this.emiId = emiId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    
    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }
    
    public String getRazorpayPaymentId() { return razorpayPaymentId; }
    public void setRazorpayPaymentId(String razorpayPaymentId) { this.razorpayPaymentId = razorpayPaymentId; }
    
    public String getRazorpaySignature() { return razorpaySignature; }
    public void setRazorpaySignature(String razorpaySignature) { this.razorpaySignature = razorpaySignature; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }
    
    public Integer getRecordedBy() { return recordedBy; }
    public void setRecordedBy(Integer recordedBy) { this.recordedBy = recordedBy; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    /**
     * Check if payment was successful
     * @return true if success
     */
    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }
    
    /**
     * Check if payment is online
     * @return true if online payment
     */
    public boolean isOnline() {
        return "online".equalsIgnoreCase(paymentMode);
    }
    
    /**
     * Generate unique transaction ID
     */
    public void generateTransactionId() {
        this.transactionId = "TXN" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
    
    @Override
    public String toString() {
        return "Payment [paymentId=" + paymentId + ", memberId=" + memberId + ", amount=" + amount + ", status=" + status + "]";
    }
}
