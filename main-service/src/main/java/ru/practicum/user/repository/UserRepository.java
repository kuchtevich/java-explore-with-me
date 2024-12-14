package ru.practicum.user.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByIdInOrderByIdAsc(List<Long> ids, PageRequest pageRequest);

    List<User> findByIdIn(final List<Long> userIds,
                          final PageRequest pageRequest);
}
