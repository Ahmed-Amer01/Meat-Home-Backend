package com.example.meat_home.dto.Review;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderReviewResponse {
    private Long id;
    private Long orderId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String customerName;
}