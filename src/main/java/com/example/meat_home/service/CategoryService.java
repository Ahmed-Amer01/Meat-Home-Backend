package com.example.meat_home.service;

import com.example.meat_home.dto.Category.CategoryDto;
import com.example.meat_home.entity.Category;
import com.example.meat_home.repository.CategoryRepository;
import com.example.meat_home.util.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /** Get all categories */
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Get a single category by ID */
    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElse(null);
    }

    /** Create a new category */
    public CategoryDto createCategory(CategoryDto dto) {
        if(dto == null ) return null;
        Category cat = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(cat);
        return categoryMapper.toDto(saved);
    }

    /** Delete category by ID (if the category is deleted all products under it will also be deleted) */
    public Boolean deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) return false;
        categoryRepository.deleteById(id); // `cascade = CascadeType.ALL` and `orphanRemoval = true` will handle products under it
        return true;
    }

    /** Delete category by DTO */
    public boolean deleteCategory(CategoryDto dto) {
        if (dto == null || !categoryRepository.existsById(dto.getId())) return false;
        Category cat = categoryMapper.toEntity(dto);
        categoryRepository.delete(cat);
        return true;
    }

    /** Partial update (PATCH) */
    public CategoryDto updateCategoryPatch(Long id, CategoryDto dto) {
        Category cat = categoryRepository.findById(id)
                .orElse(null);
        if (dto == null || cat == null) return null;
        if (dto.getName() != null) cat.setName(dto.getName());
        Category savedCategory = categoryRepository.save(cat);
        return categoryMapper.toDto(savedCategory);
    }

}
