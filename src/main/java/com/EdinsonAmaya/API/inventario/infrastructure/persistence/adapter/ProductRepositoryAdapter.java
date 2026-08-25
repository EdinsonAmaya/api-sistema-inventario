package com.EdinsonAmaya.API.inventario.infrastructure.persistence.adapter;

import com.EdinsonAmaya.API.inventario.domain.model.Product;
import com.EdinsonAmaya.API.inventario.domain.port.out.ProductRepository;
import com.EdinsonAmaya.API.inventario.infrastructure.persistence.entity.ProductEntity;
import com.EdinsonAmaya.API.inventario.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    public ProductRepositoryAdapter(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable, Long categoryId, Boolean active) {
        return jpaRepository.findAllFiltered(categoryId, active, pageable)
                .map(this::toDomain);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return jpaRepository.findBySku(sku).map(this::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return jpaRepository.existsByCategoryId(categoryId);
    }

    private Product toDomain(ProductEntity entity) {
        Product product = new Product();
        product.setId(entity.getId());
        product.setSku(entity.getSku());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setPrice(entity.getPrice());
        product.setCurrentStock(entity.getCurrentStock());
        product.setMinimumStock(entity.getMinimumStock());
        product.setCategoryId(entity.getCategoryId());
        product.setActive(entity.isActive());
        product.setCreatedAt(entity.getCreatedAt());
        product.setUpdatedAt(entity.getUpdatedAt());
        return product;
    }

    private ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setSku(product.getSku());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setCurrentStock(product.getCurrentStock());
        entity.setMinimumStock(product.getMinimumStock());
        entity.setCategoryId(product.getCategoryId());
        entity.setActive(product.isActive());
        return entity;
    }
}
