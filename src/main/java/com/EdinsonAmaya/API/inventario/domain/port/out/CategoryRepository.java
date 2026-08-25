package com.EdinsonAmaya.API.inventario.domain.port.out;

import com.EdinsonAmaya.API.inventario.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryRepository {

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    Category save(Category category);

    void deleteById(Long id);
}
