package com.example.meat_home.repository;

import com.example.meat_home.entity.OrderStatusChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderStatusChangeRepository extends JpaRepository<OrderStatusChange, Long> {
    List<OrderStatusChange> findByOrderId(Long orderId);
}
