package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        return userClient.getUserById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDto user) {
        return userClient.addUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto user, @PathVariable Integer userId) {
        return userClient.updateUser(user, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userClient.deleteUser(id);
    }
}
