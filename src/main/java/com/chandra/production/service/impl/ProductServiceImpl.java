package com.chandra.production.service.impl;

import com.chandra.production.dto.ProductRequest;
import com.chandra.production.entity.Product;
import com.chandra.production.repository.ProductRepository;
import com.chandra.production.service.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    // =========================
    // CREATE PRODUCT
    // =========================
    @Override
    public Product createProduct(ProductRequest request) {

        Product product = Product.builder()
                .productCode(request.getProductCode())
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        return productRepository.save(product);
    }


    // =========================
    // GET PRODUCT BY ID
    // =========================
    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {

        System.out.println(
                "Product fetched from MySQL"
        );

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: " + id
                        )
                );
    }


    // =========================
    // GET ALL PRODUCTS
    // =========================
    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }


    // =========================
    // UPDATE PRODUCT
    // =========================
    @Override
    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(
            Long id,
            ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: " + id
                        )
                );

        product.setProductCode(
                request.getProductCode()
        );

        product.setName(
                request.getName()
        );

        product.setCategory(
                request.getCategory()
        );

        product.setDescription(
                request.getDescription()
        );

        product.setPrice(
                request.getPrice()
        );

        product.setQuantity(
                request.getQuantity()
        );

        return productRepository.save(product);
    }


    // =========================
    // DELETE PRODUCT
    // =========================
    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: " + id
                        )
                );

        productRepository.delete(product);
    }
}