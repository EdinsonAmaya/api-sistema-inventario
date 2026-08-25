package com.EdinsonAmaya.API.inventario.application.service;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateMovementRequest;
import com.EdinsonAmaya.API.inventario.domain.exception.InsufficientStockException;
import com.EdinsonAmaya.API.inventario.domain.exception.ResourceNotFoundException;
import com.EdinsonAmaya.API.inventario.domain.model.InventoryMovement;
import com.EdinsonAmaya.API.inventario.domain.model.MovementType;
import com.EdinsonAmaya.API.inventario.domain.model.Product;
import com.EdinsonAmaya.API.inventario.domain.port.out.InventoryMovementRepository;
import com.EdinsonAmaya.API.inventario.domain.port.out.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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
class InventoryMovementServiceTest {

    @Mock
    private InventoryMovementRepository movementRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryMovementService movementService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setSku("SKU-001");
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(10.00));
        product.setCurrentStock(50);
        product.setMinimumStock(10);
        product.setCategoryId(1L);
        product.setActive(true);
    }

    @Test
    void shouldCreateEntryMovementAndIncreaseStock() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setProductId(1L);
        request.setMovementType("ENTRY");
        request.setQuantity(20);
        request.setReason("Restocking");

        InventoryMovement savedMovement = new InventoryMovement();
        savedMovement.setId(1L);
        savedMovement.setProductId(1L);
        savedMovement.setMovementType(MovementType.ENTRY);
        savedMovement.setQuantity(20);
        savedMovement.setCreatedAt(LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(InventoryMovement.class))).thenReturn(savedMovement);

        movementService.create(request);

        assertEquals(70, product.getCurrentStock());
        verify(productRepository).save(product);
        verify(movementRepository).save(any(InventoryMovement.class));
    }

    @Test
    void shouldCreateExitMovementAndDecreaseStock() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setProductId(1L);
        request.setMovementType("EXIT");
        request.setQuantity(30);
        request.setReason("Sale");

        InventoryMovement savedMovement = new InventoryMovement();
        savedMovement.setId(1L);
        savedMovement.setProductId(1L);
        savedMovement.setMovementType(MovementType.EXIT);
        savedMovement.setQuantity(30);
        savedMovement.setCreatedAt(LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movementRepository.save(any(InventoryMovement.class))).thenReturn(savedMovement);

        movementService.create(request);

        assertEquals(20, product.getCurrentStock());
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setProductId(1L);
        request.setMovementType("EXIT");
        request.setQuantity(100);
        request.setReason("Large order");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        InsufficientStockException exception = assertThrows(InsufficientStockException.class,
                () -> movementService.create(request));

        assertEquals(50, product.getCurrentStock());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setProductId(999L);
        request.setMovementType("ENTRY");
        request.setQuantity(10);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> movementService.create(request));

        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionForInvalidMovementType() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setProductId(1L);
        request.setMovementType("INVALID");
        request.setQuantity(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class,
                () -> movementService.create(request));
    }
}
