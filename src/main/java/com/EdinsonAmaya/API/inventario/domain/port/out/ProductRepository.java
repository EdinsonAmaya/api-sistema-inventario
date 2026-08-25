package com.EdinsonAmaya.API.inventario.domain.port.out;

import com.EdinsonAmaya.API.inventario.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {

    Page<Product> findAll(Pageable pageable, Long categoryId, Boolean active);

    Optional<Product> findById(Long id);

    Optional<Product> findBySku(String sku);

    Product save(Product product);

    void deleteById(Long id);

    boolean existsByCategoryId(Long categoryId);
}
