package com.example.meat_home.util;

import com.example.meat_home.dto.Product.ProductDto;
import com.example.meat_home.dto.Product.RawProduct;
import com.example.meat_home.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public ProductDto toDto(Product product) {
        if (product == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDisc(product.getDisc());
        dto.setPrice(product.getPrice());
        dto.setCategory(categoryMapper.toDto(product.getCategory()));
        return dto;
    }

    public RawProduct toSummaryDto(Product product) {
        if (product == null) return null;

        RawProduct dto = new RawProduct();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDisc(product.getDisc());
        dto.setPrice(product.getPrice());
        return dto;
    }
}