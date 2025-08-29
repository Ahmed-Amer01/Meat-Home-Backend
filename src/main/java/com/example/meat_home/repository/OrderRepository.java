package com.example.meat_home.repository;

import com.example.meat_home.entity.Order;
import com.example.meat_home.entity.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
    SELECT DISTINCT o FROM Order o
    LEFT JOIN o.orderStatusChanges sc
    WHERE (:customerId IS NULL OR o.customer.id = :customerId)
      AND (:status IS NULL OR sc.status = :status)
      AND ((:startDate IS NULL AND :endDate IS NULL) OR
           (sc.createdAt BETWEEN :startDate AND :endDate))
    ORDER BY o.id DESC
    """)
    List<Order> findWithFilters(
        @Param("customerId") Long customerId,
        @Param("status") StatusEnum status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
