package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // Changed to Role ENUM

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default constructor
    public AppUser() {}

    // 3-param constructor
    public AppUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.USER;  // Use Role enum
        this.createdAt = LocalDateTime.now();
    }

    // 4-param constructor for String role
    public AppUser(String username, String email, String password, String roleStr) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.valueOf(roleStr.toUpperCase());  // Convert String to Role enum
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }  // Returns Role enum
    public void setRole(Role role) { this.role = role; }
    public void setRole(String roleStr) { this.role = Role.valueOf(roleStr.toUpperCase()); }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
