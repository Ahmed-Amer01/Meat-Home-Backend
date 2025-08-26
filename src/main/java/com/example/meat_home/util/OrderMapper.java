package com.example.meat_home.util;

import com.example.meat_home.dto.Order.OrderDto;
import com.example.meat_home.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final CustomerMapper customerMapper;
    private final ProductMapper productMapper;
    private final OrderStatusMapper orderStatusMapper;

    public OrderDto toDto(Order order) {
        if (order == null) return null;

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomer(customerMapper.toDto(order.getCustomer()));
        dto.setProducts(order.getProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList()));
        dto.setOrderStatuses(order.getOrderStatusChanges()
                .stream()
                .map(orderStatusMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }
}

