package com.example.meat_home.util;

import com.example.meat_home.dto.Profile.ProfileDto;
import com.example.meat_home.entity.Customer;
import com.example.meat_home.entity.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileMapper {

    public static ProfileDto toDto(Customer customer) {
        return ProfileDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .password(customer.getPassword())
                .phone(customer.getPhone())
                .dateOfBirth(customer.getDateOfBirth())
                .role(null)
                .build();
    }

    public static ProfileDto toDto(Staff staff) {
        return ProfileDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .email(staff.getEmail())
                .password(staff.getPassword())
                .phone(staff.getPhone())
                .dateOfBirth(staff.getDateOfBirth())
                .role(staff.getRole().name())
                .build();
    }
}
