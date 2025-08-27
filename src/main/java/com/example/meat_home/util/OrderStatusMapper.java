package com.example.meat_home.util;

import com.example.meat_home.dto.OrderStatus.OrderStatusDto;
import com.example.meat_home.entity.OrderStatusChange;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusMapper {

    public OrderStatusDto toDto(OrderStatusChange status) {
        if (status == null) return null;

        OrderStatusDto dto = new OrderStatusDto();
        dto.setId(status.getId());
        dto.setCreatedAt(status.getCreatedAt());
        dto.setStatus(status.getStatus());
      dto.setOrderId(status.getOrder().getId()); // omit the parent Order in the child DTO, or only include its id to avoid circular mapping.
        return dto;
    }
}