package com.example.meat_home.dto.Review;

import lombok.Data;
import java.util.List;

@Data
public class OrderReviewSummary {
    private Double averageRating;
    private Long totalReviews;
    private List<RatingCount> ratingCounts;
}

@Data
class RatingCount {
    private Integer rating;
    private Long count;
}