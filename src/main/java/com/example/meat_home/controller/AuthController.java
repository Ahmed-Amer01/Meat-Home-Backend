package com.example.meat_home.controller;

import com.example.meat_home.dto.Auth.LoginRequest;
import com.example.meat_home.dto.Auth.LoginResponse;
import com.example.meat_home.dto.Auth.OtpRequest;
import com.example.meat_home.dto.Auth.ResetPasswordRequest;
import com.example.meat_home.dto.Auth.SignupRequest;
import com.example.meat_home.dto.Error.ErrorResponse;
import com.example.meat_home.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request, 
                                    @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String message = authService.signup(request, authHeader);
            return ResponseEntity.ok(message); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new ErrorResponse(e.getMessage(), 400));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new ErrorResponse(e.getMessage(), 401));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("Server error", 500));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ErrorResponse(e.getMessage(), 401));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse("Server error", 500));
        }
    }

    @PostMapping("/otp-request")
    public ResponseEntity<?> requestReset(@RequestBody OtpRequest request) {
        try {
            authService.requestPasswordReset(request);
            return ResponseEntity.ok("OTP sent to email: " + request.getEmail());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok("Password reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            authService.logout(authHeader);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse("Logout failed", 500));
        }
    }
}