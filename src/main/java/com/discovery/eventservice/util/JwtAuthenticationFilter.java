package com.discovery.eventservice.util;

import com.discovery.eventservice.model.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        log.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtils.validateToken(token)) {
                Claims claims = jwtUtils.getAllClaims(token);
                String subject = claims.getSubject();
                List<SimpleGrantedAuthority> authorities = jwtUtils.getAuthorities(token);

                log.info("Authenticated user: {}, with roles: {}", subject, authorities);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                claims.get("id"), // store userId as principal
                                null,
                                authorities
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Invalid JWT token");
            }
        }

        filterChain.doFilter(request, response);
    }
}

