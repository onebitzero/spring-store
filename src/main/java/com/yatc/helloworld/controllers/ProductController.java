package com.yatc.helloworld.controllers;

import java.net.URI;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.yatc.helloworld.dtos.CreateProductRequest;
import com.yatc.helloworld.dtos.ProductDto;
import com.yatc.helloworld.dtos.UpdateProductRequest;
import com.yatc.helloworld.entities.Category;
import com.yatc.helloworld.entities.Product;
import com.yatc.helloworld.mappers.ProductMapper;
import com.yatc.helloworld.repositories.CategoryRepository;
import com.yatc.helloworld.repositories.ProductRepository;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody CreateProductRequest request,
            UriComponentsBuilder uriComponentsBuilder) {
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElse(null);

        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(request);

        product.setCategory(category);

        productRepository.save(product);

        ProductDto productDto = productMapper.toDto(product);

        URI uri = uriComponentsBuilder
                .path("products/{id}")
                .buildAndExpand(productDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false) Integer categoryId) {
        List<Product> products;

        if (categoryId == null) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findAllByCategoryId(categoryId);
        }

        return products
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);

        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        productMapper.update(request, product);

        product.setCategory(category);

        productRepository.save(product);

        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
