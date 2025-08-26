package com.example.meat_home.dto.Order;

import com.example.meat_home.dto.Customer.CustomerDto;
import com.example.meat_home.dto.OrderStatus.OrderStatusDto;
import com.example.meat_home.dto.Product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private CustomerDto customer;
    private List<ProductDto> products;
    private List<OrderStatusDto> orderStatuses;
}
