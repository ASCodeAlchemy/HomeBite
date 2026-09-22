package com.homebite.Gateway.Utils;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtUtil {

    private final byte[] key;

    public JwtUtil(
            @Value("${jwt.secret}") String secret) {

        this.key = Base64.getDecoder().decode(secret);
    }

    public String extractEmail(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractProviderId(String token) {
        Object providerId = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("providerId");
        return providerId == null ? null : String.valueOf(providerId);
    }

    public void validate(String token) {

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}