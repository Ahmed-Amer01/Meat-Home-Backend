package com.example.meat_home.service;
import com.example.meat_home.dto.Product.CreateProductDto;
import com.example.meat_home.dto.Product.ProductDto;
import com.example.meat_home.entity.Category;
import com.example.meat_home.entity.Product;
import com.example.meat_home.repository.CategoryRepository;
import com.example.meat_home.repository.ProductRepository;
import com.example.meat_home.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    /** Get all products */
    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Get a single product by ID */
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElse(null);
    }

    /** Create a new category */
    public ProductDto createProduct(CreateProductDto dto) {
        if(dto == null ) return null;

        // 1. Find the category to attach the product to it
        Category cat = categoryRepository.findById(dto.getCategory_id()).orElse(null);
        if(cat == null) return null;

        // 2. Build the product via dto and assign the found category to it
        Product product = new Product();
        product.setName(dto.getName());
        product.setDisc(dto.getDisc());
        product.setPrice(dto.getPrice());
        product.setCategory(cat); // attach directly

        // 3. Save to DB
        Product saved = productRepository.save(product);

        // 4. Return the DTO
        return productMapper.toDto(saved);
    }

    /** Delete product by ID  */
    public Boolean deleteProductById(Long id) {
        if (!productRepository.existsById(id)) return false;
        productRepository.deleteById(id);
        return true;
    }

    /** Partial update (PATCH) */
    public ProductDto updateProductPatch(Long id, CreateProductDto dto) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (dto == null || product == null) return null;

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDisc() != null) product.setDisc(dto.getDisc());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());

        // if the dto category exist and its id also exist, fetch the modified category from the DB and assign it to the product
        if (dto.getCategory_id() != null) {
            Category category = categoryRepository.findById(dto.getCategory_id())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }
}
