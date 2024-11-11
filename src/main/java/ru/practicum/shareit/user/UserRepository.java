package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User saveUser(User user);

    User updateUser(User user, Integer id);

    List<User> findAllUsers();

    Optional<User> findUserById(Integer id);

    void deleteUser(Integer id);
}
