package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(User user);

    UserDto updateUser(User user, Integer id);

    List<UserDto> getAllUsers();

    UserDto getUserById(Integer id);

    void deleteUser(Integer id);
}
