package com.EdinsonAmaya.API.inventario.domain.exception;

import java.net.URI;

public class DomainException extends RuntimeException {

    private final URI type;
    private final int status;

    public DomainException(String message, URI type, int status) {
        super(message);
        this.type = type;
        this.status = status;
    }

    public URI getType() { return type; }
    public int getStatus() { return status; }
}
