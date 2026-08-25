package com.EdinsonAmaya.API.inventario.application.mapper;

import com.EdinsonAmaya.API.inventario.application.dto.request.CreateCategoryRequest;
import com.EdinsonAmaya.API.inventario.application.dto.response.CategoryResponse;
import com.EdinsonAmaya.API.inventario.domain.model.Category;

import java.time.LocalDateTime;

public class CategoryMapper {

    private CategoryMapper() {}

    public static Category toDomain(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return category;
    }

    public static CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
