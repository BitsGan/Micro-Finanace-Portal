package servlets;

import model.DashboardStats;
import model.Loan;
import model.Payment;
import model.EMISchedule;
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
 * Admin Dashboard Servlet
 * Handles admin dashboard operations and statistics
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private MemberDAO memberDAO;
    private LoanDAO loanDAO;
    private PaymentDAO paymentDAO;
    private EMIScheduleDAO emiScheduleDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        memberDAO = new MemberDAO();
        loanDAO = new LoanDAO();
        paymentDAO = new PaymentDAO();
        emiScheduleDAO = new EMIScheduleDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in and is admin
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        if (!SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        // Update overdue EMIs
        emiScheduleDAO.updateOverdueEMIs();
        
        // Get dashboard statistics
        DashboardStats stats = getDashboardStats();
        request.setAttribute("stats", stats);
        
        // Get recent pending loans
        List<Loan> pendingLoans = loanDAO.getLoansByStatus("pending");
        request.setAttribute("pendingLoans", pendingLoans);
        
        // Get recent payments
        List<Payment> recentPayments = paymentDAO.getAllPayments();
        if (recentPayments.size() > 10) {
            recentPayments = recentPayments.subList(0, 10);
        }
        request.setAttribute("recentPayments", recentPayments);
        
        // Get overdue EMIs
        List<EMISchedule> overdueEMIs = emiScheduleDAO.getOverdueEMIs();
        request.setAttribute("overdueEMIs", overdueEMIs);
        
        // Get monthly collection data for chart
        List<Object[]> monthlyData = paymentDAO.getMonthlyCollectionData();
        request.setAttribute("monthlyData", monthlyData);
        
        request.getRequestDispatcher("/jsp/adminDashboard.jsp").forward(request, response);
    }
    
    /**
     * Get dashboard statistics
     * @return DashboardStats object
     */
    private DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();
        
        // Member statistics
        stats.setTotalMembers(memberDAO.getTotalMembersCount());
        stats.setActiveMembers(memberDAO.getActiveMembersCount());
        stats.setNewMembersThisMonth(memberDAO.getNewMembersThisMonth());
        
        // Loan statistics
        stats.setTotalLoans(loanDAO.getTotalLoansCount());
        stats.setActiveLoans(loanDAO.getLoansCountByStatus("active"));
        stats.setPendingLoans(loanDAO.getLoansCountByStatus("pending"));
        stats.setCompletedLoans(loanDAO.getLoansCountByStatus("completed"));
        stats.setTotalLoanAmount(loanDAO.getTotalLoanAmount());
        stats.setActiveLoanAmount(loanDAO.getActiveLoanAmount());
        
        // Payment statistics
        stats.setTotalCollection(paymentDAO.getTotalCollection());
        stats.setMonthlyCollection(paymentDAO.getMonthlyCollection());
        stats.setTodayCollection(paymentDAO.getTodayCollection());
        stats.setTotalTransactions(paymentDAO.getTotalTransactionsCount());
        
        // EMI statistics
        stats.setTotalEmis(emiScheduleDAO.getTotalEMIsCount());
        stats.setPaidEmis(emiScheduleDAO.getEMIsCountByStatus("paid"));
        stats.setPendingEmis(emiScheduleDAO.getEMIsCountByStatus("pending"));
        stats.setOverdueEmis(emiScheduleDAO.getEMIsCountByStatus("overdue"));
        stats.setPendingEmiAmount(emiScheduleDAO.getPendingEMIAmount());
        
        // Savings statistics
        stats.setTotalSavings(memberDAO.getTotalSavings());
        
        return stats;
    }
}
