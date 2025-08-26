package com.example.meat_home.dto.Profile;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private LocalDate dateOfBirth;
}