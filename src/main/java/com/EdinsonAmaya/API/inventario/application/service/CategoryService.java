package com.EdinsonAmaya.API.inventario.application.service;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateCategoryRequest;
import com.EdinsonAmaya.API.inventario.application.dto.response.CategoryResponse;
import com.EdinsonAmaya.API.inventario.domain.exception.DuplicateResourceException;
import com.EdinsonAmaya.API.inventario.domain.exception.ResourceNotFoundException;
import com.EdinsonAmaya.API.inventario.domain.model.Category;
import com.EdinsonAmaya.API.inventario.domain.port.out.CategoryRepository;
import com.EdinsonAmaya.API.inventario.application.mapper.CategoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(CategoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return CategoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        categoryRepository.findByName(request.getName())
                .ifPresent(c -> { throw new DuplicateResourceException("Category", "name", request.getName()); });

        Category category = CategoryMapper.toDomain(request);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toResponse(saved);
    }

    @Transactional
    public CategoryResponse update(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setName(request.getName());
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        Category saved = categoryRepository.save(category);
        return CategoryMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.deleteById(id);
    }
}
