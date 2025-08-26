package com.example.meat_home.dto.Product;

import com.example.meat_home.dto.Category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private Double price;
    private String disc;
    private String name;
    private CategoryDto category;
}
