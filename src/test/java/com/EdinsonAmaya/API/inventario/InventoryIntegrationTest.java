package com.EdinsonAmaya.API.inventario;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateCategoryRequest;
import com.EdinsonAmaya.API.inventario.application.dto.request.CreateMovementRequest;
import com.EdinsonAmaya.API.inventario.application.dto.request.CreateProductRequest;
import com.EdinsonAmaya.API.inventario.application.dto.response.CategoryResponse;
import com.EdinsonAmaya.API.inventario.application.dto.response.MovementResponse;
import com.EdinsonAmaya.API.inventario.application.dto.response.ProductResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static Long categoryId;
    private static Long productId;

    @Test
    @Order(1)
    void shouldCreateCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Electronics");
        request.setDescription("Electronic devices");

        ResponseEntity<CategoryResponse> response = restTemplate.postForEntity(
                "/api/v1/categories", request, CategoryResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Electronics", response.getBody().getName());
        categoryId = response.getBody().getId();
    }

    @Test
    @Order(2)
    void shouldCreateProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setSku("ELEC-001");
        request.setName("Laptop");
        request.setDescription("Gaming laptop");
        request.setPrice(BigDecimal.valueOf(999.99));
        request.setCurrentStock(20);
        request.setMinimumStock(5);
        request.setCategoryId(categoryId);

        ResponseEntity<ProductResponse> response = restTemplate.postForEntity(
                "/api/v1/products", request, ProductResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ELEC-001", response.getBody().getSku());
        assertEquals(20, response.getBody().getCurrentStock());
        assertTrue(response.getBody().isActive());
        productId = response.getBody().getId();
    }

    @Test
    @Order(3)
    void shouldCreateEntryMovementAndIncreaseStock() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setProductId(productId);
        request.setMovementType("ENTRY");
        request.setQuantity(10);
        request.setReason("Restocking");

        ResponseEntity<MovementResponse> response = restTemplate.postForEntity(
                "/api/v1/movements", request, MovementResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ENTRY", response.getBody().getMovementType());
        assertEquals(10, response.getBody().getQuantity());

        ResponseEntity<ProductResponse> productResponse = restTemplate.getForEntity(
                "/api/v1/products/" + productId, ProductResponse.class);
        assertEquals(30, productResponse.getBody().getCurrentStock());
    }

    @Test
    @Order(4)
    void shouldCreateExitMovementAndDecreaseStock() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setProductId(productId);
        request.setMovementType("EXIT");
        request.setQuantity(15);
        request.setReason("Sale");

        ResponseEntity<MovementResponse> response = restTemplate.postForEntity(
                "/api/v1/movements", request, MovementResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<ProductResponse> productResponse = restTemplate.getForEntity(
                "/api/v1/products/" + productId, ProductResponse.class);
        assertEquals(15, productResponse.getBody().getCurrentStock());
    }

    @Test
    @Order(5)
    void shouldRejectExitWhenInsufficientStock() {
        CreateMovementRequest request = new CreateMovementRequest();
        request.setProductId(productId);
        request.setMovementType("EXIT");
        request.setQuantity(1000);
        request.setReason("Large order");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/movements", request, Map.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @Order(6)
    void shouldListProductsWithPagination() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "/api/v1/products?page=0&size=10", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("totalElements"));
    }
}
