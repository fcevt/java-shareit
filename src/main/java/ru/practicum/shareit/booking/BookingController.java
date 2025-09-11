package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingCreateDto booking,
                                    @RequestHeader("X-Sharer-User-Id") long bookerId) {
        log.debug("Try Booking created: {}, booker id: {}", booking, bookerId);
        if (booking.getStart() == null || booking.getEnd() == null
                || booking.getStart().equals(booking.getEnd())
               ) {
            throw new ValidationException("Время начала и конца бронирования должны быть указаны и должны отличаться");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())
                || booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время бронирования не может быть в прошлом");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Время начала бронирования не может быть раньше времени завершения" +
                    " бронирования");
        }
        return bookingService.createBooking(booking, bookerId);
    }

    @PatchMapping("/{id}")
    public BookingDto bookingApproval(@PathVariable long id, @RequestParam Boolean approved,
                                   @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.debug("Booking approval - approved: {}, booking id: {}, owner id: {}", approved, id, ownerId);
        return bookingService.bookingApproval(id, ownerId, approved);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("getBooking - booking id: {}, user id: {}", id, userId);
        return bookingService.getBooking(id, userId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestParam(required = false, defaultValue = "ALL") String state,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("getUserBookings - state: {}, user id: {}", state, userId);
        return bookingService.getUserBookings(state, userId).stream()
                .map(BookingDto::mapBookingToDto)
                .toList();
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestParam(required = false, defaultValue = "ALL") String state,
                                             @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.debug("getOwnerBookings - state: {}, owner id: {}", state, ownerId);
        return bookingService.getOwnerBookings(state, ownerId).stream()
                .map(BookingDto::mapBookingToDto)
                .toList();
    }
}
