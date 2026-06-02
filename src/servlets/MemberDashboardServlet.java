package servlets;

import model.Member;
import model.Loan;
import model.Payment;
import model.EMISchedule;
import model.Notification;
import dao.*;
import utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Member Dashboard Servlet
 * Handles member dashboard operations
 */
@WebServlet("/member/dashboard")
public class MemberDashboardServlet extends HttpServlet {
    
    private MemberDAO memberDAO;
    private LoanDAO loanDAO;
    private PaymentDAO paymentDAO;
    private EMIScheduleDAO emiScheduleDAO;
    private NotificationDAO notificationDAO;
    
    @Override
    public void init() throws ServletException {
        memberDAO = new MemberDAO();
        loanDAO = new LoanDAO();
        paymentDAO = new PaymentDAO();
        emiScheduleDAO = new EMIScheduleDAO();
        notificationDAO = new NotificationDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in and is member
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        if (!SessionManager.isMember(request)) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        int userId = SessionManager.getCurrentUserId(request);
        Member member = memberDAO.getMemberByUserId(userId);
        
        if (member == null) {
            request.setAttribute("error", "Member profile not found");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            return;
        }
        
        // Update overdue EMIs
        emiScheduleDAO.updateOverdueEMIs();
        
        // Get member's loans
        List<Loan> loans = loanDAO.getLoansByMemberId(member.getMemberId());
        request.setAttribute("loans", loans);
        
        // Get member's payments
        List<Payment> payments = paymentDAO.getPaymentsByMemberId(member.getMemberId());
        if (payments.size() > 5) {
            payments = payments.subList(0, 5);
        }
        request.setAttribute("payments", payments);
        
        // Get pending EMIs
        List<EMISchedule> pendingEMIs = emiScheduleDAO.getPendingEMIsByMemberId(member.getMemberId());
        request.setAttribute("pendingEMIs", pendingEMIs);
        
        // Get notifications
        List<Notification> notifications = notificationDAO.getRecentNotificationsByUserId(userId);
        request.setAttribute("notifications", notifications);
        
        // Get unread notification count
        int unreadCount = notificationDAO.getUnreadCount(userId);
        request.setAttribute("unreadCount", unreadCount);
        
        // Calculate statistics
        int activeLoans = 0;
        int pendingLoans = 0;
        java.math.BigDecimal totalLoanAmount = java.math.BigDecimal.ZERO;
        
        for (Loan loan : loans) {
            if ("active".equals(loan.getStatus())) {
                activeLoans++;
                totalLoanAmount = totalLoanAmount.add(loan.getLoanAmount());
            } else if ("pending".equals(loan.getStatus())) {
                pendingLoans++;
            }
        }
        
        request.setAttribute("member", member);
        request.setAttribute("activeLoans", activeLoans);
        request.setAttribute("pendingLoans", pendingLoans);
        request.setAttribute("totalLoanAmount", totalLoanAmount);
        request.setAttribute("pendingEMICount", pendingEMIs.size());
        
        request.getRequestDispatcher("/jsp/memberDashboard.jsp").forward(request, response);
    }
}
