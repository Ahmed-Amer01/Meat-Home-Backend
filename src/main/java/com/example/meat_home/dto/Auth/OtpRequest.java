package com.example.meat_home.dto.Auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRequest {
    private String email;
}
