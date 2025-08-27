package com.example.meat_home.dto.Auth;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate dateOfBirth;
    private String type; // "customer" or "staff"
    private String role; // optional, if he is a staff
}