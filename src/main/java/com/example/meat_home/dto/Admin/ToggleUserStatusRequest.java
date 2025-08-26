package com.example.meat_home.dto.Admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToggleUserStatusRequest {
    private String type; // "customer" or "staff"
}