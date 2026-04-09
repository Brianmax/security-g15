package com.example.demo.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public String generateToken(String username)
    {
        return null;
    }

    public String extractUsername(String token)
    {
        return null;
    }

    public boolean isTokenValid(String token)
    {
        return true;
    }

    private Claims extractClaims(String token)
    {
        return null;
    }
}
