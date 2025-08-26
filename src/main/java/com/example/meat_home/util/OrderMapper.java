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

    public Order toEntity(OrderDto dto) {
        if (dto == null) return null;

        Order order = new Order();
        order.setId(dto.getId());
        order.setCustomer(customerMapper.toEntity(dto.getCustomer()));
        order.setProducts(dto.getProducts()
                .stream()
                .map(productMapper::toEntity)
                .collect(Collectors.toList()));
        order.setOrderStatusChanges(dto.getOrderStatuses()
                .stream()
                .map(orderStatusMapper::toEntity)
                .collect(Collectors.toList()));
        return order;
    }

    public void updateOrderFromDto(OrderDto dto, Order order) {
        if (dto == null) return;

        if (dto.getCustomer() != null)
            order.setCustomer(customerMapper.toEntity(dto.getCustomer()));

        if (dto.getProducts() != null)
            order.setProducts(dto.getProducts().stream()
                    .map(productMapper::toEntity)
                    .toList());

        if (dto.getOrderStatuses() != null)
            order.setOrderStatusChanges(dto.getOrderStatuses().stream()
                    .map(orderStatusMapper::toEntity)
                    .toList());
    }

}

