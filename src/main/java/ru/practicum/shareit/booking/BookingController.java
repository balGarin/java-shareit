package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDtoReturn addBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                       @RequestBody BookingDto dto) {
        return bookingService.addNewBooking(ownerId, dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoReturn changeStatus(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                         @PathVariable Integer bookingId, @RequestParam Boolean approved) {
        return bookingService.changeStatus(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoReturn getBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                       @PathVariable Integer bookingId) {
        return bookingService.getBooking(ownerId, bookingId);
    }

    @GetMapping()
    public List<BookingDtoReturn> getAllBookingByUser(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
    @RequestParam(required = false,defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsByUser(ownerId,state);
    }


}
