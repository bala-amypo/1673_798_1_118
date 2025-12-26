package com.example.demo.dto;

import com.example.demo.model.Role;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role;  // stored as String, but setters can accept Role

    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters/Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // store as String, but allow both String and Role inputs

    public String getRole() { return role; }

    public void setRole(String role) {       // called in normal code
        this.role = role;
    }

    public void setRole(Role role) {         // called by tests: setRole(Role.ANALYST)
        this.role = role != null ? role.name() : null;
    }
}
