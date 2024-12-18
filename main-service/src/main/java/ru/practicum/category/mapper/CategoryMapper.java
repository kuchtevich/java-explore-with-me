package ru.practicum.category.mapper;

import ru.practicum.category.dto.CategoryDtoIn;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.category.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static Category toCategory(final CategoryDtoIn categoryDtoIn) {

        final Category category = new Category();
        category.setName(categoryDtoIn.getName());

        return category;
    }

    public static CategoryDtoOut toCategoryDtoOut(final Category category) {

        final CategoryDtoOut categoryDtoOut = new CategoryDtoOut();
        categoryDtoOut.setId(category.getId());
        categoryDtoOut.setName(category.getName());

        return categoryDtoOut;
    }

    public static List<CategoryDtoOut> toList(Iterable<Category> categories) {
        final List<CategoryDtoOut> result = new ArrayList<>();

        for (Category category : categories) {
            result.add(toCategoryDtoOut(category));
        }

        return result;
    }
}
