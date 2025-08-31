package com.example.meat_home.dto.Order;

import com.example.meat_home.dto.Product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private ProductDto product;
    private Integer quantity;
}
