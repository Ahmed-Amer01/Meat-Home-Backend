package com.example.meat_home.controller;

import com.example.meat_home.entity.Customer;
import com.example.meat_home.service.AdminService;
import com.example.meat_home.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // View all customers
    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<Customer> customers = adminService.getAllCustomers();
            return ResponseEntity.ok(customers); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("Server error", 500));
        }
    }

    // Activate / Deactivate a customer
    @PatchMapping("/customers/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleCustomerStatus(@PathVariable Long id) {
        try {
            Customer customer = adminService.toggleCustomerStatus(id);
            return ResponseEntity.ok(customer); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new ErrorResponse(e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("Server error", 500));
        }
    }
}
