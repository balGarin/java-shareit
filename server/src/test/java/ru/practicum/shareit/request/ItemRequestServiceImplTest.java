package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void addRequestTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemRequestDto itemRequestDto = getItemRequestDto("Hammer");
        ItemRequestDto returned = itemRequestService.addRequest(userDto.getId(), itemRequestDto);
        assertNotNull(returned.getId(), "Id не верный");
        assertEquals(itemRequestDto.getDescription(), returned.getDescription(), "Описание не совпадает");
    }

    @Test
    void getRequestsTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemRequestDto itemRequestDto = getItemRequestDto("Hammer");
        itemRequestDto = itemRequestService.addRequest(userDto.getId(), itemRequestDto);
        ItemDto itemDto = new ItemDto(
                null, "Cup", "With sign 'Hummer'", true,
                itemRequestDto.getId());
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        List<ItemRequestResponseDto> listRequests = itemRequestService.getRequests(userDto.getId());
        assertEquals(1, listRequests.size(), "Размер не совпадает");
        assertEquals(itemRequestDto.getDescription(), listRequests.get(0).getDescription(), "Не верные данные");
    }

    @Test
    void getAllRequestsTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemRequestDto itemRequestDto = getItemRequestDto("Hammer");
        itemRequestDto = itemRequestService.addRequest(userDto.getId(), itemRequestDto);
        List<ItemRequestDto> listRequests = itemRequestService.getAllRequests(userDto.getId() + 1);
        assertEquals(1, listRequests.size(), "Размер не совпадает");
        assertEquals(itemRequestDto.getDescription(), listRequests.get(0).getDescription(), "Не верные данные");
    }

    @Test
    void getRequestTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemRequestDto itemRequestDto = getItemRequestDto("Hammer");
        itemRequestDto = itemRequestService.addRequest(userDto.getId(), itemRequestDto);
        ItemRequestResponseDto itemRequestDtoReturned = itemRequestService.getRequest(itemRequestDto.getId());
        assertNotNull(itemRequestDtoReturned.getId(), "ID не верный");
        assertEquals(itemRequestDto.getDescription(), itemRequestDtoReturned.getDescription(), "Запрос не совпадет");

    }

    private ItemRequestDto getItemRequestDto(String description) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(null);
        itemRequestDto.setDescription(description);
        return itemRequestDto;
    }
}