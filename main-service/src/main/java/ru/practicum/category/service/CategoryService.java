package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDtoIn;
import ru.practicum.category.dto.CategoryDtoOut;

import java.util.List;

public interface CategoryService {
    CategoryDtoOut createCategory(final CategoryDtoIn categoryDtoIn);

    CategoryDtoOut updateCategory(final long catId, final CategoryDtoIn categoryDtoIn);

    void deleteCategory(final long catId);

    CategoryDtoOut getCategoryById(final long catId);

    List<CategoryDtoOut> getAllCategories(final int from, final int size);
}
