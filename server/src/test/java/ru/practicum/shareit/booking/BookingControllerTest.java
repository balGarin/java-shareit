package ru.practicum.shareit.booking;


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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController bookingController;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    private BookingDto bookingDto;
    private BookingDtoReturn bookingDtoReturn;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();
        bookingDto = new BookingDto();
        bookingDto.setItemId(1);
        bookingDto.setStart(null);
        bookingDto.setEnd(null);
        bookingDtoReturn = new BookingDtoReturn();
        bookingDtoReturn.setId(1);
        bookingDtoReturn.setStart(bookingDto.getStart());
        bookingDtoReturn.setEnd(bookingDto.getEnd());
        bookingDtoReturn.setBooker(new UserDto(null, "Dmitry", "mail@mail.ru"));
        bookingDtoReturn.setItem(new ItemDto(null, "Cup", "For head", true, null));

    }

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addNewBooking(any(), any())).thenReturn(bookingDtoReturn);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDtoReturn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoReturn.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(bookingDtoReturn.getStart())))
                .andExpect(jsonPath("$.end", is(bookingDtoReturn.getEnd())));
    }

    @Test
    void changeStatusTest() throws Exception {
        when(bookingService.changeStatus(any(), any(), anyBoolean()))
                .thenReturn(bookingDtoReturn);
        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoReturn.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(bookingDtoReturn.getStart())))
                .andExpect(jsonPath("$.end", is(bookingDtoReturn.getEnd())));
        verify(bookingService, times(1)).changeStatus(any(), any(), anyBoolean());
    }

    @Test
    void getBookingTest() throws Exception {
        when(bookingService.getBooking(any(), any()))
                .thenReturn(bookingDtoReturn);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoReturn.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(bookingDtoReturn.getStart())))
                .andExpect(jsonPath("$.end", is(bookingDtoReturn.getEnd())));
        verify(bookingService, times(1)).getBooking(any(), any());
    }

    @Test
    void getAllBookingByUserTest() throws Exception {
        List<BookingDtoReturn> list = new ArrayList<>();
        list.add(bookingDtoReturn);
        when(bookingService.getAllBookingsByUser(any(), any())).thenReturn(list);
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "FUTURE")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookingService, times(1)).getAllBookingsByUser(any(), any());
    }

    @Test
    void getAllBookingByCurrentUserTest() throws Exception {
        List<BookingDtoReturn> list = new ArrayList<>();
        list.add(bookingDtoReturn);
        when(bookingService.getAllBookingByCurrentUser(any(), any())).thenReturn(list);
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "FUTURE")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookingService, times(1)).getAllBookingByCurrentUser(any(), any());
    }
}