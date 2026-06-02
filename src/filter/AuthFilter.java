package filter;

import utils.SessionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication Filter
 * Secures protected resources and validates user sessions
 */
@WebFilter(urlPatterns = {
    "/admin/*", 
    "/cashier/*", 
    "/member/*", 
    "/payment/*", 
    "/receipt/*", 
    "/notifications/*"
})
public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Check if user is logged in
        if (!SessionManager.isLoggedIn(httpRequest)) {
            // Store the requested URL for redirect after login
            String requestedUrl = httpRequest.getRequestURI();
            if (httpRequest.getQueryString() != null) {
                requestedUrl += "?" + httpRequest.getQueryString();
            }
            httpRequest.getSession().setAttribute("redirectUrl", requestedUrl);
            
            // Redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        
        // Check role-based access
        String requestURI = httpRequest.getRequestURI();
        String userRole = SessionManager.getCurrentUserRole(httpRequest);
        
        if (requestURI.contains("/admin/") && !"admin".equalsIgnoreCase(userRole)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        if (requestURI.contains("/cashier/") && !"cashier".equalsIgnoreCase(userRole) && !"admin".equalsIgnoreCase(userRole)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        if (requestURI.contains("/member/") && !"member".equalsIgnoreCase(userRole) && !"admin".equalsIgnoreCase(userRole)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }
        
        // User is authenticated and authorized, continue
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        System.out.println("AuthFilter destroyed");
    }
}
