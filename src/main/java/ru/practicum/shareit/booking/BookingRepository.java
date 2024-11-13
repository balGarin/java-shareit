package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerId(Integer bookerId);

    Booking findByItemIdAndStatusAndBookerId(Integer itemId, Status status, Integer bookerId);

    Booking findByItemIdAndEndIsBefore(Integer itemId, LocalDateTime end);

    Booking findByItemIdAndStartIsAfter(Integer itemId, LocalDateTime start);
}
