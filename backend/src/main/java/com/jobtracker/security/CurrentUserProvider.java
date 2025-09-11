package com.jobtracker.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// @Component
// @RequiredArgsConstructor
// public class CurrentUserProvider {

//     private final JwtUtil jwtUtil;
//     private final HttpServletRequest request;

//     public String getCurrentUserId() {
//         String authHeader = request.getHeader("Authorization");
//         System.out.println("🔍 Called getCurrentUserId()");
//         System.out.println("🔐 Auth Header: " + authHeader);
//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//             String token = authHeader.substring(7);
//             if (jwtUtil.validateToken(token)) {
//                 return jwtUtil.extractUserId(token);
//             }
//         }
//         // throw new RuntimeException("Unauthorized");
//         return null; // Or Optional.empty()
//     }
// }

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    public String getCurrentUserId() {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);  // <--- log here

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("JWT token: " + token);  // <--- log token
            if (jwtUtil.validateToken(token)) {
                String userId = jwtUtil.extractUserId(token);
                System.out.println("Extracted userId: " + userId); // <--- log userId
                return userId;
            } else {
                System.out.println("Invalid JWT token");
            }
        } else {
            System.out.println("No or malformed Authorization header");
        }
        throw new RuntimeException("Unauthorized");
    }
}
