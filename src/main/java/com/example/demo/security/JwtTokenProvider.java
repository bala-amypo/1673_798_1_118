package com.example.demo.security;

import com.example.demo.model.AppUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {

    private final Key key;
    private final long validityInMs;

    public JwtTokenProvider(String secret, long validityInMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.validityInMs = validityInMs;
    }

    public String generateToken(AppUser user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("userId", user.getId())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getBody().get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        return parseClaims(token).getBody().get("role", String.class);
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
