package com.jobtracker.security;

import com.jobtracker.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Component
public class JwtUtil {
    // private final String SECRET_KEY = "your-secret"; // Put in application.yml

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // Defaults to 1 day if not set
    private long expirationMs;

    @PostConstruct
    public void init() {
        System.out.println("JWT Secret: " + secret);
        System.out.println("JWT Expiration: " + expirationMs);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // log or handle invalid token
            return false;
        }
    }
}
