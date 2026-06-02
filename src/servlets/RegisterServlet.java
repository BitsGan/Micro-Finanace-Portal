package servlets;

import model.User;
import model.Member;
import dao.UserDAO;
import dao.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

/**
 * Register Servlet
 * Handles user registration for new members
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private MemberDAO memberDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        memberDAO = new MemberDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get registration parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String occupation = request.getParameter("occupation");
        String monthlySavingsStr = request.getParameter("monthlySavings");
        
        // Validate input
        String error = validateInput(name, email, password, confirmPassword, phone);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Check if email already exists
        if (userDAO.emailExists(email.trim())) {
            request.setAttribute("error", "Email already registered. Please use a different email.");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }
        
        try {
            // Create user
            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPassword(password);
            user.setRole("member");
            user.setPhone(phone.trim());
            user.setAddress(address != null ? address.trim() : "");
            user.setStatus("active");
            
            int userId = userDAO.createUser(user);
            
            if (userId > 0) {
                // Create member record
                Member member = new Member();
                member.setUserId(userId);
                member.setJoinDate(new Date(System.currentTimeMillis()));
                member.setMonthlySavingsAmount(monthlySavingsStr != null && !monthlySavingsStr.isEmpty() ? 
                    new java.math.BigDecimal(monthlySavingsStr) : new java.math.BigDecimal("1000"));
                member.setEmergencyContact(phone.trim());
                member.setOccupation(occupation != null ? occupation.trim() : "");
                member.setStatus("active");
                
                int memberId = memberDAO.createMember(member);
                
                if (memberId > 0) {
                    request.setAttribute("success", "Registration successful! Please login with your credentials.");
                    request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                    return;
                } else {
                    // Rollback user creation if member creation fails
                    userDAO.deleteUser(userId);
                    request.setAttribute("error", "Error creating member profile. Please try again.");
                }
            } else {
                request.setAttribute("error", "Error creating user account. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            request.setAttribute("error", "An error occurred during registration. Please try again.");
        }
        
        request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
    }
    
    /**
     * Validate registration input
     * @param name Name
     * @param email Email
     * @param password Password
     * @param confirmPassword Confirm password
     * @param phone Phone
     * @return Error message or null if valid
     */
    private String validateInput(String name, String email, String password, 
                                  String confirmPassword, String phone) {
        if (name == null || name.trim().isEmpty()) {
            return "Please enter your name";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Please enter your email";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Please enter a valid email address";
        }
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters long";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
        if (phone == null || phone.trim().isEmpty()) {
            return "Please enter your phone number";
        }
        if (!phone.matches("^[0-9]{10}$")) {
            return "Please enter a valid 10-digit phone number";
        }
        return null;
    }
}
