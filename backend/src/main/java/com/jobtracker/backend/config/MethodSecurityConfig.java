package com.jobtracker.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

//This turns on @PreAuthorize and related annotations for use in your resolver methods.
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {
    // No need to add anything else here unless customizing
}
