package com.EdinsonAmaya.API.inventario.infrastructure.controller;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateMovementRequest;
import com.EdinsonAmaya.API.inventario.application.dto.response.MovementResponse;
import com.EdinsonAmaya.API.inventario.application.service.InventoryMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movements")
@Tag(name = "Inventory Movements", description = "Inventory movement endpoints")
public class InventoryMovementController {

    private final InventoryMovementService movementService;

    public InventoryMovementController(InventoryMovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping
    @Operation(summary = "Register an inventory movement (entry or exit)")
    public ResponseEntity<MovementResponse> create(@Valid @RequestBody CreateMovementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movementService.create(request));
    }

    @GetMapping
    @Operation(summary = "List all movements with pagination")
    public ResponseEntity<Page<MovementResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseEntity.ok(movementService.findAll(pageRequest));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "List movements for a specific product")
    public ResponseEntity<Page<MovementResponse>> findByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseEntity.ok(movementService.findByProductId(productId, pageRequest));
    }
}
