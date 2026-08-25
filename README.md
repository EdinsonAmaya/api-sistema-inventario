# API de Gestión de Inventario

API REST para la gestión de inventario de productos, desarrollada con Spring Boot siguiendo Arquitectura Hexagonal (Clean Architecture), principios SOLID y buenas prácticas de Clean Code.

## Características

- **Arquitectura Hexagonal**: Separación clara en capas Domain, Application e Infrastructure.
- **CRUD de Productos**: Crear, listar (con paginación y filtros), actualizar y desactivar.
- **CRUD de Categorías**: Gestión completa para clasificar productos.
- **Movimientos de Inventario**: Registro de entradas y salidas con validación de stock y actualización atómica.
- **Manejo de Errores**: `@ControllerAdvice` global con formato RFC 7807 (Problem Details).
- **DTOs con Validaciones**: Usando `jakarta.validation` (`@NotNull`, `@Positive`, etc.).
- **Documentación**: Springdoc OpenAPI (Swagger UI) integrado.
- **Migraciones de BD**: Flyway para gestión de esquema MySQL.
- **Contenerización**: Dockerfile multi-stage y docker-compose para API + MySQL.
- **Pruebas**: Unitarias con JUnit 5 + Mockito y prueba de integración con `@SpringBootTest`.

## Stack Tecnológico

| Componente         | Tecnología                     |
|--------------------|--------------------------------|
| Lenguaje           | Java 17                        |
| Framework          | Spring Boot 3.2.0              |
| Base de Datos      | MySQL 8.0                      |
| ORM                | Spring Data JPA                |
| Migraciones        | Flyway                         |
| Documentación      | Springdoc OpenAPI (Swagger UI) |
| Pruebas            | JUnit 5 + Mockito              |
| Contenerización    | Docker + Docker Compose        |

## Estructura del Proyecto

```
src/main/java/com/EdinsonAmaya/API/inventario/
├── domain/
│   ├── model/                    # Entidades del negocio
│   ├── exception/                # Excepciones personalizadas
│   └── port/out/                 # Interfaces de repositorio
├── application/
│   ├── dto/request/              # DTOs de entrada
│   ├── dto/response/             # DTOs de salida
│   ├── mapper/                   # Mappers DTO <-> Dominio
│   └── service/                  # Casos de uso
└── infrastructure/
    ├── controller/               # Controladores REST + ExceptionHandler
    └── persistence/
        ├── entity/               # Entidades JPA
        ├── repository/           # Repositorios Spring Data
        └── adapter/              # Adaptadores (puerto -> JPA)
```

## Endpoints

### Productos

| Método   | Endpoint                         | Descripción                          |
|----------|----------------------------------|--------------------------------------|
| `GET`    | `/api/v1/products`               | Listar productos (paginado/filtros)  |
| `GET`    | `/api/v1/products/{id}`          | Obtener producto por ID              |
| `POST`   | `/api/v1/products`               | Crear producto                       |
| `PUT`    | `/api/v1/products/{id}`          | Actualizar producto                  |
| `PATCH`  | `/api/v1/products/{id}/deactivate` | Desactivar producto               |

**Filtros de listado:** `?page=0&size=10&categoryId=1&active=true`

### Categorías

| Método   | Endpoint                          | Descripción                |
|----------|-----------------------------------|----------------------------|
| `GET`    | `/api/v1/categories`              | Listar categorías          |
| `GET`    | `/api/v1/categories/{id}`         | Obtener categoría por ID   |
| `POST`   | `/api/v1/categories`              | Crear categoría            |
| `PUT`    | `/api/v1/categories/{id}`         | Actualizar categoría       |
| `DELETE` | `/api/v1/categories/{id}`         | Eliminar categoría         |

### Movimientos de Inventario

| Método   | Endpoint                                    | Descripción                        |
|----------|---------------------------------------------|------------------------------------|
| `POST`   | `/api/v1/movements`                         | Registrar movimiento (entrada/salida) |
| `GET`    | `/api/v1/movements`                         | Listar todos los movimientos       |
| `GET`    | `/api/v1/movements/product/{productId}`     | Movimientos por producto           |

### Documentación

| Endpoint                          | Descripción     |
|-----------------------------------|-----------------|
| `/swagger-ui.html`                | Swagger UI      |
| `/api-docs`                       | OpenAPI JSON    |

## Ejemplos de Uso

### Crear Categoría

```json
POST /api/v1/categories
{
  "name": "Electrónica",
  "description": "Dispositivos electrónicos"
}
```

### Crear Producto

```json
POST /api/v1/products
{
  "sku": "LAP-001",
  "name": "Laptop Gaming",
  "description": "Laptop con procesador Intel i7, 16GB RAM",
  "price": 2499.99,
  "currentStock": 25,
  "minimumStock": 5,
  "categoryId": 1
}
```

### Registrar Entrada de Inventario

```json
POST /api/v1/movements
{
  "productId": 1,
  "movementType": "ENTRY",
  "quantity": 50,
  "reason": "Reabastecimiento proveedor"
}
```

### Registrar Salida de Inventario

```json
POST /api/v1/movements
{
  "productId": 1,
  "movementType": "EXIT",
  "quantity": 10,
  "reason": "Venta a cliente"
}
```

## Ejecución

### Con Docker (Recomendado)

```bash
docker-compose up --build
```

La API estará disponible en `http://localhost:8080` y MySQL en `http://localhost:3306`.

### Con Maven

```bash
# Asegúrate de tener MySQL corriendo en el puerto 3306
mvn spring-boot:run
```

### Variables de Entorno (Docker)

| Variable                        | Valor por defecto                        |
|---------------------------------|------------------------------------------|
| `SPRING_DATASOURCE_URL`        | `jdbc:mysql://mysql:3306/inventario_db`  |
| `SPRING_DATASOURCE_USERNAME`   | `root`                                   |
| `SPRING_DATASOURCE_PASSWORD`   | `root`                                   |

## Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Pruebas unitarias solamente
mvn test -Dtest="ProductServiceTest,InventoryMovementServiceTest"

# Prueba de integración
mvn test -Dtest="InventoryIntegrationTest"
```

## Manejo de Errores

La API retorna errores en formato **RFC 7807 (Problem Details)**:

```json
{
  "type": "https://api-inventario/errors/insufficient-stock",
  "title": "Conflict",
  "status": 409,
  "detail": "Insufficient stock for product 1: requested 100, available 50",
  "timestamp": "2026-08-17T12:00:00"
}
```

| Código | Error                          |
|--------|--------------------------------|
| `400`  | Validación de campos fallida   |
| `404`  | Recurso no encontrado          |
| `409`  | Conflicto (stock, duplicado)   |
| `500`  | Error interno del servidor     |
