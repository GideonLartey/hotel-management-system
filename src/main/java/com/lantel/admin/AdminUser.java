package com.lantel.admin;


//admin authentication
 
public class AdminUser {
    private final String adminID;
    private final String username;
    private String password;
    private String fullName;
    private String role; // Manager, Receptionist, Housekeeper
    private boolean isActive;

    public AdminUser(String adminID, String username, String password, String fullName, String role) {
        this.adminID = adminID;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true;
    }

    // Getters and Setters
    public String getAdminID() {
        return adminID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password) && this.isActive;
    }
    
    // Display admin user details
    @Override
    public String toString() {
        return "AdminUser{" +
                "adminID='" + adminID + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
