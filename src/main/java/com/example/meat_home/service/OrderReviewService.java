package com.example.meat_home.service;

import com.example.meat_home.dto.Review.OrderReviewRequest;
import com.example.meat_home.dto.Review.OrderReviewResponse;
import com.example.meat_home.dto.Review.OrderReviewSummary;
import com.example.meat_home.entity.Order;
import com.example.meat_home.entity.OrderReview;
import com.example.meat_home.repository.OrderRepository;
import com.example.meat_home.repository.OrderReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderReviewService {

    private final OrderReviewRepository orderReviewRepository;
    private final OrderRepository orderRepository;

    public OrderReviewResponse createReview(Long orderId, OrderReviewRequest request) {
        Order order = orderRepository.findByid(orderId);

        if (orderReviewRepository.existsByOrderId(orderId)) {
            throw new IllegalArgumentException("Order already has a review");
        }

        OrderReview review = OrderReview.builder()
                .order(order)
                .customer(order.getCustomer()) // Add this line
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        OrderReview savedReview = orderReviewRepository.save(review);
        return mapToResponse(savedReview);
    }

    public OrderReviewResponse getReviewByOrderId(Long orderId) {
        OrderReview review = orderReviewRepository.findByOrderId(orderId);
        return mapToResponse(review);
    }

    public List<OrderReviewResponse> getReviewsByCustomerId(Long customerId) {
        return orderReviewRepository.findByOrderCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderReviewResponse> getReviewsByRating(Integer rating) {
        return orderReviewRepository.findByRating(rating)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderReviewResponse updateReview(Long orderId, OrderReviewRequest request) {
        OrderReview review = orderReviewRepository.findByOrderId(orderId);

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        OrderReview updatedReview = orderReviewRepository.save(review);
        return mapToResponse(updatedReview);
    }

    public void deleteReview(Long orderId) {
        OrderReview review = orderReviewRepository.findByOrderId(orderId);
        orderReviewRepository.delete(review);
    }

    public OrderReviewSummary getReviewSummary() {
        List<OrderReview> allReviews = orderReviewRepository.findAll();

        if (allReviews.isEmpty()) {
            return new OrderReviewSummary();
        }

        double averageRating = allReviews.stream()
                .mapToInt(OrderReview::getRating)
                .average()
                .orElse(0.0);

        OrderReviewSummary summary = new OrderReviewSummary();
        summary.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
        summary.setTotalReviews((long) allReviews.size());

        return summary;
    }

    private OrderReviewResponse mapToResponse(OrderReview review) {
        OrderReviewResponse response = new OrderReviewResponse();
        response.setId(review.getId());
        response.setOrderId(review.getOrder().getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        response.setCustomerName(review.getOrder().getCustomer().getName());
        return response;
    }
}