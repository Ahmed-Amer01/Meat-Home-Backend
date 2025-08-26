package com.example.meat_home.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate dateOfBirth;
    private String role;
}