package com.example.meat_home.repository;

import com.example.meat_home.entity.OrderReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderReviewRepository extends JpaRepository<OrderReview, Long> {
    OrderReview findByOrderId(Long orderId);
    List<OrderReview> findByOrderCustomerId(Long customerId);
    List<OrderReview> findByRating(Integer rating);
    boolean existsByOrderId(Long orderId);
}