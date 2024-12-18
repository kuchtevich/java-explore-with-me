package ru.practicum.user.service;

import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.dto.UserDtoOut;

import java.util.List;

public interface UserService {
    List<UserDtoOut> getAllUsers(List<Long> ids, final int from, final int size);

    UserDtoOut createUser(final UserDtoIn userDtoIn);

    void deleteUser(final Long userId);
}

