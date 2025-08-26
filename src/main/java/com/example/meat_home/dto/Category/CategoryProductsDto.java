package com.example.meat_home.dto.Category;

import com.example.meat_home.dto.Product.RawProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryProductsDto {
    private String name;
    private List<RawProduct> products;
}
