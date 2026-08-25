package com.EdinsonAmaya.API.inventario.application.service;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateProductRequest;
import com.EdinsonAmaya.API.inventario.application.dto.request.UpdateProductRequest;
import com.EdinsonAmaya.API.inventario.application.dto.response.ProductResponse;
import com.EdinsonAmaya.API.inventario.domain.exception.DuplicateResourceException;
import com.EdinsonAmaya.API.inventario.domain.exception.ResourceNotFoundException;
import com.EdinsonAmaya.API.inventario.domain.model.Product;
import com.EdinsonAmaya.API.inventario.domain.port.out.ProductRepository;
import com.EdinsonAmaya.API.inventario.application.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable, Long categoryId, Boolean active) {
        return productRepository.findAll(pageable, categoryId, active)
                .map(ProductMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return ProductMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        productRepository.findBySku(request.getSku())
                .ifPresent(p -> { throw new DuplicateResourceException("Product", "SKU", request.getSku()); });

        Product product = ProductMapper.toDomain(request);
        product.setActive(true);
        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Transactional
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getMinimumStock() != null) product.setMinimumStock(request.getMinimumStock());
        if (request.getCategoryId() != null) product.setCategoryId(request.getCategoryId());

        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Transactional
    public void deactivate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setActive(false);
        productRepository.save(product);
    }
}
