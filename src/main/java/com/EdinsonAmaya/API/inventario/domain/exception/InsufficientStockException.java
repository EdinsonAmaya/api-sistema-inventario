package com.EdinsonAmaya.API.inventario.domain.exception;

public class InsufficientStockException extends DomainException {

    public InsufficientStockException(Long productId, int requested, int available) {
        super(String.format("Insufficient stock for product %d: requested %d, available %d",
                productId, requested, available),
                java.net.URI.create("https://api-inventario/errors/insufficient-stock"), 409);
    }
}
