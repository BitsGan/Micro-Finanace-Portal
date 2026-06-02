package model;

import java.sql.Timestamp;

/**
 * User Model Class
 * Represents a user in the system
 */
public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private String phone;
    private String address;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String status;
    
    // Constructors
    public User() {}
    
    public User(int userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
    
    public User(int userId, String name, String email, String password, String role, 
                String phone, String address, Timestamp createdAt, String status) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.status = status;
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    /**
     * Check if user is active
     * @return true if active
     */
    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }
    
    /**
     * Check if user is admin
     * @return true if admin
     */
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
    
    /**
     * Check if user is cashier
     * @return true if cashier
     */
    public boolean isCashier() {
        return "cashier".equalsIgnoreCase(role);
    }
    
    /**
     * Check if user is member
     * @return true if member
     */
    public boolean isMember() {
        return "member".equalsIgnoreCase(role);
    }
    
    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", role=" + role + "]";
    }
}
