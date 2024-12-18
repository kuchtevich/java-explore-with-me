package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDtoIn;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.ConflictException;
import ru.practicum.error.DuplicatedException;
import ru.practicum.error.NotFoundException;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.category.mapper.CategoryMapper;

import java.util.List;
import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDtoOut createCategory(final CategoryDtoIn categoryDtoIn) {
        if (categoryRepository.findAll().contains(CategoryMapper.toCategory(categoryDtoIn))) {
            log.warn("Категория с id = {} уже добавлена.", categoryDtoIn.getId());
            throw new DuplicatedException("Эта категория уже существует.");
        }

        final Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDtoIn));
        log.info("Категория c id {} добавлена.", category.getId());

        return CategoryMapper.toCategoryDtoOut(category);
    }

    @Override
    public CategoryDtoOut updateCategory(final long categoryId, final CategoryDtoIn categoryDtoIn) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категории с id = {} не существует." + categoryId));

        if (Objects.nonNull(categoryDtoIn.getName())) {
            category.setName(categoryDtoIn.getName());
        }

        final Category categoryUpdate = categoryRepository.save(category);
        log.info("Категория с id = {} обновлена.", categoryUpdate.getId());
        return CategoryMapper.toCategoryDtoOut(categoryUpdate);

    }

    @Override
    public void deleteCategory(final long categoryId) {

        if (eventRepository.existsByCategoryId(categoryId)) {
            log.warn("Категория с id {} не может быть удалена.", categoryId);
            throw new ConflictException("Нельзя удалить категорию, с которой связаны события.");
        }

        categoryRepository.deleteById(categoryId);
        log.info("Категория с id  = {} удалена.", categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDtoOut getCategoryById(final long categoryId) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категории с id = {} не существует." + categoryId));

        log.info("Получение категории по id {}.", categoryId);
        return CategoryMapper.toCategoryDtoOut(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDtoOut> getAllCategories(final int from, final int size) {
        final Pageable pageable = PageRequest.of(from / size, size);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        log.info("Получение списка всех категорий.");
        return CategoryMapper.toList(categoryPage.getContent());
    }
}
