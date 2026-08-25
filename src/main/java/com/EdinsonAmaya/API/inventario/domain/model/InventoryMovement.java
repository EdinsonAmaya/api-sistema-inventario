package com.EdinsonAmaya.API.inventario.domain.model;

import java.time.LocalDateTime;

public class InventoryMovement {

    private Long id;
    private Long productId;
    private MovementType movementType;
    private int quantity;
    private String reason;
    private LocalDateTime createdAt;

    public InventoryMovement() {}

    public InventoryMovement(Long id, Long productId, MovementType movementType, int quantity,
                             String reason, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
