package ru.practicum.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDtoIn;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.category.service.CategoryService;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDtoOut createCategory(@Valid @RequestBody final CategoryDtoIn categoryDtoIn) {
        return categoryService.createCategory(categoryDtoIn);
    }

    @PatchMapping("/{catId}")
    public CategoryDtoOut updateCategory(@PathVariable @Positive final long catId, @Valid @RequestBody final CategoryDtoIn categoryDtoIn) {
        return categoryService.updateCategory(catId, categoryDtoIn);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable final long catId) {
        categoryService.deleteCategory(catId);
    }
}
