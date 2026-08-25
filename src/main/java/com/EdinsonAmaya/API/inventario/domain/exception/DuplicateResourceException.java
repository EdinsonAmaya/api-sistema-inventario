package com.EdinsonAmaya.API.inventario.domain.exception;

public class DuplicateResourceException extends DomainException {

    public DuplicateResourceException(String resource, String field, Object value) {
        super(String.format("%s already exists with %s: %s", resource, field, value),
                java.net.URI.create("https://api-inventario/errors/duplicate-resource"), 409);
    }
}
