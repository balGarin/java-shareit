package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto user, Integer id);

    List<UserDto> getAllUsers();

    UserDto getUserById(Integer id);

    void deleteUser(Integer id);
}
