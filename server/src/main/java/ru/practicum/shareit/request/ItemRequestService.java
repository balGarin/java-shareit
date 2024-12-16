package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(Integer userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> getRequests(Integer userId);

    List<ItemRequestDto> getAllRequests(Integer userId);

    ItemRequestResponseDto getRequest(Integer requestId);

}
