package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBooking(BookingCreateDto bookingCreateDto, long bookerId) {
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<Booking> bookings = bookingRepository.findBookingByTimeIntersection(bookingCreateDto.getStart(),
                bookingCreateDto.getEnd(), bookingCreateDto.getItemId());
        if (!bookings.isEmpty()) {
            throw new ValidationException("указанные даты уже заняты");
        }
        if (item.getAvailable().equals(false)) {
            throw new ValidationException("Вещь не доступна для бронирования");
        }
        Booking newBooking = new Booking();
        newBooking.setStart(bookingCreateDto.getStart());
        newBooking.setEnd(bookingCreateDto.getEnd());
        newBooking.setBooker(booker);
        newBooking.setItem(item);
        newBooking.setBookingStatus(StatusBooking.WAITING);
        return BookingDto.mapBookingToDto(bookingRepository.save(newBooking));
    }

    @Override
    public BookingDto bookingApproval(long bookingId, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new ValidationException("User not found"));
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new ValidationException("изменить статус бронирования может только владелец");
        }
        booking.setBookingStatus(approved ? StatusBooking.APPROVED : StatusBooking.REJECTED);
        return BookingDto.mapBookingToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)
                && !Objects.equals(booking.getBooker().getId(), userId)) {
            throw new ValidationException("Просматривать бронирование могут только владелец вещи и создатель брони");
        }
        return BookingDto.mapBookingToDto(booking);
    }

    @Override
    public List<Booking> getUserBookings(String state, long userId) {
        BookingState bookingState = BookingState.valueOf(state.toUpperCase());
        User user = userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found"));
        switch (bookingState) {
            case ALL -> {
                return bookingRepository.findAllByBooker_Id(userId);
            }
            case PAST -> {
                return bookingRepository.findAllByBooker_IdAndEndBefore(userId, LocalDateTime.now());
            }
            case FUTURE -> {
                return bookingRepository.findAllByBooker_IdAndStartAfter(userId, LocalDateTime.now());
            }
            case WAITING -> {
                return bookingRepository.findAllByBooker_IdAndBookingStatus(userId, StatusBooking.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findAllByBooker_IdAndBookingStatus(userId, StatusBooking.REJECTED);
            }
            case CURRENT -> {
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            }
            default -> throw new ValidationException("неверное значение параметра state: " + state);
        }
    }

    @Override
    public List<Booking> getOwnerBookings(String state, long userId) {
        BookingState bookingState = BookingState.valueOf(state.toUpperCase());
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<Item> userItems = itemRepository.findAllByOwner(user);
        if (userItems.isEmpty()) {
            throw new NotFoundException("У вас нет вещей для аренды");
        }
        switch (bookingState) {
            case ALL -> {
                return bookingRepository.findAllByItemIn(userItems);
            }
            case PAST -> {
                return bookingRepository.findAllByItemInAndEndBefore(userItems, LocalDateTime.now());
            }
            case FUTURE -> {
                return bookingRepository.findAllByItemInAndStartAfter(userItems, LocalDateTime.now());
            }
            case WAITING -> {
                return bookingRepository.findAllByItemInAndBookingStatus(userItems, StatusBooking.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findAllByItemInAndBookingStatus(userItems, StatusBooking.REJECTED);
            }
            case CURRENT -> {
                return bookingRepository.findAllByItemInAndStartBeforeAndEndAfter(userItems,
                        LocalDateTime.now(), LocalDateTime.now());
            }
            default -> throw new ValidationException("неверное значение параметра state: " + state);
        }
    }
}
