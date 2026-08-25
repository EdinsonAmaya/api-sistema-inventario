package com.EdinsonAmaya.API.inventario.application.service;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateProductRequest;
import com.EdinsonAmaya.API.inventario.application.dto.request.UpdateProductRequest;
import com.EdinsonAmaya.API.inventario.domain.exception.DuplicateResourceException;
import com.EdinsonAmaya.API.inventario.domain.exception.ResourceNotFoundException;
import com.EdinsonAmaya.API.inventario.domain.model.Product;
import com.EdinsonAmaya.API.inventario.domain.port.out.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProductSuccessfully() {
        CreateProductRequest request = new CreateProductRequest();
        request.setSku("SKU-001");
        request.setName("Test Product");
        request.setPrice(BigDecimal.valueOf(25.50));
        request.setCurrentStock(100);
        request.setMinimumStock(10);
        request.setCategoryId(1L);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setSku("SKU-001");
        savedProduct.setName("Test Product");
        savedProduct.setPrice(BigDecimal.valueOf(25.50));
        savedProduct.setCurrentStock(100);
        savedProduct.setMinimumStock(10);
        savedProduct.setCategoryId(1L);
        savedProduct.setActive(true);
        savedProduct.setCreatedAt(LocalDateTime.now());
        savedProduct.setUpdatedAt(LocalDateTime.now());

        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        var response = productService.create(request);

        assertEquals("SKU-001", response.getSku());
        assertEquals("Test Product", response.getName());
        assertTrue(response.isActive());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenSkuAlreadyExists() {
        CreateProductRequest request = new CreateProductRequest();
        request.setSku("SKU-001");
        request.setName("Test Product");
        request.setPrice(BigDecimal.valueOf(25.50));
        request.setCurrentStock(100);
        request.setMinimumStock(10);
        request.setCategoryId(1L);

        Product existingProduct = new Product();
        existingProduct.setSku("SKU-001");

        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.of(existingProduct));

        assertThrows(DuplicateResourceException.class, () -> productService.create(request));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Product");
        request.setPrice(BigDecimal.valueOf(30.00));

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setSku("SKU-001");
        existingProduct.setName("Original Product");
        existingProduct.setPrice(BigDecimal.valueOf(25.50));
        existingProduct.setCurrentStock(100);
        existingProduct.setMinimumStock(10);
        existingProduct.setCategoryId(1L);
        existingProduct.setActive(true);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setSku("SKU-001");
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(BigDecimal.valueOf(30.00));
        updatedProduct.setCurrentStock(100);
        updatedProduct.setMinimumStock(10);
        updatedProduct.setCategoryId(1L);
        updatedProduct.setActive(true);
        updatedProduct.setCreatedAt(LocalDateTime.now());
        updatedProduct.setUpdatedAt(LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        var response = productService.update(1L, request);

        assertEquals("Updated Product", response.getName());
        assertEquals(0, BigDecimal.valueOf(30.00).compareTo(response.getPrice()));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnUpdate() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Product");

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.update(999L, request));
    }

    @Test
    void shouldDeactivateProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.deactivate(1L);

        assertFalse(product.isActive());
        verify(productRepository).save(product);
    }
}
