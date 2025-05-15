package com.example.bms_plpeprofeval.models;

public class Admin extends User {
    private String adminId;
    private String role;
    private String department;

    public Admin() {
        super();
        setUserType("admin");
    }

    public Admin(String userId, String email, String password, String fullName, String adminId) {
        super(userId, email, password, fullName, "admin");
        this.adminId = adminId;
    }

    // Getters and Setters
    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}