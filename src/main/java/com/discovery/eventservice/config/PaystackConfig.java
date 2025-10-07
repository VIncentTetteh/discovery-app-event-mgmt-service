package com.discovery.eventservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class PaystackConfig {
    @Value("${paystack.secret-key}")
    private String SECRET_KEY;

}
