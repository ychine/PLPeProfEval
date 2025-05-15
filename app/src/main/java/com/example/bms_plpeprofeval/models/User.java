package com.example.bms_plpeprofeval.models;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String email;
    private String password;
    private String fullName;
    private String userType;
    private String dateCreated;
    private boolean isActive;

    // Default constructor
    public User() {
    }

    // Constructor with parameters
    public User(String userId, String email, String password, String fullName, String userType) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.userType = userType;
        this.isActive = true;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}