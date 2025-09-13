package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select new ru.practicum.shareit.booking.Booking(b.bookingId, b.start, b.end, b.item," +
            "b.booker, b.bookingStatus) " +
            "from Booking as b " +
            "where ((b.start between  ?1 and ?2) or (b.end between ?1 and ?2)) and b.item.id = ?3 ")
    List<Booking> findBookingByTimeIntersection(LocalDateTime start, LocalDateTime end, long itemId);

    @Query("select new ru.practicum.shareit.booking.Booking(b.bookingId, b.start, b.end, b.item," +
            "b.booker, b.bookingStatus) " +
            "from Booking as b " +
            "where (b.end between ?1 and ?2) and b.item.id = ?3 " +
            "order by b.end desc " +
            "limit 1")
    Booking findLastBookingByItemId(LocalDateTime nowMinusMonth, LocalDateTime now, long itemId);

    @Query("select new ru.practicum.shareit.booking.Booking(b.bookingId, b.start, b.end, b.item," +
            "b.booker, b.bookingStatus) " +
            "from Booking as b " +
            "where (b.start between ?1 and ?2) and b.item.id = ?3 " +
            "order by b.start asc  " +
            "limit 1")
    Booking findNextBookingByItemId(LocalDateTime now, LocalDateTime nowPlusMonth, long itemId);

    List<Booking> findAllByBooker_Id(Long bookerId);

    List<Booking> findAllByBooker_IdAndEndBefore(Long bookerId, LocalDateTime endBefore);

    List<Booking> findAllByBooker_IdAndBookingStatus(Long bookerId, StatusBooking bookingStatus);

    List<Booking> findAllByBooker_IdAndStartAfter(Long bookerId, LocalDateTime startAfter);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime startBefore,
                                                              LocalDateTime endAfter);

    List<Booking> findAllByItemIn(Collection<Item> items);

    List<Booking> findAllByItemInAndEndBefore(Collection<Item> items, LocalDateTime endBefore);

    List<Booking> findAllByItemInAndStartAfter(Collection<Item> items, LocalDateTime startAfter);

    List<Booking> findAllByItemInAndBookingStatus(Collection<Item> items, StatusBooking bookingStatus);

    List<Booking> findAllByItemInAndStartBeforeAndEndAfter(Collection<Item> items, LocalDateTime startBefore,
                                                           LocalDateTime endAfter);

    List<Booking> findAllByBookerAndItem(User booker, Item item);
}
