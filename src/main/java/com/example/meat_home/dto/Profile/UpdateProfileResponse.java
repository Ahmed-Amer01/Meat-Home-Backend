package com.example.meat_home.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileResponse {
    private ProfileDto user;
    private String newToken; // null if email not changed
}
