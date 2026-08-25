package com.EdinsonAmaya.API.inventario.infrastructure.persistence.repository;

import com.EdinsonAmaya.API.inventario.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findBySku(String sku);

    @Query("SELECT p FROM ProductEntity p WHERE (:categoryId IS NULL OR p.categoryId = :categoryId) AND (:active IS NULL OR p.active = :active)")
    Page<ProductEntity> findAllFiltered(@Param("categoryId") Long categoryId, @Param("active") Boolean active, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);
}
