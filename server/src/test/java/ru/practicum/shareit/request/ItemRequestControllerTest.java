package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private ItemRequestDto itemRequestDto;

    private ItemRequestResponseDto itemRequestResponseDto;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("text");
        itemRequestDto.setCreated(null);
        itemRequestResponseDto = new ItemRequestResponseDto();
        itemRequestResponseDto.setDescription("text");
        itemRequestResponseDto.setCreated(null);
    }

    @Test
    void addRequestTest() throws Exception {
        when(itemRequestService.addRequest(any(), any())).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void getRequestsTest() throws Exception {
        ItemRequestResponseDto itemRequestResponseDto1 = new ItemRequestResponseDto();
        itemRequestResponseDto1.setId(itemRequestDto.getId());
        itemRequestResponseDto1.setCreated(itemRequestResponseDto1.getCreated());
        itemRequestResponseDto1.setDescription(itemRequestDto.getDescription());
        List<ItemRequestResponseDto> list = new ArrayList<>();
        list.add(itemRequestResponseDto);
        list.add(itemRequestResponseDto1);
        when(itemRequestService.getRequests(anyInt())).thenReturn(list);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String requests = result.getResponse().getContentAsString();
                    assertTrue(requests.contains("text"));
                });
    }

    @Test
    void getAllRequestsTest() throws Exception {
        ItemRequestDto itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setId(itemRequestDto.getId());
        itemRequestDto1.setCreated(itemRequestDto.getCreated());
        itemRequestDto1.setDescription(itemRequestDto.getDescription());
        List<ItemRequestDto> list = new ArrayList<>();
        list.add(itemRequestDto);
        list.add(itemRequestDto1);
        when(itemRequestService.getAllRequests(anyInt())).thenReturn(list);
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String requests = result.getResponse().getContentAsString();
                    assertTrue(requests.contains("text"));
                });
    }

    @Test
    void getRequestTest() throws Exception {
        when(itemRequestService.getRequest(anyInt())).thenReturn(itemRequestResponseDto);
        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription())));
    }
}