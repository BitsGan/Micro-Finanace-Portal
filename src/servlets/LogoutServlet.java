package servlets;

import utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout Servlet
 * Handles user logout and session invalidation
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Log logout action
        if (SessionManager.isLoggedIn(request)) {
            System.out.println("User logged out: " + SessionManager.getCurrentUser(request).getEmail());
        }
        
        // Invalidate session
        SessionManager.invalidateSession(request);
        
        // Redirect to login page with message
        request.setAttribute("success", "You have been logged out successfully.");
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
