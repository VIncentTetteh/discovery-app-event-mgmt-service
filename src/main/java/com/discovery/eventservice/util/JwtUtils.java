package com.discovery.eventservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Slf4j
@Component
public class JwtUtils {

    private final Key key;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        Claims claims = getAllClaims(token);
        Object roleClaim = claims.get("role");
        if (roleClaim == null) return List.of();

        if (roleClaim instanceof String role) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        } else if (roleClaim instanceof List<?> roles) {
            return roles.stream()
                    .map(Object::toString)
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .toList();
        }
        return List.of();
    }
}
