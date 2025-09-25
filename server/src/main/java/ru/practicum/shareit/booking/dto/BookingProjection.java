package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

public interface BookingProjection {

    Long getBookingId();

    LocalDateTime getStart();

    LocalDateTime getEnd();
}
