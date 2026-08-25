package com.EdinsonAmaya.API.inventario.infrastructure.persistence.adapter;

import com.EdinsonAmaya.API.inventario.domain.model.InventoryMovement;
import com.EdinsonAmaya.API.inventario.domain.model.MovementType;
import com.EdinsonAmaya.API.inventario.domain.port.out.InventoryMovementRepository;
import com.EdinsonAmaya.API.inventario.infrastructure.persistence.entity.InventoryMovementEntity;
import com.EdinsonAmaya.API.inventario.infrastructure.persistence.repository.InventoryMovementJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class InventoryMovementRepositoryAdapter implements InventoryMovementRepository {

    private final InventoryMovementJpaRepository jpaRepository;

    public InventoryMovementRepositoryAdapter(InventoryMovementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        InventoryMovementEntity entity = toEntity(movement);
        InventoryMovementEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Page<InventoryMovement> findByProductId(Long productId, Pageable pageable) {
        return jpaRepository.findByProductId(productId, pageable).map(this::toDomain);
    }

    @Override
    public Page<InventoryMovement> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(this::toDomain);
    }

    private InventoryMovement toDomain(InventoryMovementEntity entity) {
        InventoryMovement movement = new InventoryMovement();
        movement.setId(entity.getId());
        movement.setProductId(entity.getProductId());
        movement.setMovementType(MovementType.valueOf(entity.getMovementType()));
        movement.setQuantity(entity.getQuantity());
        movement.setReason(entity.getReason());
        movement.setCreatedAt(entity.getCreatedAt());
        return movement;
    }

    private InventoryMovementEntity toEntity(InventoryMovement movement) {
        InventoryMovementEntity entity = new InventoryMovementEntity();
        entity.setId(movement.getId());
        entity.setProductId(movement.getProductId());
        entity.setMovementType(movement.getMovementType().name());
        entity.setQuantity(movement.getQuantity());
        entity.setReason(movement.getReason());
        return entity;
    }
}
