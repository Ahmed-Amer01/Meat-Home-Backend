package com.example.meat_home.service;

import com.example.meat_home.dto.Auth.LoginRequest;
import com.example.meat_home.dto.Auth.LoginResponse;
import com.example.meat_home.dto.Auth.OtpRequest;
import com.example.meat_home.dto.Auth.ResetPasswordRequest;
import com.example.meat_home.dto.Auth.SignupRequest;
import com.example.meat_home.entity.Customer;
import com.example.meat_home.entity.PasswordResetToken;
import com.example.meat_home.entity.Staff;
import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.repository.PasswordResetTokenRepository;
import com.example.meat_home.repository.StaffRepository;
import com.example.meat_home.security.JwtService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepo;
    private final StaffRepository staffRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
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

    // Request OTP to reset password
    public void requestPasswordReset(OtpRequest request) {
        String email = request.getEmail();

        // check if user exists (customer or staff)
        boolean exists = customerRepo.existsByEmail(email) || staffRepo.existsByEmail(email);
        if (!exists) {
            throw new RuntimeException("No account found with this email");
        }

        // generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // check if OTP already exists for this email
        PasswordResetToken token = tokenRepo.findByEmail(email).orElse(null);

        if (token != null) {
            // update existing token
            token.setOtp(otp);
            token.setExpiry(LocalDateTime.now().plusMinutes(5));
            token.setUsed(false);
        } else {
            // create new token
            token = PasswordResetToken.builder()
                    .email(email)
                    .otp(otp)
                    .expiry(LocalDateTime.now().plusMinutes(5))
                    .used(false)
                    .build();
        }

        tokenRepo.save(token);

        // Send real email
        emailService.sendOtp(email, otp);
    }

    // Reset password using OTP
    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        // Find latest OTP for this email
        PasswordResetToken token = tokenRepo.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        // Check if already used
        if (token.isUsed()) {
            throw new RuntimeException("OTP already used");
        }

        // Check expiry
        if (token.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        // Mark token as used
        token.setUsed(true);
        tokenRepo.save(token);

        // Update password in either staff or customer
        Customer customer = customerRepo.findByEmail(email).orElse(null);
        if (customer != null) {
            customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
            customerRepo.save(customer);
            return;
        }

        Staff staff = staffRepo.findByEmail(email).orElse(null);
        if (staff != null) {
            staff.setPassword(passwordEncoder.encode(request.getNewPassword()));
            staffRepo.save(staff);
            return;
        }

        throw new RuntimeException("Account not found for email: " + email);
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