package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final RequestMapper requestMapper;

    @Autowired
    public ItemRequestServiceImpl(UserRepository userRepository,
                                  ItemRepository itemRepository,
                                  ItemRequestRepository itemRequestRepository,
                                  RequestMapper requestMapper) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.requestMapper = requestMapper;
    }

    @Override
    public ItemRequestDto addRequest(Integer userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя не существует"));
        ItemRequest itemRequest = requestMapper.toItemRequest(itemRequestDto, user);
        return requestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestResponseDto> getRequests(Integer userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(userId);
        List<Integer> ids = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        List<Item> items = itemRepository.findAllByRequestIdIn(ids);
        List<ItemRequestResponseDto> itemRequestResponseDtos = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> itemListForRequest = new ArrayList<>();
            for (Item item : items) {
                if (itemRequest.getId().equals(item.getRequest().getId())) {
                    itemListForRequest.add(item);
                }
            }
            itemRequestResponseDtos.add(requestMapper.toItemRequestResponseDto(itemRequest, itemListForRequest));
        }

        itemRequestResponseDtos.sort(Comparator.comparing(ItemRequestResponseDto::getCreated).reversed());
        return itemRequestResponseDtos;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer userId) {
        return  requestMapper.toItemRequestDto(itemRequestRepository.findAllByRequesterIdNot(userId));

    }

    @Override
    public ItemRequestResponseDto getRequest(Integer requestId) {
        ItemRequest itemRequest=itemRequestRepository.findById(requestId)
                .orElseThrow(()->new NotFoundException("Нет такого запроса"));
        List<Item>items = itemRepository.findAllByRequestId(requestId);
        return requestMapper.toItemRequestResponseDto(itemRequest,items);
    }
}
