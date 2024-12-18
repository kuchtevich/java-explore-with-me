package ru.practicum.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/categories")
public class CategoryControllerForAll {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public CategoryDtoOut getCategoryById(@PathVariable @Positive final long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping
    public List<CategoryDtoOut> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                 @RequestParam(defaultValue = "10") @Positive final int size) {
        return categoryService.getAllCategories(from, size);
    }
}
