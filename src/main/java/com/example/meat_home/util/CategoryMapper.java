package com.example.meat_home.util;

import com.example.meat_home.dto.Category.CategoryDto;
import com.example.meat_home.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        if(category == null) return null;
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public Category toEntity(CategoryDto dto) {
        if(dto == null) return null;
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;

    }
}
