package com.EdinsonAmaya.API.inventario.infrastructure.persistence.repository;

import com.EdinsonAmaya.API.inventario.infrastructure.persistence.entity.InventoryMovementEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementJpaRepository extends JpaRepository<InventoryMovementEntity, Long> {

    Page<InventoryMovementEntity> findByProductId(Long productId, Pageable pageable);
}
