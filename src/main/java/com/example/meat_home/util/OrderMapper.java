package com.example.meat_home.util;

import com.example.meat_home.dto.Order.OrderDto;
import com.example.meat_home.dto.Order.OrderItemDto;
import com.example.meat_home.entity.Order;
import com.example.meat_home.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
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

        // ✅ Group products and count quantities
        Map<Product, Long> grouped = order.getProducts().stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));

        // ✅ Convert to OrderItemDto
        List<OrderItemDto> items = grouped.entrySet().stream()
                .map(entry -> new OrderItemDto(
                        productMapper.toDto(entry.getKey()),
                        entry.getValue().intValue()
                ))
                .collect(Collectors.toList());

        dto.setProducts(items);

        dto.setOrderStatuses(order.getOrderStatusChanges()
                .stream()
                .map(orderStatusMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public List<OrderDto> toDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

