package com.EdinsonAmaya.API.inventario.domain.exception;

public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: %s", resource, field, value),
                java.net.URI.create("https://api-inventario/errors/not-found"), 404);
    }
}
