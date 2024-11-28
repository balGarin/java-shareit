package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithCommentsAndBookings;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mvc;

    private ItemDtoWithCommentsAndBookings itemFullDto;

    private ObjectMapper mapper = new ObjectMapper();

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();

        itemFullDto = new ItemDtoWithCommentsAndBookings();
        itemFullDto.setAvailable(true);
        itemFullDto.setId(1);
        itemFullDto.setName("Cup");
        itemFullDto.setDescription("Description");
        itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setId(1);
        itemDto.setName("Cup");
        itemDto.setDescription("Description");
        itemDto.setRequestId(null);
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(any(), any())).thenReturn(itemFullDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemFullDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemFullDto.getName())))
                .andExpect(jsonPath("$.description", is(itemFullDto.getDescription())));
    }

    @Test
    void addItemTest() throws Exception {
        when(itemService.addItem(any(), any())).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
        verify(itemService, times(1)).addItem(any(), any());
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(any(), any(), any())).thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemService, times(1)).updateItem(any(), any(), any());

    }

    @Test
    void getAllItemsByOwnerTest() throws Exception {
        ItemDtoWithCommentsAndBookings itemFullDto2 =
                new ItemDtoWithCommentsAndBookings();
        itemFullDto2.setAvailable(true);
        itemFullDto2.setId(1);
        itemFullDto2.setName("Glovers");
        itemFullDto2.setDescription("Description");
        List<ItemDtoWithCommentsAndBookings> list = new LinkedList<>();
        list.add(itemFullDto);
        list.add(itemFullDto2);
        when(itemService.getAllItemsByOwner(any())).thenReturn(list);
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String items = result.getResponse().getContentAsString();
                    assertTrue(items.contains("Cup"));
                    assertTrue(items.contains("Glovers"));
                });

    }

    @Test
    void searchByQueryTest() throws Exception {
        ItemDto itemDto2 =
                new ItemDto();
        itemDto2.setAvailable(true);
        itemDto2.setId(1);
        itemDto2.setName("Glovers");
        itemDto2.setDescription("Description");
        itemDto2.setRequestId(null);
        List<ItemDto> list = new ArrayList<>();
        list.add(itemDto);
        list.add(itemDto2);
        when(itemService.searchByQuery(any(), any())).thenReturn(list);
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String items = result.getResponse().getContentAsString();
                    assertTrue(items.contains("Cup"));
                    assertTrue(items.contains("Glovers"));
                });
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("textOfComment");
        commentDto.setCreated(null);
        when(itemService.addComment(any(), any(), any())).thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText())));

    }
}