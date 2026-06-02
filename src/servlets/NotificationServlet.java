package servlets;

import model.Notification;
import dao.NotificationDAO;
import utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Notification Servlet
 * Handles notification operations
 */
@WebServlet(urlPatterns = {"/notifications", "/notifications/mark-read", "/notifications/get-unread"})
public class NotificationServlet extends HttpServlet {
    
    private NotificationDAO notificationDAO;
    
    @Override
    public void init() throws ServletException {
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
            case "/notifications":
                listNotifications(request, response);
                break;
            case "/notifications/get-unread":
                getUnreadNotifications(request, response);
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
        
        if ("/notifications/mark-read".equals(path)) {
            markAsRead(request, response);
        }
    }
    
    /**
     * List all notifications for user
     */
    private void listNotifications(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int userId = SessionManager.getCurrentUserId(request);
        List<Notification> notifications = notificationDAO.getNotificationsByUserId(userId);
        
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("/jsp/notifications.jsp").forward(request, response);
    }
    
    /**
     * Get unread notifications (AJAX)
     */
    private void getUnreadNotifications(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        int userId = SessionManager.getCurrentUserId(request);
        List<Notification> notifications = notificationDAO.getUnreadNotificationsByUserId(userId);
        int unreadCount = notificationDAO.getUnreadCount(userId);
        
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("count", unreadCount);
        
        JSONArray notificationsArray = new JSONArray();
        for (Notification notification : notifications) {
            JSONObject notifObj = new JSONObject();
            notifObj.put("id", notification.getNotificationId());
            notifObj.put("title", notification.getTitle());
            notifObj.put("message", notification.getMessage());
            notifObj.put("type", notification.getType());
            notifObj.put("createdAt", notification.getCreatedAt().toString());
            notificationsArray.put(notifObj);
        }
        jsonResponse.put("notifications", notificationsArray);
        
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Mark notification as read
     */
    private void markAsRead(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        
        try {
            String notificationIdStr = request.getParameter("notificationId");
            
            if ("all".equals(notificationIdStr)) {
                // Mark all as read
                int userId = SessionManager.getCurrentUserId(request);
                int count = notificationDAO.markAllAsRead(userId);
                jsonResponse.put("success", true);
                jsonResponse.put("message", count + " notifications marked as read");
            } else {
                // Mark single notification as read
                int notificationId = Integer.parseInt(notificationIdStr);
                boolean success = notificationDAO.markAsRead(notificationId);
                jsonResponse.put("success", success);
                jsonResponse.put("message", success ? "Notification marked as read" : "Error marking notification");
            }
        } catch (Exception e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error: " + e.getMessage());
        }
        
        out.print(jsonResponse.toString());
        out.flush();
    }
}
