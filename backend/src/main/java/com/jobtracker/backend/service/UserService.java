package com.jobtracker.backend.service;

import com.jobtracker.backend.model.User;
import com.jobtracker.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                user.getPassword(),
                new ArrayList<>()  // No roles yet
        );
    }
}

// @Service
// public class UserService implements UserDetailsService {

//     private final UserRepository userRepository;
//     private final PasswordEncoder passwordEncoder;

//     public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//         this.userRepository = userRepository;
//         this.passwordEncoder = passwordEncoder;
//     }

//     public String encodePassword(String rawPassword) {
//         return passwordEncoder.encode(rawPassword);
//     }

//     @Override
//     public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//         return new org.springframework.security.core.userdetails.User(
//                 user.getId(),
//                 user.getPassword(),
//                 new ArrayList<>() // or roles if you have them
//         );
//     }

//     public boolean userExists(String userId) {
//         return userRepository.existsById(userId);
//     }

//     public User saveUser(User user) {
//         return userRepository.save(user);
//     }

//     public Optional<User> findById(String userId) {
//         return userRepository.findById(userId);
//     }
// }

// @Service
// public class UserService implements UserDetailsService {

//     private final UserRepository userRepository;
//     private final PasswordEncoder passwordEncoder;

//     public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//         this.userRepository = userRepository;
//         this.passwordEncoder = passwordEncoder;
//     }

//     @Override
//     public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//         return org.springframework.security.core.userdetails.User
//                 .withUsername(user.getId())
//                 .password(user.getPassword()) // hashed password
//                 .roles("USER") // default role
//                 .build();
//     }

//     // public User registerUser(String userId, String rawPassword) {
//     //     String encodedPassword = passwordEncoder.encode(rawPassword); // use injected bean
//     //     User user = new User();
//     //     user.setId(userId);
//     //     user.setPassword(encodedPassword);
//     //     return userRepository.save(user);
//     // }

//     // @Override
//     // public UserDetails loadUserByUsername(String userId) throws
//     // UsernameNotFoundException {
//     // User user = userRepository.findById(userId)
//     // .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//     // System.out.println("Loaded user: " + user.getId() + " with password: " +
//     // user.getPassword());
//     // return new org.springframework.security.core.userdetails.User(
//     // user.getId(),
//     // user.getPassword(), // ✅ This must be the hashed password
//     // new ArrayList<>());
//     // }

// }
