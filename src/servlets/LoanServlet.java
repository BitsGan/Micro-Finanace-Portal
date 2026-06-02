package servlets;

import model.Loan;
import model.Member;
import dao.LoanDAO;
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
import java.math.BigDecimal;
import java.util.List;

/**
 * Loan Servlet
 * Handles loan management operations
 */
@WebServlet(urlPatterns = {"/admin/loans", "/admin/loan/apply", "/admin/loan/approve", 
                           "/admin/loan/reject", "/member/loan/apply", "/member/loans"})
public class LoanServlet extends HttpServlet {
    
    private LoanDAO loanDAO;
    private MemberDAO memberDAO;
    private EMIScheduleDAO emiScheduleDAO;
    private NotificationDAO notificationDAO;
    
    @Override
    public void init() throws ServletException {
        loanDAO = new LoanDAO();
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
            case "/admin/loans":
                listAllLoans(request, response);
                break;
            case "/member/loans":
                listMemberLoans(request, response);
                break;
            case "/admin/loan/apply":
            case "/member/loan/apply":
                showLoanApplyForm(request, response);
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
            case "/admin/loan/apply":
            case "/member/loan/apply":
                applyForLoan(request, response);
                break;
            case "/admin/loan/approve":
                approveLoan(request, response);
                break;
            case "/admin/loan/reject":
                rejectLoan(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    /**
     * List all loans (Admin)
     */
    private void listAllLoans(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        String status = request.getParameter("status");
        List<Loan> loans;
        
        if (status != null && !status.isEmpty()) {
            loans = loanDAO.getLoansByStatus(status);
        } else {
            loans = loanDAO.getAllLoans();
        }
        
        request.setAttribute("loans", loans);
        request.getRequestDispatcher("/jsp/loans.jsp").forward(request, response);
    }
    
    /**
     * List member's loans
     */
    private void listMemberLoans(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int userId = SessionManager.getCurrentUserId(request);
        Member member = memberDAO.getMemberByUserId(userId);
        
        if (member != null) {
            List<Loan> loans = loanDAO.getLoansByMemberId(member.getMemberId());
            request.setAttribute("loans", loans);
            request.setAttribute("member", member);
        }
        
        request.getRequestDispatcher("/jsp/memberLoans.jsp").forward(request, response);
    }
    
    /**
     * Show loan application form
     */
    private void showLoanApplyForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get active members for admin
        if (SessionManager.isAdmin(request)) {
            List<Member> members = memberDAO.getActiveMembers();
            request.setAttribute("members", members);
        } else {
            // Get current member
            int userId = SessionManager.getCurrentUserId(request);
            Member member = memberDAO.getMemberByUserId(userId);
            request.setAttribute("member", member);
        }
        
        request.getRequestDispatcher("/jsp/loanApply.jsp").forward(request, response);
    }
    
    /**
     * Apply for loan
     */
    private void applyForLoan(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            Loan loan = new Loan();
            
            // Determine member ID
            if (SessionManager.isAdmin(request)) {
                loan.setMemberId(Integer.parseInt(request.getParameter("memberId")));
            } else {
                int userId = SessionManager.getCurrentUserId(request);
                Member member = memberDAO.getMemberByUserId(userId);
                loan.setMemberId(member.getMemberId());
            }
            
            // Set loan details
            loan.setLoanAmount(new BigDecimal(request.getParameter("loanAmount")));
            loan.setInterestRate(new BigDecimal(request.getParameter("interestRate") != null ? 
                request.getParameter("interestRate") : "12.00"));
            loan.setTenureMonths(Integer.parseInt(request.getParameter("tenureMonths")));
            loan.setPurpose(request.getParameter("purpose"));
            loan.setStatus("pending");
            
            // Calculate loan details
            loan.calculateLoanDetails();
            
            // Create loan
            int loanId = loanDAO.createLoan(loan);
            
            if (loanId > 0) {
                // Notify admins about new loan application
                notificationDAO.createNotificationForRole("admin", 
                    "New Loan Application", 
                    "A new loan of ₹" + loan.getLoanAmount() + " has been applied.", 
                    "info");
                
                request.setAttribute("success", "Loan application submitted successfully!");
            } else {
                request.setAttribute("error", "Error submitting loan application");
            }
        } catch (Exception e) {
            System.err.println("Error applying for loan: " + e.getMessage());
            request.setAttribute("error", "Error applying for loan: " + e.getMessage());
        }
        
        // Redirect based on role
        if (SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/loans");
        } else {
            response.sendRedirect(request.getContextPath() + "/member/loans");
        }
    }
    
    /**
     * Approve loan
     */
    private void approveLoan(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        try {
            int loanId = Integer.parseInt(request.getParameter("loanId"));
            String remarks = request.getParameter("remarks");
            int approvedBy = SessionManager.getCurrentUserId(request);
            
            Loan loan = loanDAO.getLoanById(loanId);
            
            if (loan != null && loan.isPending()) {
                if (loanDAO.approveLoan(loanId, approvedBy, remarks)) {
                    // Generate EMI schedule
                    emiScheduleDAO.generateEMISchedule(loanId, loan.getLoanAmount(), 
                        loan.getInterestRate(), loan.getTenureMonths(), 
                        new java.sql.Date(System.currentTimeMillis()));
                    
                    // Update loan status to active
                    loanDAO.updateLoanStatus(loanId, "active");
                    
                    // Notify member
                    Member member = memberDAO.getMemberById(loan.getMemberId());
                    if (member != null) {
                        notificationDAO.createNotification(
                            new model.Notification() {{
                                setUserId(member.getUserId());
                                setTitle("Loan Approved");
                                setMessage("Your loan of ₹" + loan.getLoanAmount() + " has been approved.");
                                setType("success");
                            }}
                        );
                    }
                    
                    request.setAttribute("success", "Loan approved successfully!");
                } else {
                    request.setAttribute("error", "Error approving loan");
                }
            } else {
                request.setAttribute("error", "Loan not found or already processed");
            }
        } catch (Exception e) {
            System.err.println("Error approving loan: " + e.getMessage());
            request.setAttribute("error", "Error approving loan: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/loans");
    }
    
    /**
     * Reject loan
     */
    private void rejectLoan(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        try {
            int loanId = Integer.parseInt(request.getParameter("loanId"));
            String remarks = request.getParameter("remarks");
            
            Loan loan = loanDAO.getLoanById(loanId);
            
            if (loan != null && loan.isPending()) {
                if (loanDAO.rejectLoan(loanId, remarks)) {
                    // Notify member
                    Member member = memberDAO.getMemberById(loan.getMemberId());
                    if (member != null) {
                        notificationDAO.createNotification(
                            new model.Notification() {{
                                setUserId(member.getUserId());
                                setTitle("Loan Rejected");
                                setMessage("Your loan application of ₹" + loan.getLoanAmount() + " has been rejected. Reason: " + remarks);
                                setType("error");
                            }}
                        );
                    }
                    
                    request.setAttribute("success", "Loan rejected successfully!");
                } else {
                    request.setAttribute("error", "Error rejecting loan");
                }
            } else {
                request.setAttribute("error", "Loan not found or already processed");
            }
        } catch (Exception e) {
            System.err.println("Error rejecting loan: " + e.getMessage());
            request.setAttribute("error", "Error rejecting loan: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/loans");
    }
}
