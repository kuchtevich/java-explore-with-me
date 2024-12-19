package ru.practicum.user.mapper;

import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.dto.UserDtoOut;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.ArrayList;

public class UserMapper {
    public static UserDtoOut toUserDto(final User user) {
        final UserDtoOut userDtoOut = new UserDtoOut();

        userDtoOut.setId(user.getId());
        userDtoOut.setName(user.getName());
        userDtoOut.setEmail(user.getEmail());

        return userDtoOut;
    }

    public static User toUser(final UserDtoIn userDtoIn) {
        final User user = new User();

        user.setName(userDtoIn.getName());
        user.setEmail(userDtoIn.getEmail());

        return user;
    }

    public static List<UserDtoOut> toListDto(Iterable<User> users) {
        List<UserDtoOut> result = new ArrayList<>();

        for (User user : users) {
            result.add(toUserDto(user));
        }
        return result;
    }
}
