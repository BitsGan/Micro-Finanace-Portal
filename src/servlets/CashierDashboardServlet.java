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
import java.util.List;

/**
 * Cashier Dashboard Servlet
 * Handles cashier dashboard operations
 */
@WebServlet("/cashier/dashboard")
public class CashierDashboardServlet extends HttpServlet {
    
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
        
        // Check if user is logged in and is cashier or admin
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        if (!SessionManager.isCashier(request) && !SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        // Get today's collection
        java.math.BigDecimal todayCollection = paymentDAO.getTodayCollection();
        request.setAttribute("todayCollection", todayCollection);
        
        // Get monthly collection
        java.math.BigDecimal monthlyCollection = paymentDAO.getMonthlyCollection();
        request.setAttribute("monthlyCollection", monthlyCollection);
        
        // Get today's transactions
        List<Payment> todayPayments = paymentDAO.getAllPayments();
        // Filter for today's payments only
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        todayPayments.removeIf(p -> !p.getPaymentDate().toString().startsWith(today.toString()));
        if (todayPayments.size() > 10) {
            todayPayments = todayPayments.subList(0, 10);
        }
        request.setAttribute("todayPayments", todayPayments);
        
        // Get total members
        int totalMembers = memberDAO.getTotalMembersCount();
        request.setAttribute("totalMembers", totalMembers);
        
        // Get active members for quick payment
        List<Member> activeMembers = memberDAO.getActiveMembers();
        request.setAttribute("activeMembers", activeMembers);
        
        request.getRequestDispatcher("/jsp/cashierDashboard.jsp").forward(request, response);
    }
}
