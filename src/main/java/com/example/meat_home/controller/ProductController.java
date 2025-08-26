package com.example.meat_home.controller;

import com.example.meat_home.dto.Category.CategoryDto;
import com.example.meat_home.dto.Category.CategoryProductsDto;
import com.example.meat_home.dto.Product.CreateProductDto;
import com.example.meat_home.dto.Product.ProductDto;
import com.example.meat_home.service.CategoryService;
import com.example.meat_home.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryProductsDto>> getProductsWithCategories() {
        return ResponseEntity.ok(productService.getCategoriesWithProducts());
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductDto dto, @NotNull UriComponentsBuilder uriBuilder) {
        ProductDto created = productService.createProduct(dto);
        URI uri = uriBuilder.path("/api/products/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProductById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody CreateProductDto productDto) {
        ProductDto updated = productService.updateProductPatch(id, productDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}