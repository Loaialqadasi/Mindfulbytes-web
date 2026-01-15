package com.project.Mental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String userId;
    
    private String name;
    private String role;   // Student, Admin, Counselor
    private String status; // Active, Banned
    
    // NEW FIELD
    private String password;

    // 1. Default Constructor (Required by JPA)
    public User() {
    }

    // 2. Constructor WITHOUT password (for Admin creating users manually, maybe default password?)
    public User(String userId, String name, String role, String status) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.status = status;
        this.password = "123456"; // Default password for users created by Admin
    }

    // 3. Constructor WITH password (for Registration)
    public User(String userId, String name, String role, String status, String password) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.status = status;
        this.password = password;
    }

    // --- Getters and Setters ---
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // NEW Getter/Setter
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}