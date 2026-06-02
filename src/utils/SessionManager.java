package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.User;

/**
 * Session Management Utility Class
 * Handles user session operations
 */
public class SessionManager {
    
    private static final String USER_SESSION_KEY = "currentUser";
    private static final String USER_ROLE_KEY = "userRole";
    private static final String USER_ID_KEY = "userId";
    private static final int SESSION_TIMEOUT = 30 * 60; // 30 minutes
    
    /**
     * Create user session
     * @param request HttpServletRequest
     * @param user User object
     */
    public static void createSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(SESSION_TIMEOUT);
        session.setAttribute(USER_SESSION_KEY, user);
        session.setAttribute(USER_ROLE_KEY, user.getRole());
        session.setAttribute(USER_ID_KEY, user.getUserId());
    }
    
    /**
     * Get current user from session
     * @param request HttpServletRequest
     * @return User object or null
     */
    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(USER_SESSION_KEY);
        }
        return null;
    }
    
    /**
     * Get current user role
     * @param request HttpServletRequest
     * @return User role or null
     */
    public static String getCurrentUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(USER_ROLE_KEY);
        }
        return null;
    }
    
    /**
     * Get current user ID
     * @param request HttpServletRequest
     * @return User ID or -1
     */
    public static int getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Integer userId = (Integer) session.getAttribute(USER_ID_KEY);
            return userId != null ? userId : -1;
        }
        return -1;
    }
    
    /**
     * Check if user is logged in
     * @param request HttpServletRequest
     * @return true if logged in
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
    
    /**
     * Check if user has specific role
     * @param request HttpServletRequest
     * @param role Role to check
     * @return true if user has role
     */
    public static boolean hasRole(HttpServletRequest request, String role) {
        String userRole = getCurrentUserRole(request);
        return userRole != null && userRole.equalsIgnoreCase(role);
    }
    
    /**
     * Check if user is admin
     * @param request HttpServletRequest
     * @return true if admin
     */
    public static boolean isAdmin(HttpServletRequest request) {
        return hasRole(request, "admin");
    }
    
    /**
     * Check if user is cashier
     * @param request HttpServletRequest
     * @return true if cashier
     */
    public static boolean isCashier(HttpServletRequest request) {
        return hasRole(request, "cashier");
    }
    
    /**
     * Check if user is member
     * @param request HttpServletRequest
     * @return true if member
     */
    public static boolean isMember(HttpServletRequest request) {
        return hasRole(request, "member");
    }
    
    /**
     * Invalidate user session (logout)
     * @param request HttpServletRequest
     */
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    
    /**
     * Update session user data
     * @param request HttpServletRequest
     * @param user Updated user object
     */
    public static void updateSessionUser(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(USER_SESSION_KEY, user);
            session.setAttribute(USER_ROLE_KEY, user.getRole());
        }
    }
    
    /**
     * Get session creation time
     * @param request HttpServletRequest
     * @return Session creation time in milliseconds
     */
    public static long getSessionCreationTime(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getCreationTime();
        }
        return 0;
    }
    
    /**
     * Get session last accessed time
     * @param request HttpServletRequest
     * @return Last accessed time in milliseconds
     */
    public static long getLastAccessedTime(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getLastAccessedTime();
        }
        return 0;
    }
}
