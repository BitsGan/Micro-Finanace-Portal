package servlets;

import model.Payment;
import model.Member;
import model.EMISchedule;
import dao.PaymentDAO;
import dao.MemberDAO;
import dao.EMIScheduleDAO;
import dao.NotificationDAO;
import utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import org.json.JSONObject;

/**
 * Payment Servlet
 * Handles payment operations including online (Razorpay) and offline payments
 */
@WebServlet(urlPatterns = {"/payment", "/payment/process", "/payment/verify", 
                           "/payment/offline", "/cashier/payment", "/member/pay-emi"})
public class PaymentServlet extends HttpServlet {
    
    private PaymentDAO paymentDAO;
    private MemberDAO memberDAO;
    private EMIScheduleDAO emiScheduleDAO;
    private NotificationDAO notificationDAO;
    
    // Razorpay Configuration (Replace with your actual keys)
    private static final String RAZORPAY_KEY_ID = "rzp_test_YOUR_KEY_ID";
    private static final String RAZORPAY_KEY_SECRET = "YOUR_KEY_SECRET";
    
    @Override
    public void init() throws ServletException {
        paymentDAO = new PaymentDAO();
        memberDAO = new MemberDAO();
        emiScheduleDAO = new EMIScheduleDAO();
        notificationDAO = new NotificationDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/member/pay-emi":
                showPayEMIForm(request, response);
                break;
            case "/cashier/payment":
                showCashierPaymentForm(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/payment/process":
                processPayment(request, response);
                break;
            case "/payment/verify":
                verifyPayment(request, response);
                break;
            case "/payment/offline":
                processOfflinePayment(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    /**
     * Show Pay EMI form for member
     */
    private void showPayEMIForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int userId = SessionManager.getCurrentUserId(request);
        Member member = memberDAO.getMemberByUserId(userId);
        
        if (member != null) {
            // Get pending EMIs for member
            List<EMISchedule> pendingEMIs = emiScheduleDAO.getPendingEMIsByMemberId(member.getMemberId());
            request.setAttribute("pendingEMIs", pendingEMIs);
            request.setAttribute("member", member);
            request.setAttribute("razorpayKeyId", RAZORPAY_KEY_ID);
        }
        
        request.getRequestDispatcher("/jsp/payEMI.jsp").forward(request, response);
    }
    
    /**
     * Show cashier payment form
     */
    private void showCashierPaymentForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!SessionManager.isCashier(request) && !SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        // Get all active members
        List<Member> members = memberDAO.getActiveMembers();
        request.setAttribute("members", members);
        
        request.getRequestDispatcher("/jsp/cashierPayment.jsp").forward(request, response);
    }
    
    /**
     * Process online payment (Razorpay)
     */
    private void processPayment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        
        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            Integer emiId = request.getParameter("emiId") != null ? 
                Integer.parseInt(request.getParameter("emiId")) : null;
            Integer loanId = request.getParameter("loanId") != null ? 
                Integer.parseInt(request.getParameter("loanId")) : null;
            
            // Create payment record
            Payment payment = new Payment();
            payment.setMemberId(memberId);
            payment.setLoanId(loanId);
            payment.setEmiId(emiId);
            payment.setAmount(amount);
            payment.setPaymentMode("online");
            payment.setPaymentType(emiId != null ? "emi" : "savings");
            payment.setStatus("pending");
            payment.generateTransactionId();
            payment.setRecordedBy(SessionManager.getCurrentUserId(request));
            
            int paymentId = paymentDAO.createPayment(payment);
            
            if (paymentId > 0) {
                jsonResponse.put("success", true);
                jsonResponse.put("paymentId", paymentId);
                jsonResponse.put("transactionId", payment.getTransactionId());
                jsonResponse.put("amount", amount.multiply(new BigDecimal("100")).intValue()); // Amount in paise
                jsonResponse.put("keyId", RAZORPAY_KEY_ID);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Error creating payment record");
            }
        } catch (Exception e) {
            System.err.println("Error processing payment: " + e.getMessage());
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error processing payment: " + e.getMessage());
        }
        
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Verify Razorpay payment
     */
    private void verifyPayment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        
        try {
            int paymentId = Integer.parseInt(request.getParameter("paymentId"));
            String razorpayPaymentId = request.getParameter("razorpayPaymentId");
            String razorpayOrderId = request.getParameter("razorpayOrderId");
            String razorpaySignature = request.getParameter("razorpaySignature");
            
            // Get payment record
            Payment payment = paymentDAO.getPaymentById(paymentId);
            
            if (payment != null) {
                // Update payment with Razorpay details
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpayOrderId(razorpayOrderId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus("success");
                
                if (paymentDAO.updateRazorpayDetails(paymentId, razorpayPaymentId, razorpaySignature)) {
                    // If EMI payment, mark EMI as paid
                    if (payment.getEmiId() != null) {
                        emiScheduleDAO.markEMIPaid(payment.getEmiId(), paymentId);
                    }
                    
                    // If savings payment, update member savings
                    if ("savings".equals(payment.getPaymentType())) {
                        memberDAO.updateSavings(payment.getMemberId(), payment.getAmount());
                    }
                    
                    // Create notification
                    Member member = memberDAO.getMemberById(payment.getMemberId());
                    if (member != null) {
                        notificationDAO.createNotification(
                            new model.Notification() {{
                                setUserId(member.getUserId());
                                setTitle("Payment Successful");
                                setMessage("Your payment of ₹" + payment.getAmount() + " has been received. Transaction ID: " + payment.getTransactionId());
                                setType("success");
                            }}
                        );
                    }
                    
                    jsonResponse.put("success", true);
                    jsonResponse.put("transactionId", payment.getTransactionId());
                    jsonResponse.put("message", "Payment verified successfully");
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Error updating payment status");
                }
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Payment record not found");
            }
        } catch (Exception e) {
            System.err.println("Error verifying payment: " + e.getMessage());
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error verifying payment: " + e.getMessage());
        }
        
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Process offline payment (Cashier)
     */
    private void processOfflinePayment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!SessionManager.isCashier(request) && !SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            String paymentMode = request.getParameter("paymentMode");
            String paymentType = request.getParameter("paymentType");
            Integer emiId = request.getParameter("emiId") != null && !request.getParameter("emiId").isEmpty() ? 
                Integer.parseInt(request.getParameter("emiId")) : null;
            Integer loanId = request.getParameter("loanId") != null && !request.getParameter("loanId").isEmpty() ? 
                Integer.parseInt(request.getParameter("loanId")) : null;
            String notes = request.getParameter("notes");
            
            // Create payment record
            Payment payment = new Payment();
            payment.setMemberId(memberId);
            payment.setLoanId(loanId);
            payment.setEmiId(emiId);
            payment.setAmount(amount);
            payment.setPaymentMode(paymentMode);
            payment.setPaymentType(paymentType);
            payment.setStatus("success");
            payment.generateTransactionId();
            payment.setRecordedBy(SessionManager.getCurrentUserId(request));
            payment.setNotes(notes);
            
            int paymentId = paymentDAO.createPayment(payment);
            
            if (paymentId > 0) {
                // If EMI payment, mark EMI as paid
                if (emiId != null) {
                    emiScheduleDAO.markEMIPaid(emiId, paymentId);
                }
                
                // If savings payment, update member savings
                if ("savings".equals(paymentType)) {
                    memberDAO.updateSavings(memberId, amount);
                }
                
                // Create notification
                Member member = memberDAO.getMemberById(memberId);
                if (member != null) {
                    notificationDAO.createNotification(
                        new model.Notification() {{
                            setUserId(member.getUserId());
                            setTitle("Payment Received");
                            setMessage("Your payment of ₹" + amount + " has been received. Transaction ID: " + payment.getTransactionId());
                            setType("success");
                        }}
                    );
                }
                
                request.setAttribute("success", "Payment recorded successfully! Transaction ID: " + payment.getTransactionId());
            } else {
                request.setAttribute("error", "Error recording payment");
            }
        } catch (Exception e) {
            System.err.println("Error processing offline payment: " + e.getMessage());
            request.setAttribute("error", "Error processing payment: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/cashier/payment");
    }
}
