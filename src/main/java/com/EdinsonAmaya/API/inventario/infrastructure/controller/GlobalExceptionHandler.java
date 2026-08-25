package com.EdinsonAmaya.API.inventario.infrastructure.controller;

import com.EdinsonAmaya.API.inventario.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomainException(DomainException ex) {
        ProblemDetail problem = new ProblemDetail();
        problem.setType(ex.getType());
        problem.setTitle(getTitleForStatus(ex.getStatus()));
        problem.setStatus(ex.getStatus());
        problem.setDetail(ex.getMessage());
        problem.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(ex.getStatus()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ProblemDetail problem = new ProblemDetail();
        problem.setType(URI.create("https://api-inventario/errors/validation"));
        problem.setTitle("Validation Error");
        problem.setStatus(400);
        problem.setDetail("Request validation failed");
        problem.setErrors(errors);
        problem.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problem = new ProblemDetail();
        problem.setType(URI.create("https://api-inventario/errors/invalid-argument"));
        problem.setTitle("Bad Request");
        problem.setStatus(400);
        problem.setDetail(ex.getMessage());
        problem.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        ProblemDetail problem = new ProblemDetail();
        problem.setType(URI.create("https://api-inventario/errors/internal"));
        problem.setTitle("Internal Server Error");
        problem.setStatus(500);
        problem.setDetail("An unexpected error occurred");
        problem.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(500).body(problem);
    }

    private String getTitleForStatus(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            default -> "Error";
        };
    }

    public static class ProblemDetail {
        private URI type;
        private String title;
        private int status;
        private String detail;
        private List<String> errors;
        private LocalDateTime timestamp;

        public URI getType() { return type; }
        public void setType(URI type) { this.type = type; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }

        public String getDetail() { return detail; }
        public void setDetail(String detail) { this.detail = detail; }

        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
}
