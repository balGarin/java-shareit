package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto addUser(User user) {
        return userMapper.toUserDto(userRepository.saveUser(user));
    }

    @Override
    public UserDto updateUser(User user, Integer id) {
        return userMapper.toUserDto(userRepository.updateUser(user, id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAllUsers().stream().map(userMapper::toUserDto).toList();
    }

    @Override
    public UserDto getUserById(Integer id) {
        Optional<User> user = userRepository.findUserById(id);
        if (user.isPresent()) {
            return userMapper.toUserDto(user.get());
        } else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteUser(id);
    }
}
