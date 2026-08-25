package com.EdinsonAmaya.API.inventario.application.service;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateMovementRequest;
import com.EdinsonAmaya.API.inventario.application.dto.response.MovementResponse;
import com.EdinsonAmaya.API.inventario.domain.exception.InsufficientStockException;
import com.EdinsonAmaya.API.inventario.domain.exception.ResourceNotFoundException;
import com.EdinsonAmaya.API.inventario.domain.model.InventoryMovement;
import com.EdinsonAmaya.API.inventario.domain.model.MovementType;
import com.EdinsonAmaya.API.inventario.domain.model.Product;
import com.EdinsonAmaya.API.inventario.domain.port.out.InventoryMovementRepository;
import com.EdinsonAmaya.API.inventario.domain.port.out.ProductRepository;
import com.EdinsonAmaya.API.inventario.application.mapper.MovementMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryMovementService {

    private final InventoryMovementRepository movementRepository;
    private final ProductRepository productRepository;

    public InventoryMovementService(InventoryMovementRepository movementRepository,
                                    ProductRepository productRepository) {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MovementResponse create(CreateMovementRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        MovementType type = MovementType.valueOf(request.getMovementType());

        if (type == MovementType.EXIT && !product.hasEnoughStock(request.getQuantity())) {
            throw new InsufficientStockException(product.getId(), request.getQuantity(), product.getCurrentStock());
        }

        if (type == MovementType.ENTRY) {
            product.setCurrentStock(product.getCurrentStock() + request.getQuantity());
        } else {
            product.setCurrentStock(product.getCurrentStock() - request.getQuantity());
        }
        productRepository.save(product);

        InventoryMovement movement = MovementMapper.toDomain(request);
        InventoryMovement saved = movementRepository.save(movement);
        return MovementMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<MovementResponse> findByProductId(Long productId, Pageable pageable) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        return movementRepository.findByProductId(productId, pageable)
                .map(MovementMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<MovementResponse> findAll(Pageable pageable) {
        return movementRepository.findAll(pageable)
                .map(MovementMapper::toResponse);
    }
}
