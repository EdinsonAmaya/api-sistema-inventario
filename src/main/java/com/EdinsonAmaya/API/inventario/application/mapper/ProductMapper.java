package com.EdinsonAmaya.API.inventario.application.mapper;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateProductRequest;
import com.EdinsonAmaya.API.inventario.application.dto.response.ProductResponse;
import com.EdinsonAmaya.API.inventario.domain.model.Product;

import java.time.LocalDateTime;

public class ProductMapper {

    private ProductMapper() {}

    public static Product toDomain(CreateProductRequest request) {
        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCurrentStock(request.getCurrentStock());
        product.setMinimumStock(request.getMinimumStock());
        product.setCategoryId(request.getCategoryId());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }

    public static ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCurrentStock(product.getCurrentStock());
        response.setMinimumStock(product.getMinimumStock());
        response.setCategoryId(product.getCategoryId());
        response.setActive(product.isActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
