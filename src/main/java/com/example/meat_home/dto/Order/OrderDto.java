package com.example.meat_home.dto.Order;

import com.example.meat_home.dto.Customer.CustomerDto;
import com.example.meat_home.dto.OrderStatus.OrderStatusDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private CustomerDto customer;
    private List<OrderItemDto> products;
    private List<OrderStatusDto> orderStatuses;
}
