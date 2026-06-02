package servlets;

import model.Payment;
import model.Member;
import dao.PaymentDAO;
import dao.MemberDAO;
import utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Receipt Servlet
 * Handles receipt generation and viewing
 */
@WebServlet(urlPatterns = {"/receipt", "/receipt/download"})
public class ReceiptServlet extends HttpServlet {
    
    private PaymentDAO paymentDAO;
    private MemberDAO memberDAO;
    
    @Override
    public void init() throws ServletException {
        paymentDAO = new PaymentDAO();
        memberDAO = new MemberDAO();
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
        
        if ("/receipt/download".equals(path)) {
            downloadReceipt(request, response);
        } else {
            viewReceipt(request, response);
        }
    }
    
    /**
     * View receipt
     */
    private void viewReceipt(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String transactionId = request.getParameter("transactionId");
        
        if (transactionId == null || transactionId.isEmpty()) {
            request.setAttribute("error", "Transaction ID is required");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            return;
        }
        
        Payment payment = paymentDAO.getPaymentByTransactionId(transactionId);
        
        if (payment == null) {
            request.setAttribute("error", "Payment not found");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            return;
        }
        
        // Check if user has permission to view this receipt
        int currentUserId = SessionManager.getCurrentUserId(request);
        Member member = memberDAO.getMemberById(payment.getMemberId());
        
        if (!SessionManager.isAdmin(request) && !SessionManager.isCashier(request)) {
            if (member == null || member.getUserId() != currentUserId) {
                response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
                return;
            }
        }
        
        // Format payment date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        String formattedDate = sdf.format(payment.getPaymentDate());
        
        request.setAttribute("payment", payment);
        request.setAttribute("member", member);
        request.setAttribute("formattedDate", formattedDate);
        
        request.getRequestDispatcher("/jsp/receipt.jsp").forward(request, response);
    }
    
    /**
     * Download receipt as PDF (HTML version for simplicity)
     */
    private void downloadReceipt(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String transactionId = request.getParameter("transactionId");
        
        if (transactionId == null || transactionId.isEmpty()) {
            request.setAttribute("error", "Transaction ID is required");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            return;
        }
        
        Payment payment = paymentDAO.getPaymentByTransactionId(transactionId);
        
        if (payment == null) {
            request.setAttribute("error", "Payment not found");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            return;
        }
        
        // Set response headers for download
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment; filename=Receipt-" + transactionId + ".html");
        
        // Generate receipt HTML
        Member member = memberDAO.getMemberById(payment.getMemberId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        String formattedDate = sdf.format(payment.getPaymentDate());
        
        String receiptHtml = generateReceiptHtml(payment, member, formattedDate);
        
        response.getWriter().write(receiptHtml);
    }
    
    /**
     * Generate receipt HTML
     */
    private String generateReceiptHtml(Payment payment, Member member, String formattedDate) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<title>Payment Receipt - ").append(payment.getTransactionId()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }");
        html.append(".receipt { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border: 1px solid #ddd; }");
        html.append(".header { text-align: center; border-bottom: 2px solid #007bff; padding-bottom: 20px; margin-bottom: 20px; }");
        html.append(".header h1 { color: #007bff; margin: 0; }");
        html.append(".header p { margin: 5px 0; color: #666; }");
        html.append(".details { margin: 20px 0; }");
        html.append(".details table { width: 100%; border-collapse: collapse; }");
        html.append(".details td { padding: 10px; border-bottom: 1px solid #eee; }");
        html.append(".details td:first-child { font-weight: bold; width: 40%; color: #555; }");
        html.append(".amount { font-size: 24px; color: #28a745; font-weight: bold; text-align: center; margin: 20px 0; }");
        html.append(".footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; color: #666; }");
        html.append(".stamp { text-align: center; margin: 20px 0; }");
        html.append(".stamp span { display: inline-block; padding: 10px 30px; border: 3px solid #28a745; color: #28a745; font-weight: bold; border-radius: 5px; }");
        html.append("@media print { body { background: white; } .receipt { border: none; max-width: 100%; } }");
        html.append("</style>");
        html.append("</head><body>");
        
        html.append("<div class='receipt'>");
        html.append("<div class='header'>");
        html.append("<h1>SANGAM MICRO FINANCE</h1>");
        html.append("<p>Payment Receipt</p>");
        html.append("<p>123 Finance Street, Mumbai, India</p>");
        html.append("<p>Phone: +91 9876543210</p>");
        html.append("</div>");
        
        html.append("<div class='stamp'><span>PAID</span></div>");
        
        html.append("<div class='amount'>₹").append(payment.getAmount()).append("</div>");
        
        html.append("<div class='details'>");
        html.append("<table>");
        html.append("<tr><td>Transaction ID:</td><td>").append(payment.getTransactionId()).append("</td></tr>");
        html.append("<tr><td>Date & Time:</td><td>").append(formattedDate).append("</td></tr>");
        html.append("<tr><td>Member Name:</td><td>").append(member != null ? member.getName() : "N/A").append("</td></tr>");
        html.append("<tr><td>Email:</td><td>").append(member != null ? member.getEmail() : "N/A").append("</td></tr>");
        html.append("<tr><td>Phone:</td><td>").append(member != null ? member.getPhone() : "N/A").append("</td></tr>");
        html.append("<tr><td>Payment Type:</td><td>").append(capitalizeFirst(payment.getPaymentType())).append("</td></tr>");
        html.append("<tr><td>Payment Mode:</td><td>").append(capitalizeFirst(payment.getPaymentMode())).append("</td></tr>");
        if (payment.getRazorpayPaymentId() != null) {
            html.append("<tr><td>Payment ID:</td><td>").append(payment.getRazorpayPaymentId()).append("</td></tr>");
        }
        if (payment.getNotes() != null && !payment.getNotes().isEmpty()) {
            html.append("<tr><td>Notes:</td><td>").append(payment.getNotes()).append("</td></tr>");
        }
        html.append("</table>");
        html.append("</div>");
        
        html.append("<div class='footer'>");
        html.append("<p>Thank you for your payment!</p>");
        html.append("<p>This is a computer-generated receipt and does not require a signature.</p>");
        html.append("<p>For any queries, please contact us at support@sangamfinance.com</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</body></html>");
        
        return html.toString();
    }
    
    /**
     * Capitalize first letter of string
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
