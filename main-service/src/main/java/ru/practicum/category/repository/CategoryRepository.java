package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIdInOrderById(List<Long> ids, PageRequest pageRequest);
}
