package com.EdinsonAmaya.API.inventario.application.mapper;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateMovementRequest;
import com.EdinsonAmaya.API.inventario.application.dto.response.MovementResponse;
import com.EdinsonAmaya.API.inventario.domain.model.InventoryMovement;
import com.EdinsonAmaya.API.inventario.domain.model.MovementType;

import java.time.LocalDateTime;

public class MovementMapper {

    private MovementMapper() {}

    public static InventoryMovement toDomain(CreateMovementRequest request) {
        InventoryMovement movement = new InventoryMovement();
        movement.setProductId(request.getProductId());
        movement.setMovementType(MovementType.valueOf(request.getMovementType()));
        movement.setQuantity(request.getQuantity());
        movement.setReason(request.getReason());
        movement.setCreatedAt(LocalDateTime.now());
        return movement;
    }

    public static MovementResponse toResponse(InventoryMovement movement) {
        MovementResponse response = new MovementResponse();
        response.setId(movement.getId());
        response.setProductId(movement.getProductId());
        response.setMovementType(movement.getMovementType().name());
        response.setQuantity(movement.getQuantity());
        response.setReason(movement.getReason());
        response.setCreatedAt(movement.getCreatedAt());
        return response;
    }
}
