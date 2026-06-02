package dao;

import model.Notification;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Notification Data Access Object
 * Handles all database operations for Notification entity
 */
public class NotificationDAO {
    
    /**
     * Create new notification
     * @param notification Notification object
     * @return Created notification ID or -1 if failed
     */
    public int createNotification(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, title, message, type, is_read) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, notification.getUserId());
            pstmt.setString(2, notification.getTitle());
            pstmt.setString(3, notification.getMessage());
            pstmt.setString(4, notification.getType());
            pstmt.setBoolean(5, notification.isRead());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating notification: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get notification by ID
     * @param notificationId Notification ID
     * @return Notification object or null
     */
    public Notification getNotificationById(int notificationId) {
        String sql = "SELECT * FROM notifications WHERE notification_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notificationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNotification(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting notification: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get notifications by user ID
     * @param userId User ID
     * @return List of notifications
     */
    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting notifications by user: " + e.getMessage());
        }
        return notifications;
    }
    
    /**
     * Get unread notifications by user ID
     * @param userId User ID
     * @return List of unread notifications
     */
    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = false ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting unread notifications: " + e.getMessage());
        }
        return notifications;
    }
    
    /**
     * Get recent notifications by user ID (limit 10)
     * @param userId User ID
     * @return List of recent notifications
     */
    public List<Notification> getRecentNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT 10";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent notifications: " + e.getMessage());
        }
        return notifications;
    }
    
    /**
     * Mark notification as read
     * @param notificationId Notification ID
     * @return true if updated successfully
     */
    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = true WHERE notification_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notificationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mark all notifications as read for a user
     * @param userId User ID
     * @return Number of notifications updated
     */
    public int markAllAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = true WHERE user_id = ? AND is_read = false";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error marking all notifications as read: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Delete notification
     * @param notificationId Notification ID
     * @return true if deleted successfully
     */
    public boolean deleteNotification(int notificationId) {
        String sql = "DELETE FROM notifications WHERE notification_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notificationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting notification: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete old notifications (older than specified days)
     * @param days Number of days
     * @return Number of notifications deleted
     */
    public int deleteOldNotifications(int days) {
        String sql = "DELETE FROM notifications WHERE created_at < DATE_SUB(CURRENT_DATE, INTERVAL ? DAY)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, days);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting old notifications: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get unread notification count for user
     * @param userId User ID
     * @return Count of unread notifications
     */
    public int getUnreadCount(int userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = false";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting unread count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Create notification for all users with specific role
     * @param role User role
     * @param title Notification title
     * @param message Notification message
     * @param type Notification type
     * @return Number of notifications created
     */
    public int createNotificationForRole(String role, String title, String message, String type) {
        String sql = "INSERT INTO notifications (user_id, title, message, type) " +
                     "SELECT user_id, ?, ?, ? FROM users WHERE role = ? AND status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, message);
            pstmt.setString(3, type);
            pstmt.setString(4, role);
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating notifications for role: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Create notification for all users
     * @param title Notification title
     * @param message Notification message
     * @param type Notification type
     * @return Number of notifications created
     */
    public int createNotificationForAll(String title, String message, String type) {
        String sql = "INSERT INTO notifications (user_id, title, message, type) " +
                     "SELECT user_id, ?, ?, ? FROM users WHERE status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, message);
            pstmt.setString(3, type);
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating notifications for all: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Map ResultSet to Notification object
     * @param rs ResultSet
     * @return Notification object
     * @throws SQLException
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notification_id"));
        notification.setUserId(rs.getInt("user_id"));
        notification.setTitle(rs.getString("title"));
        notification.setMessage(rs.getString("message"));
        notification.setType(rs.getString("type"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setCreatedAt(rs.getTimestamp("created_at"));
        return notification;
    }
}
