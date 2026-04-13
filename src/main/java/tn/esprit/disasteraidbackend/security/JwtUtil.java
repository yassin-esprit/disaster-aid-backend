package tn.esprit.disasteraidbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    @Value("${app.jwt.secret:disaster-aid-super-secret-key-change-in-production-min-32chars}")
    private String secret;

    @Value("${app.jwt.expiration-ms:86400000}") // 24 h
    private long expirationMs;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** Generate a signed JWT containing username + role. */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extract the username (subject) from a token. */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /** Extract the role claim from a token. */
    public String extractRole(String token) {
        return (String) parseClaims(token).get("role");
    }

    /** Return true if the token is valid (signature OK + not expired). */
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}