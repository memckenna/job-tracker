package com.jobtracker.backend.security;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.jobtracker.backend.service.UserService;

import org.springframework.web.cors.CorsConfigurationSource;

// @RequiredArgsConstructor
// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity(prePostEnabled = true)
// public class SecurityConfig {

//     private final JwtUtil jwtUtil;
//     // private final AuthenticationConfiguration authenticationConfiguration;
//     // private final UserService userService;

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     // AuthenticationManager from AuthenticationConfiguration
//     // @Bean
//     // public AuthenticationManager authenticationManager() throws Exception {
//     // // Let Spring automatically configure it using UserDetailsService +
//     // // PasswordEncoder
//     // return authenticationConfiguration.getAuthenticationManager();
//     // }
//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//         return authConfig.getAuthenticationManager();
//     }

//     // Provide UserDetailsService bean for Spring Security
//     @Bean
//     public org.springframework.security.core.userdetails.UserDetailsService userDetailsService(
//             UserService userService) {
//         return userService;
//     }

//     // Create JwtAuthenticationFilter bean here explicitly to avoid circular
//     // dependency
//     // @Bean
//     // public JwtAuthenticationFilter jwtAuthenticationFilter() {
//     // return new JwtAuthenticationFilter(jwtUtil, userService);
//     // }
//     @Bean
//     public JwtAuthenticationFilter jwtAuthenticationFilter(UserService userService) {
//         return new JwtAuthenticationFilter(jwtUtil, userService);
//     }

//     // @Bean
//     // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     // http
//     // .httpBasic(httpBasic -> httpBasic.disable())
//     // .formLogin(formLogin -> formLogin.disable())
//     // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//     // .csrf(csrf -> csrf.disable())
//     // // .securityMatcher("/**")
//     // .authorizeHttpRequests(auth -> auth
//     // .requestMatchers("/auth/login", "/auth/register").permitAll()
//     // .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//     // .requestMatchers("/graphql", "/graphql/**").permitAll()
//     // .anyRequest().authenticated())
//     // .sessionManagement(session ->
//     // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//     // .addFilterBefore(jwtAuthenticationFilter(),
//     // UsernamePasswordAuthenticationFilter.class);

//     // return http.build();
//     // }

//     // 4️⃣ Security filter chain
//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
//             throws Exception {
//         http
//                 .httpBasic(httpBasic -> httpBasic.disable())
//                 .formLogin(formLogin -> formLogin.disable())
//                 .csrf(csrf -> csrf.disable())
//                 .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/auth/login", "/auth/register").permitAll() // must come first
//                         .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                         .anyRequest().authenticated())
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration config = new CorsConfiguration();
//         // config.setAllowedOrigins(List.of("http://localhost:3000"));
//         config.setAllowedOriginPatterns(List.of("*"));
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         config.setAllowedHeaders(List.of("*"));
//         config.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);
//         return source;
//     }
// }

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserService userService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("Building SecurityFilterChain with CSRF disabled...");
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // config.setAllowedOrigins(List.of("http://localhost:3000"));
        // config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        // config.setAllowedHeaders(List.of("*"));
        // config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("*")); // for Postman testing
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
