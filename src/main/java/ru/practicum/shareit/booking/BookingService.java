package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingCreateDto booking, long bookerId);

    BookingDto bookingApproval(long bookingId, long ownerId, String approved);

    BookingDto getBooking(long bookingId, long userId);

    List<Booking> getUserBookings(String state, long userId);

    List<Booking> getOwnerBookings(String state, long userId);
}
