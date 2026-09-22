package com.homebite.provider_service.Config;

import com.homebite.provider_service.Entity.Provider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${spring.security.SECRET_KEY}")
    private String secretKey;

    @Value("${spring.security.EXPIRATION}")
    private int expiration;

    private SecretKey cachedKey;

    @PostConstruct
    public void init() {

        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);

            cachedKey = Keys.hmacShaKeyFor(keyBytes);

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid SECRET_KEY: must be a Base64-encoded string",
                    e
            );
        }
    }

    public SecretKey getKey() {
        return cachedKey;
    }

    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        String subject = userDetails.getUsername();

        if (userDetails instanceof Provider provider) {

            subject = provider.getEmail();

            System.out.println("========== JWT GENERATION ==========");
            System.out.println("Provider email = " + provider.getEmail());
            System.out.println("Provider ID    = " + provider.getProviderId());

            claims.put("providerId", String.valueOf(provider.getProviderId()));


            claims.put("role", "PROVIDER");
        }

        System.out.println("Claims = " + claims);
        System.out.println("====================================");

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuer("HomeBite")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .and()
                .signWith(getKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {

        try {

            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (SignatureException e) {

            System.err.println(
                    "JWT signature does not match: "
                            + e.getMessage()
            );

            return null;

        } catch (Exception e) {

            System.err.println(
                    "JWT extraction error: "
                            + e.getMessage()
            );

            return null;
        }
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimResolver) {

        Claims claims = extractAllClaims(token);

        return claims != null
                ? claimResolver.apply(claims)
                : null;
    }

    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    public String extractProviderId(String token) {

        return extractClaim(
                token,
                claims -> claims.get(
                        "providerId",
                        String.class
                )
        );
    }

    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        String username =
                extractUsername(token);

        return username != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        Date expiration =
                extractExpiration(token);

        return expiration != null
                && expiration.before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }
}