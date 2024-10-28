package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User saveUser(User user) {
        if (checkEmail(user.getEmail())) {
            throw new ConflictException("Данный email занят");
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь с id - {}", user.getId());
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user, Integer id) {
        if (checkEmail(user.getEmail())) {
            throw new ConflictException("Данный email занят");
        }
        if (users.containsKey(id)) {
            log.info("Пользователь для обновления {}", user);
            User outdated = users.get(id);
            User userUpdated = new User(outdated.getId(),
                    user.getName() != null ? user.getName() : outdated.getName(),
                    user.getEmail() != null ? user.getEmail() : outdated.getEmail());
            log.info("Пользователь - {} обновлен на - \n {}", outdated, userUpdated);
            users.put(userUpdated.getId(), userUpdated);
            return userUpdated;
        } else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void deleteUser(Integer id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    private Integer generateId() {
        Integer id = users.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
        return ++id;
    }

    private boolean checkEmail(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
