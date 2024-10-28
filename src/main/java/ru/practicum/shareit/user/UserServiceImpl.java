package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapperDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(User user) {
        return UserMapperDto.toUserDto(userRepository.saveUser(user));
    }

    @Override
    public UserDto updateUser(User user, Integer id) {
        return UserMapperDto.toUserDto(userRepository.updateUser(user, id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAllUsers().stream().map(UserMapperDto::toUserDto).toList();
    }

    @Override
    public UserDto getUserById(Integer id) {
        Optional<User> user = userRepository.findUserById(id);
        if (user.isPresent()) {
            return UserMapperDto.toUserDto(user.get());
        } else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteUser(id);
    }
}
