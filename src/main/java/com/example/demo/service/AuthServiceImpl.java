package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AuthServiceImpl(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public AppUser registerUser(AppUser user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        
        return userRepository.save(user);
    }
    
    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException("User not found with email: " + username));
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}