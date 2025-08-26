package com.example.meat_home.service;

import com.example.meat_home.dto.Profile.UpdateProfileResponse;
import com.example.meat_home.dto.Profile.UpdateProfileRequest;
import com.example.meat_home.entity.Customer;
import com.example.meat_home.entity.Staff;
import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.repository.StaffRepository;
import com.example.meat_home.security.JwtService;
import com.example.meat_home.util.ProfileMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final JwtService jwtService;
    private final CustomerRepository customerRepo;
    private final StaffRepository staffRepo;
    private final PasswordEncoder passwordEncoder;

    public Object updateProfile(String authHeader, UpdateProfileRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        if ("CUSTOMER".equalsIgnoreCase(role)) {
            Customer customer = customerRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            String oldEmail = customer.getEmail();

            // Check password not null and length >= 6
            if (request.getPassword() != null) {
                if (request.getPassword().isBlank() || request.getPassword().length() < 6) {
                    throw new IllegalArgumentException("Password must be at least 6 characters");
                }
            }
            
            // Check email uniqueness
            if (request.getEmail() != null) {
                if (request.getEmail().isBlank()) {
                    throw new IllegalArgumentException("Email cannot be empty");
                }
                if (!request.getEmail().equals(customer.getEmail()) &&
                        customerRepo.existsByEmail(request.getEmail())) {
                    throw new IllegalArgumentException("Email already exists!");
                }
            }

            // Check phone uniqueness
            if (request.getPhone() != null) {
                if (request.getPhone().isBlank()) {
                    throw new IllegalArgumentException("Phone cannot be empty");
                }
                if (!request.getPhone().equals(customer.getPhone()) &&
                        customerRepo.existsByPhone(request.getPhone())) {
                    throw new IllegalArgumentException("Phone already exists!");
                }
            }

            // Apply updates if provided
            if (request.getName() != null) customer.setName(request.getName());
            if (request.getEmail() != null) {
                customer.setEmail(request.getEmail());
                if (!oldEmail.equals(request.getEmail()))
                    customer.setTokenVersion(customer.getTokenVersion() + 1);
            }
            if (request.getPhone() != null) customer.setPhone(request.getPhone());
            if (request.getPassword() != null) {
                customer.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            if (request.getDateOfBirth() != null) customer.setDateOfBirth(request.getDateOfBirth());

            Customer updated = customerRepo.save(customer);

            String newToken = null;
            if (!oldEmail.equals(updated.getEmail())) {
                newToken = jwtService.generateToken(
                        updated.getEmail(),
                        "CUSTOMER",
                        updated.getTokenVersion()
                );
            }

            return new UpdateProfileResponse(ProfileMapper.toDto(updated), newToken);

        } else { 
            // STAFF (ADMIN, CALLCENTER, DELIVERY)
            Staff staff = staffRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Staff not found"));

            String oldEmail = staff.getEmail();
            
            // Check password not null and length >= 6
            if (request.getPassword() != null) {
                if (request.getPassword().isBlank() || request.getPassword().length() < 6) {
                    throw new IllegalArgumentException("Password must be at least 6 characters");
                }
            }
            
            // Check email uniqueness
            if (request.getEmail() != null) {
                if (request.getEmail().isBlank()) {
                    throw new IllegalArgumentException("Email cannot be empty");
                }
                if (!request.getEmail().equals(staff.getEmail()) &&
                        staffRepo.existsByEmail(request.getEmail())) {
                    throw new IllegalArgumentException("Email already exists!");
                }
            }

            // Check phone uniqueness
            if (request.getPhone() != null) {
                if (request.getPhone().isBlank()) {
                    throw new IllegalArgumentException("Phone cannot be empty");
                }
                if (!request.getPhone().equals(staff.getPhone()) &&
                        staffRepo.existsByPhone(request.getPhone())) {
                    throw new IllegalArgumentException("Phone already exists!");
                }
            }

            // Apply updates if provided
            if (request.getName() != null) staff.setName(request.getName());
            if (request.getEmail() != null) {
                staff.setEmail(request.getEmail());
                if (!oldEmail.equals(request.getEmail()))
                    staff.setTokenVersion(staff.getTokenVersion() + 1);
            }
            if (request.getPhone() != null) staff.setPhone(request.getPhone());
            if (request.getPassword() != null) {
                staff.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            if (request.getDateOfBirth() != null) staff.setDateOfBirth(request.getDateOfBirth());

            Staff updated = staffRepo.save(staff);

            String newToken = null;
            if (!oldEmail.equals(updated.getEmail())) {
                newToken = jwtService.generateToken(
                        updated.getEmail(),
                        updated.getRole().name(),
                        updated.getTokenVersion()
                );
            }

            return new UpdateProfileResponse(ProfileMapper.toDto(updated), newToken);
        }
    }
}