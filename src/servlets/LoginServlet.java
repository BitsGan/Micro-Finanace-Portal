package servlets;

import model.User;
import dao.UserDAO;
import utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Login Servlet
 * Handles user authentication and session creation
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is already logged in
        if (SessionManager.isLoggedIn(request)) {
            redirectToDashboard(response, SessionManager.getCurrentUserRole(request));
            return;
        }
        // Forward to login page
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get login parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validate input
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both email and password");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }
        
        // Authenticate user
        User user = userDAO.authenticateUser(email.trim(), password);
        
        if (user != null) {
            // Create session
            SessionManager.createSession(request, user);
            
            // Log successful login
            System.out.println("User logged in: " + user.getEmail() + " (Role: " + user.getRole() + ")");
            
            // Redirect based on role
            redirectToDashboard(response, user.getRole());
        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Redirect user to appropriate dashboard based on role
     * @param response HttpServletResponse
     * @param role User role
     * @throws IOException
     */
    private void redirectToDashboard(HttpServletResponse response, String role) throws IOException {
        if (role == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }
        
        switch (role.toLowerCase()) {
            case "admin":
                response.sendRedirect("admin/dashboard");
                break;
            case "cashier":
                response.sendRedirect("cashier/dashboard");
                break;
            case "member":
                response.sendRedirect("member/dashboard");
                break;
            default:
                response.sendRedirect("jsp/login.jsp");
        }
    }
    
    private HttpServletRequest request;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        this.request = req;
        super.service(req, resp);
    }
}
