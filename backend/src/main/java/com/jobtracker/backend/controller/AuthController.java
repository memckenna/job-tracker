package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.LoginRequest;
import com.jobtracker.backend.dto.RegisterRequest;
import com.jobtracker.backend.model.User;
import com.jobtracker.backend.repository.UserRepository;
import com.jobtracker.backend.security.JwtUtil;
import com.jobtracker.backend.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody() LoginRequest request) {
    // System.out.println("Request body: " + request);
    // System.out.println("Login attempt for user: " + request.getUserId());
    // // 1️⃣ Basic validation
    // if (request.getUserId() == null || request.getUserId().isBlank() ||
    // request.getPassword() == null || request.getPassword().isBlank()) {
    // return ResponseEntity.badRequest().body("User ID and password are
    // required.");
    // }

    // try {
    // // 2️⃣ Authenticate credentials
    // Authentication authentication = authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(request.getUserId(),
    // request.getPassword()));

    // System.out.println("Authentication successful: " +
    // authentication.isAuthenticated());

    // // 3️⃣ Optional: set authentication in SecurityContext if needed
    // SecurityContextHolder.getContext().setAuthentication(authentication);

    // // 4️⃣ Retrieve User entity (needed for JWT generation)
    // User user = userRepository.findById(request.getUserId())
    // .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // // 5️⃣ Generate JWT token
    // String token = jwtUtil.generateToken(user);
    // System.out.println(">>> Generated token: " + token);

    // // 6️⃣ Return token in response
    // return ResponseEntity.ok(new AuthResponse(token));

    // } catch (BadCredentialsException e) {
    // System.out.println("Invalid credentials for user: " + request.getUserId());
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid
    // credentials");
    // } catch (Exception e) {
    // e.printStackTrace();
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal
    // Server Error");
    // }
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // System.out.println("Login attempt for user: " + request.getUserId());

    // try {
    // Authentication authentication = authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(request.getUserId(),
    // request.getPassword()));

    // SecurityContextHolder.getContext().setAuthentication(authentication);

    // User user = userRepository.findById(request.getUserId())
    // .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // String token = jwtUtil.generateToken(user);

    // return ResponseEntity.ok(new AuthResponse(token));

    // } catch (BadCredentialsException e) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid
    // credentials");
    // }
    // }

    // @PostMapping("/login-test")
    // public ResponseEntity<?> loginTest(@RequestBody LoginRequest request) {
    // System.out.println("LoginTest request received: " + request);
    // if (request == null) {
    // return ResponseEntity.badRequest().body("Request body is missing or
    // malformed");
    // }
    // return ResponseEntity.ok("Received LoginRequest for user: " +
    // request.getUserId());
    // }

    // @PostMapping("/register")
    // public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    // if (request.getUserId() == null || request.getUserId().isBlank()
    // || request.getPassword() == null || request.getPassword().isBlank()) {
    // return ResponseEntity.badRequest().body("User ID and password are
    // required.");
    // }

    // if (userRepository.existsById(request.getUserId())) {
    // return ResponseEntity.badRequest().body("User already exists");
    // }

    // User newUser = new User();
    // newUser.setId(request.getUserId());
    // newUser.setPassword(passwordEncoder.encode(request.getPassword()));
    // userRepository.save(newUser);

    // return ResponseEntity.ok("User registered successfully");
    // }

    // @PostMapping("/register")
    // public ResponseEntity<?> register(@RequestBody LoginRequest request) {
    // if (userService.userExists(request.getUserId())) {
    // return ResponseEntity.status(HttpStatus.CONFLICT).body("User already
    // exists");
    // }

    // User user = new User();
    // user.setId(request.getUserId());
    // user.setPassword(userService.encodePassword(request.getPassword()));

    // userService.saveUser(user);
    // return ResponseEntity.ok("User registered successfully");
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // try {
    // Authentication authentication = authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(request.getUserId(),
    // request.getPassword()));

    // SecurityContextHolder.getContext().setAuthentication(authentication);

    // User user = userService.findById(request.getUserId())
    // .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // String token = jwtUtil.generateToken(user);
    // return ResponseEntity.ok(new AuthResponse(token));

    // } catch (BadCredentialsException e) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid
    // credentials");
    // }
    // }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        if (userRepository.existsById(request.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        User user = new User();
        user.setId(request.getUserId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Use repository directly
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login-test")
    public ResponseEntity<?> loginTest(@RequestBody LoginRequest request) {
        System.out.println("LoginTest body: " + request);
        return ResponseEntity.ok("Reached login-test");
    }

    @Getter
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
    }
}
