package com.chandra.production.controller;

import com.chandra.production.dto.ProductRequest;
import com.chandra.production.entity.Product;
import com.chandra.production.response.ApiResponse;
import com.chandra.production.service.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // =========================
    // CREATE PRODUCT
    // =========================
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @Valid @RequestBody ProductRequest request) {

        Product product = productService.createProduct(request);

        ApiResponse<Product> response = new ApiResponse<>(
                true,
                "Product created successfully",
                product
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // =========================
    // GET PRODUCT BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(
            @PathVariable Long id) {

        Product product =
                productService.getProductById(id);

        ApiResponse<Product> response = new ApiResponse<>(
                true,
                "Product fetched successfully",
                product
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // GET ALL PRODUCTS
    // =========================
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {

        List<Product> products =
                productService.getAllProducts();

        ApiResponse<List<Product>> response = new ApiResponse<>(
                true,
                "Products fetched successfully",
                products
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // UPDATE PRODUCT
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        Product product =
                productService.updateProduct(id, request);

        ApiResponse<Product> response = new ApiResponse<>(
                true,
                "Product updated successfully",
                product
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // DELETE PRODUCT
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Product deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }
}