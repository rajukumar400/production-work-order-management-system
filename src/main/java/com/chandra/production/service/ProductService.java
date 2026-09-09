

package com.chandra.production.service;

import com.chandra.production.dto.ProductRequest;
import com.chandra.production.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductRequest request);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    Product updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}