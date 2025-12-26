package com.smart_delivery_management.smartlogi_delivery.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "SECRET_KEY_123456_SECRET_KEY_123456";

    private static final long EXPIRATION = 86400000; // 24h

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ Utilisé pour LOGIN CLASSIQUE
    public String generateToken(UserDetails user) {
        return generateTokenFromEmail(user.getUsername());
    }

    // ✅ Utilisé pour OAUTH2
    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername());
    }
}
