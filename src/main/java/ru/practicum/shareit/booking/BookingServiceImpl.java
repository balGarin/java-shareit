package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
                              BookingMapper bookingMapper,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
        this.itemRepository = itemRepository;
    }


    @Override
    public BookingDtoReturn addNewBooking(Integer ownerId, BookingDto dto) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден "));
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (item.getAvailable()) {
            Booking booking = bookingMapper.toBooking(dto, user, item);
            booking.setStatus(Status.WAITING);
            return bookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new IncorrectDataException("Вещь не доступна для бронирования");
        }
    }

    @Override
    public BookingDtoReturn changeStatus(Integer bookingId, Integer ownerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (booking.getItem().getOwner().getId().equals(ownerId)) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
                return bookingMapper.toBookingDto(bookingRepository.save(booking));
            }
            if (!approved && booking.getBooker().getId().equals(ownerId)) {
                booking.setStatus(Status.CANCELED);
                return bookingMapper.toBookingDto(bookingRepository.save(booking));
            } else {
                booking.setStatus(Status.REJECTED);
                return bookingMapper.toBookingDto(bookingRepository.save(booking));
            }
        } else {
            throw new IncorrectDataException("Не владелец вещи");
        }
    }

    @Override
    public BookingDtoReturn getBooking(Integer ownerId, Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (booking.getBooker().getId().equals(ownerId) ||
                booking.getItem().getOwner().getId().equals(ownerId)) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new IncorrectDataException("Нет доступа к этому бронированию");
        }
    }

    @Override
    public List<BookingDtoReturn> getAllBookingsByUser(Integer ownerId) {
        return bookingRepository.findAllByBookerId(ownerId).stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }
}
