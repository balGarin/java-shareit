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
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              BookingMapper bookingMapper, ItemRepository itemRepository, ItemMapper itemMapper) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
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
    public List<BookingDtoReturn> getAllBookingsByUser(Integer ownerId, String state) {
        State stateEnum;

        try {
            stateEnum = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IncorrectDataException("Не верный параметр запроса");
        }
        switch (stateEnum) {
            case PAST:
                return bookingMapper.toBookingDto(bookingRepository
                                .findAllByBookerIdAndEndIsBefore(ownerId, LocalDateTime.now())).stream()
                        .sorted(Comparator.comparing(BookingDtoReturn::getEnd).reversed()).collect(Collectors.toList());
            case FUTURE:
                return bookingMapper.toBookingDto(bookingRepository
                                .findAllByBookerIdAndStartIsAfter(ownerId, LocalDateTime.now())).stream()
                        .sorted(Comparator.comparing(BookingDtoReturn::getEnd).reversed()).collect(Collectors.toList());
            case CURRENT:
                return bookingMapper.toBookingDto(bookingRepository
                                .findAllByStartBeforeAndEndAfterAndBookerId(LocalDateTime.now(),
                                        LocalDateTime.now(), ownerId)).stream()
                        .sorted(Comparator.comparing(BookingDtoReturn::getEnd).reversed()).collect(Collectors.toList());
            case WAITING:
                return bookingMapper.toBookingDto(bookingRepository
                                .findAllByBookerIdAndStatusIs(ownerId, Status.WAITING)).stream()
                        .sorted(Comparator.comparing(BookingDtoReturn::getEnd).reversed()).collect(Collectors.toList());
            case REJECTED:
                return bookingMapper.toBookingDto(bookingRepository
                                .findAllByBookerIdAndStatusIs(ownerId, Status.REJECTED)).stream()
                        .sorted(Comparator.comparing(BookingDtoReturn::getEnd).reversed()).collect(Collectors.toList());
        }
        return bookingMapper.toBookingDto(bookingRepository.findAllByBookerId(ownerId)).stream()
                .sorted(Comparator.comparing(BookingDtoReturn::getEnd).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoReturn> getAllBookingByCurrentUser(Integer userId, String stateParam) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        State state = State.valueOf(stateParam);
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        if (items.size() < 1) {
            throw new IncorrectDataException("У текущего пользователя нет выложенных вещей");
        }
        List<Integer> ids = items.stream()
                .map(Item::getId).collect(Collectors.toList());
        List<Booking> bookings = bookingRepository.findAllByItemIdIn(ids);
        switch (state) {
            case PAST:
                return bookingMapper.toBookingDto(bookings.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList()));
            case FUTURE:
                return bookingMapper.toBookingDto(bookings.stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList()));
            case CURRENT:
                return bookingMapper.toBookingDto(bookings.stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList()));
            case WAITING:
                return bookingMapper.toBookingDto(bookings.stream()
                        .filter(booking -> booking.getStatus().toString().equals(State.WAITING.toString()))
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList()));
            case REJECTED:
                return bookingMapper.toBookingDto(bookings.stream()
                        .filter(booking -> booking.getStatus().toString().equals(State.REJECTED.toString()))
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList()));
        }
        return bookingMapper.toBookingDto(bookings.stream()
                .sorted(Comparator.comparing(Booking::getEnd)).collect(Collectors.toList()));
    }
}
