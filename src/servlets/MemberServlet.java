package servlets;

import model.Member;
import model.User;
import dao.MemberDAO;
import dao.UserDAO;
import utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Member Servlet
 * Handles member management operations
 */
@WebServlet(urlPatterns = {"/admin/members", "/admin/member/add", "/admin/member/edit", "/admin/member/delete"})
public class MemberServlet extends HttpServlet {
    
    private MemberDAO memberDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        memberDAO = new MemberDAO();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication and authorization
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/admin/members":
                listMembers(request, response);
                break;
            case "/admin/member/add":
                showAddForm(request, response);
                break;
            case "/admin/member/edit":
                showEditForm(request, response);
                break;
            case "/admin/member/delete":
                deleteMember(request, response);
                break;
            default:
                listMembers(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication and authorization
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/admin/member/add":
                addMember(request, response);
                break;
            case "/admin/member/edit":
                updateMember(request, response);
                break;
            default:
                listMembers(request, response);
        }
    }
    
    /**
     * List all members
     */
    private void listMembers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String searchTerm = request.getParameter("search");
        List<Member> members;
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            members = memberDAO.searchMembers(searchTerm.trim());
        } else {
            members = memberDAO.getAllMembers();
        }
        
        request.setAttribute("members", members);
        request.getRequestDispatcher("/jsp/members.jsp").forward(request, response);
    }
    
    /**
     * Show add member form
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/memberForm.jsp").forward(request, response);
    }
    
    /**
     * Show edit member form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String memberIdStr = request.getParameter("id");
        if (memberIdStr == null || memberIdStr.isEmpty()) {
            response.sendRedirect("/admin/members");
            return;
        }
        
        try {
            int memberId = Integer.parseInt(memberIdStr);
            Member member = memberDAO.getMemberById(memberId);
            
            if (member != null) {
                request.setAttribute("member", member);
                request.getRequestDispatcher("/jsp/memberForm.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Member not found");
                response.sendRedirect("/admin/members");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("/admin/members");
        }
    }
    
    /**
     * Add new member
     */
    private void addMember(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Create user first
            User user = new User();
            user.setName(request.getParameter("name"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(request.getParameter("password"));
            user.setRole("member");
            user.setPhone(request.getParameter("phone"));
            user.setAddress(request.getParameter("address"));
            user.setStatus("active");
            
            int userId = userDAO.createUser(user);
            
            if (userId > 0) {
                // Create member record
                Member member = new Member();
                member.setUserId(userId);
                member.setJoinDate(Date.valueOf(request.getParameter("joinDate")));
                member.setMonthlySavingsAmount(new java.math.BigDecimal(request.getParameter("monthlySavings")));
                member.setEmergencyContact(request.getParameter("emergencyContact"));
                member.setOccupation(request.getParameter("occupation"));
                member.setAnnualIncome(new java.math.BigDecimal(request.getParameter("annualIncome")));
                member.setStatus("active");
                
                int memberId = memberDAO.createMember(member);
                
                if (memberId > 0) {
                    request.setAttribute("success", "Member added successfully!");
                } else {
                    userDAO.deleteUser(userId);
                    request.setAttribute("error", "Error creating member profile");
                }
            } else {
                request.setAttribute("error", "Error creating user account");
            }
        } catch (Exception e) {
            System.err.println("Error adding member: " + e.getMessage());
            request.setAttribute("error", "Error adding member: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/members");
    }
    
    /**
     * Update member
     */
    private void updateMember(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            Member member = memberDAO.getMemberById(memberId);
            
            if (member != null) {
                member.setMonthlySavingsAmount(new java.math.BigDecimal(request.getParameter("monthlySavings")));
                member.setEmergencyContact(request.getParameter("emergencyContact"));
                member.setOccupation(request.getParameter("occupation"));
                member.setAnnualIncome(new java.math.BigDecimal(request.getParameter("annualIncome")));
                member.setStatus(request.getParameter("status"));
                
                if (memberDAO.updateMember(member)) {
                    // Update user info
                    User user = userDAO.getUserById(member.getUserId());
                    if (user != null) {
                        user.setName(request.getParameter("name"));
                        user.setPhone(request.getParameter("phone"));
                        user.setAddress(request.getParameter("address"));
                        userDAO.updateUser(user);
                    }
                    
                    request.setAttribute("success", "Member updated successfully!");
                } else {
                    request.setAttribute("error", "Error updating member");
                }
            } else {
                request.setAttribute("error", "Member not found");
            }
        } catch (Exception e) {
            System.err.println("Error updating member: " + e.getMessage());
            request.setAttribute("error", "Error updating member: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/members");
    }
    
    /**
     * Delete member
     */
    private void deleteMember(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int memberId = Integer.parseInt(request.getParameter("id"));
            Member member = memberDAO.getMemberById(memberId);
            
            if (member != null) {
                // Delete member first (will cascade to related records)
                if (memberDAO.deleteMember(memberId)) {
                    // Delete user
                    userDAO.deleteUser(member.getUserId());
                    request.setAttribute("success", "Member deleted successfully!");
                } else {
                    request.setAttribute("error", "Error deleting member");
                }
            } else {
                request.setAttribute("error", "Member not found");
            }
        } catch (Exception e) {
            System.err.println("Error deleting member: " + e.getMessage());
            request.setAttribute("error", "Error deleting member: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/members");
    }
}
