package com.example.demo.service;

import com.example.demo.model.AppUser;

public interface AuthService {
    AppUser registerUser(AppUser user);
    AppUser findByUsername(String username);
    boolean existsByEmail(String email);
}