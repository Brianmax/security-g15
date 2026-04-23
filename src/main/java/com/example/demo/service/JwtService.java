package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username, String role)
    {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .claim("role", role)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token)
    {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenValid(String token)
    {
        Claims claims = extractClaims(token);
        return !claims.getExpiration().before(new Date()); // true si el token es valido\
        //             25             antes que 20 // false
    }

    private Claims extractClaims(String token)
    {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
