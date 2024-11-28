package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserService userService;


    @Test
    void addUserTest() {
        UserDto userDto = makeUserDto(null, "Dmitrii", "mailg@m.ru");
        userDto = userService.addUser(userDto);
        assertNotNull(userDto.getId(), "Id Null");
        assertEquals("mailg@m.ru", userDto.getEmail(), "Данные не совпадают");
        assertEquals("Dmitrii", userDto.getName(), "Данные не совпадают");
    }

    @Test
    void updateUserTest() {
        UserDto userDto = makeUserDto(null, "Dmitrii", "mailg@m.ru");
        userDto = userService.addUser(userDto);
        userDto.setName("newName");
        userDto.setEmail("newEmail@mail.com");
        UserDto userDtoReturn = userService.updateUser(userDto, userDto.getId());
        assertEquals(userDto.getId(), userDtoReturn.getId(), "Id не совпадет");
        assertEquals("newName", userDtoReturn.getName(), "Данные не совпадают");
        assertEquals("newEmail@mail.com", userDtoReturn.getEmail(), "Данные не совпадают");
    }

    @Test
    void getAllUsersTest() {
        UserDto userDto1 = makeUserDto(null, "Dmitrii", "mailg@m.ru");
        UserDto userDto2 = makeUserDto(null, "Nikolay", "mailga@m.ru");
        UserDto userDto3 = makeUserDto(null, "Vasiliy", "mailg@mh.ru");
        List<UserDto> dtos = new ArrayList<>();
        dtos.add(userDto1);
        dtos.add(userDto2);
        dtos.add(userDto3);
        userService.addUser(userDto1);
        userService.addUser(userDto2);
        userService.addUser(userDto3);
        assertEquals(dtos.size(), userService.getAllUsers().size(), "Список возвращается не корректно");
    }

    @Test
    void getUserByIdTest() {
        UserDto userDto = makeUserDto(null, "Dmitrii", "mailg@maa.ru");
        userDto = userService.addUser(userDto);
        UserDto userDtoReturn = userService.getUserById(userDto.getId());
        assertEquals(userDto.getId(), userDtoReturn.getId(), "ID не совпадают");
        assertEquals(userDto.getName(), userDtoReturn.getName(), "Имена не совпадают");
        assertEquals(userDto.getEmail(), userDtoReturn.getEmail(), "Email не совпадает");
    }

    @Test
    void deleteUserTest() {
        UserDto userDto = makeUserDto(null, "Dmitrii", "mailg@maa.ru");
        userDto = userService.addUser(userDto);
        int id = userDto.getId();
        userService.deleteUser(id);
        try {
            userService.getUserById(id);
        } catch (NotFoundException e) {
            ErrorResponse response = new ErrorResponse(e.getMessage());
            assertEquals("Пользователь с таким ID не найден", response.getError());
        }
    }

    private UserDto makeUserDto(Integer id, String name, String email) {
        return new UserDto(id, name, email);
    }
}