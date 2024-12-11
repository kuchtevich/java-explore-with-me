package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserDtoOut;
import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.model.User;
import ru.practicum.error.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDtoOut> getAllUsers(List<Long> ids, final int from, final int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        log.info("Запрос на получение списка пользователей");
        final List<User> users;
        if (Objects.isNull(ids) || ids.isEmpty()) {
            users = userRepository.findAll(pageRequest).getContent();
            log.info("Получен списк всех пользователей");
        } else {
            users = userRepository.findByIdIn(ids, pageRequest);
            log.info("Получен списк пользователей по заданным id");
        }
        return UserMapper.toListDto(users);

    }

    @Override
    public UserDtoOut createUser(final UserDtoIn userDtoIn) {
        if (userRepository.findAll().contains(UserMapper.toUser(userDtoIn))) {
            log.warn("Пользователь с id = {} уже добавлен в список.", userDtoIn.getId());
            throw new DuplicatedException("Этот пользователь уже существует.");
        }

        final User user = userRepository.save(UserMapper.toUser(userDtoIn));
        log.info("Пользователь с id = {} добавлен.", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = {} не существует." + userId));

        userRepository.delete(user);
        log.info("Пользователь с id  = {} удален.", userId);
    }
}
