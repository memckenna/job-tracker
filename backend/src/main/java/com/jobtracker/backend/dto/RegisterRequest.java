package com.jobtracker.backend.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String userId;
    private String email;
    private String password;
}

// @Getter
// @Setter
// public class RegisterRequest {
//     private String username;
//     private String password;
// }
