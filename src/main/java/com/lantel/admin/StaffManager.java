package com.lantel.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all staff members 
 */
public class StaffManager {
    private final List<AdminUser> staffList;
    private final Map<String, AdminUser> staffMap;
    private AdminUser loggedInUser;

    public StaffManager() {
        this.staffList = new ArrayList<>();
        this.staffMap = new HashMap<>();
        initializeDefaultStaff();
    }

    private void initializeDefaultStaff() {
        AdminUser admin = new AdminUser("ADM001", "admin", "admin123", "System Administrator", "Manager");
        AdminUser receptionist = new AdminUser("RCP001", "receptionist", "recept123", "Front Desk Staff", "Receptionist");
        AdminUser housekeeper = new AdminUser("HK001", "housekeeper", "house123", "Housekeeping Staff", "Housekeeper");
        
        staffList.add(admin);
        staffList.add(receptionist);
        staffList.add(housekeeper);
        
        staffMap.put(admin.getAdminID(), admin);
        staffMap.put(receptionist.getAdminID(), receptionist);
        staffMap.put(housekeeper.getAdminID(), housekeeper);
    }

    public boolean login(String username, String password) {
        for (AdminUser user : staffList) {
            if (user.authenticate(username, password)) {
                this.loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        this.loggedInUser = null;
    }

    public AdminUser getLoggedInUser() {
        return loggedInUser;
    }

    public boolean addStaff(String adminID, String username, String password, String fullName, String role) {
        // Check if username already exists
        for (AdminUser user : staffList) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        AdminUser newUser = new AdminUser(adminID, username, password, fullName, role);
        staffList.add(newUser);
        staffMap.put(adminID, newUser);
        return true;
    }
    // Display all staff members
    public void displayAllStaff() {
        if (staffList.isEmpty()) {
            System.out.println("No staff found.");
            return;
        }
        System.out.println("\n========== STAFF DIRECTORY ==========");
        System.out.printf("%-8s | %-15s | %-20s | %-15s | %-10s%n", 
            "ID", "Username", "Full Name", "Role", "Status");
        System.out.println("======================================");
        for (AdminUser user : staffList) {
            System.out.printf("%-8s | %-15s | %-20s | %-15s | %s%n",
                user.getAdminID(), user.getUsername(), user.getFullName(), 
                user.getRole(), user.isActive() ? "Active" : "Inactive");
        }
    }

    public void deactivateStaff(String adminID) {
        AdminUser user = staffMap.get(adminID);
        if (user != null) {
            user.setActive(false);
            System.out.println("Staff deactivated: " + user.getFullName());
        } else {
            System.out.println("Staff not found.");
        }
    }

    public void activateStaff(String adminID) {
        AdminUser user = staffMap.get(adminID);
        if (user != null) {
            user.setActive(true);
            System.out.println("Staff activated: " + user.getFullName());
        } else {
            System.out.println("Staff not found.");
        }
    }

    public List<AdminUser> getStaffByRole(String role) {
        List<AdminUser> result = new ArrayList<>();
        for (AdminUser user : staffList) {
            if (user.getRole().equalsIgnoreCase(role) && user.isActive()) {
                result.add(user);
            }
        }
        return result;
    }
}
