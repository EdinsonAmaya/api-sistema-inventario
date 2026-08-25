package com.EdinsonAmaya.API.inventario.domain.port.out;

import com.EdinsonAmaya.API.inventario.domain.model.InventoryMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryMovementRepository {

    InventoryMovement save(InventoryMovement movement);

    Page<InventoryMovement> findByProductId(Long productId, Pageable pageable);

    Page<InventoryMovement> findAll(Pageable pageable);
}
