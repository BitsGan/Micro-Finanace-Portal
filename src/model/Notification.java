package model;

import java.sql.Timestamp;

/**
 * Notification Model Class
 * Represents a notification in the system
 */
public class Notification {
    private int notificationId;
    private int userId;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private Timestamp createdAt;
    
    // Constructors
    public Notification() {}
    
    public Notification(int notificationId, int userId, String title, String message, 
                        String type, boolean isRead, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean isRead) { this.isRead = isRead; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    /**
     * Get CSS class based on notification type
     * @return Bootstrap alert class
     */
    public String getAlertClass() {
        switch (type.toLowerCase()) {
            case "success":
                return "alert-success";
            case "warning":
                return "alert-warning";
            case "error":
                return "alert-danger";
            default:
                return "alert-info";
        }
    }
    
    /**
     * Get icon class based on notification type
     * @return Font Awesome icon class
     */
    public String getIconClass() {
        switch (type.toLowerCase()) {
            case "success":
                return "fa-check-circle";
            case "warning":
                return "fa-exclamation-triangle";
            case "error":
                return "fa-times-circle";
            default:
                return "fa-info-circle";
        }
    }
    
    @Override
    public String toString() {
        return "Notification [notificationId=" + notificationId + ", userId=" + userId + ", title=" + title + "]";
    }
}
