package com.example.meat_home.controller;

import com.example.meat_home.dto.Review.OrderReviewRequest;
import com.example.meat_home.dto.Review.OrderReviewResponse;
import com.example.meat_home.dto.Review.OrderReviewSummary;
import com.example.meat_home.service.OrderReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderReviewController {

    private final OrderReviewService orderReviewService;

    @PostMapping("/{orderId}/review")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderReviewResponse> createReview(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderReviewRequest request) {
        OrderReviewResponse response = orderReviewService.createReview(orderId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}/review")
    public ResponseEntity<OrderReviewResponse> getReview(@PathVariable Long orderId) {
        OrderReviewResponse response = orderReviewService.getReviewByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

}