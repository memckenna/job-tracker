package com.jobtracker.backend;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;


@ActiveProfiles("test")
@SpringBootTest
class BackendApplicationTests {

    @TestConfiguration
    static class TestConfig {

        // 1) Provide a JwtUtil test bean that does not run the real init (so it won't fail on secret/key issues)
        @Bean
        public com.jobtracker.backend.security.JwtUtil jwtUtil() {
            return new com.jobtracker.backend.security.JwtUtil() {
                @Override
                @jakarta.annotation.PostConstruct
                public void init() {
                    // skip Keys.hmacShaKeyFor and other init logic in tests
                }
            };
        }

        // 2) Provide a JwtAuthenticationFilter bean that calls the real constructor (JwtUtil, UserDetailsService)
        //    but overrides doFilterInternal to be a no-op (just forwards). This avoids needing a valid secret/key
        //    and avoids hitting userDetailsService during tests.
        @Bean
        public com.jobtracker.backend.security.JwtAuthenticationFilter jwtAuthenticationFilter(com.jobtracker.backend.security.JwtUtil jwtUtil) {
            UserDetailsService stubUserDetailsService = new UserDetailsService() {
                @Override
                public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                    // Return a dummy user if ever used — not used because we override filter to no-op
                    return new org.springframework.security.core.userdetails.User(
                            "testuser",
                            "$2a$10$7qKc6J6...dummy-hash", // not used
                            new ArrayList<>()
                    );
                }
            };

            // Call the real constructor and override filter behavior
            return new com.jobtracker.backend.security.JwtAuthenticationFilter(jwtUtil, stubUserDetailsService) {
                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                                HttpServletResponse response,
                                                FilterChain filterChain) throws ServletException, IOException {
                    // no JWT checks in tests: just continue the chain
                    filterChain.doFilter(request, response);
                }
            };
        }
    }

    @Test
    void contextLoads() {
    }
}
