package com.discovery.eventservice.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Example: return current logged-in user, or "system"
        return Optional.of("system");
    }
}

