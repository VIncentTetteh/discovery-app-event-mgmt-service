package com.discovery.eventservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CustomUserPrincipal {
    private UUID id;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
}
