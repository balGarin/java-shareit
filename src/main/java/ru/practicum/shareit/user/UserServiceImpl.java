package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(User user, Integer id) {

        User outdatedUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        if (user.getName() != null) {
            outdatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            outdatedUser.setEmail(user.getEmail());
        }
        return userMapper.toUserDto(userRepository.save(outdatedUser));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.toUserDto(user.get());
        } else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
