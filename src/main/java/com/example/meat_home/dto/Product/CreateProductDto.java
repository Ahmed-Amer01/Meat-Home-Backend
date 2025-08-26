package com.example.meat_home.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto {
    private Double price;
    private String disc;
    private String name;
    private Long category_id;
}
