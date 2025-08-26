package com.example.meat_home.service;

import com.example.meat_home.dto.Login.LoginRequest;
import com.example.meat_home.dto.Login.LoginResponse;
import com.example.meat_home.dto.Signup.SignupRequest;
import com.example.meat_home.entity.Customer;
import com.example.meat_home.entity.Staff;
import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.repository.StaffRepository;
import com.example.meat_home.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepo;
    private final StaffRepository staffRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Signup
    public String signup(SignupRequest request, String authHeader) {
        if ("customer".equalsIgnoreCase(request.getType())) {
            if (customerRepo.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Customer email already exists!");
            }
            Customer customer = Customer.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .dateOfBirth(request.getDateOfBirth())
                    .isActive(true)
                    .build();
            customerRepo.save(customer);
            return "Customer created successfully: " + customer.getEmail();

        } else if ("staff".equalsIgnoreCase(request.getType())) {

            if (staffRepo.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Staff email already exists!");
            }

            // Convert role to uppercase to match enum
            Staff.Role role;
            try {
                role = Staff.Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid staff role: " + request.getRole());
            }

            // Admin creation check
            if (role == Staff.Role.ADMIN) {
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new RuntimeException("Unauthorized: Admin token required to create another Admin");
                }

                String token = authHeader.substring(7);
                String tokenRole = jwtService.extractRole(token);

                if (!"ADMIN".equalsIgnoreCase(tokenRole)) {
                    throw new RuntimeException("Unauthorized: Only Admin can create another Admin");
                }
            }

            Staff staff = Staff.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .dateOfBirth(request.getDateOfBirth())
                    .role(role)
                    .isActive(true)
                    .build();

            staffRepo.save(staff);
            return "Staff created successfully with role: " + staff.getRole();
        }

        throw new IllegalArgumentException("Invalid type, must be 'customer' or 'staff'");
    }

    // Login
    public LoginResponse login(LoginRequest request) {
        // Staff login
        Staff staff = staffRepo.findByEmail(request.getEmail()).orElse(null);
        if (staff != null && passwordEncoder.matches(request.getPassword(), staff.getPassword())) {
            // Increment token version to invalidate old tokens
            staff.setTokenVersion(staff.getTokenVersion() + 1);
            staffRepo.save(staff);

            String token = jwtService.generateToken(
                staff.getEmail(),
                staff.getRole().name().toUpperCase(),
                staff.getTokenVersion() // send the new token version
            );
            return new LoginResponse(token);
        }

        // Customer login
        Customer customer = customerRepo.findByEmail(request.getEmail()).orElse(null);
        if (customer != null && passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            // Increment token version to invalidate old tokens
            customer.setTokenVersion(customer.getTokenVersion() + 1);
            customerRepo.save(customer);
            
            String token = jwtService.generateToken(
                customer.getEmail(),
                "CUSTOMER",
                customer.getTokenVersion()
            );
            return new LoginResponse(token);
        }

        throw new RuntimeException("Invalid email or password");
    }

    // Logout
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);

            if ("CUSTOMER".equalsIgnoreCase(role)) {
                Customer customer = customerRepo.findByEmail(email).orElseThrow();
                customer.setTokenVersion(customer.getTokenVersion() + 1);
                customerRepo.save(customer);
            } else {
                Staff staff = staffRepo.findByEmail(email).orElseThrow();
                staff.setTokenVersion(staff.getTokenVersion() + 1);
                staffRepo.save(staff);
            }
        }
    }
}