package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private Booking booking;
    private User booker;
    private User owner;
    private Item item;
    private BookingDto bookingDto;

    @BeforeEach
    public void before() {
        booker = new User();
        booker.setId(1L);
        booker.setName("Booker");
        booker.setEmail("booker@booker.com");
        owner = new User();
        owner.setId(2L);
        owner.setName("Owner");
        owner.setEmail("owner@owner.com");
        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Item");
        item.setOwner(owner);
        item.setAvailable(true);
        booking = new Booking();
        booking.setBookingId(5L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(2));
        booking.setBookingStatus(StatusBooking.WAITING);
        bookingDto = BookingDto.mapBookingToDto(booking);
    }

    @Test
    public void testMethodGetBooking() throws Exception {
        when(bookingService.getBooking(booking.getBookingId(), booker.getId())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/" + booking.getBookingId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getBookingId()), Long.class))
                .andExpect(jsonPath("$.start", equalTo(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", equalTo(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(booking.getBookingStatus().toString())))
                .andExpect(jsonPath("$.booker.id", equalTo(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", equalTo(booking.getItem().getId()), Long.class)
                );
    }

    @Test
    public void testGetBookings() throws Exception {
        when(bookingService.getUserBookings("ALL", booker.getId())).thenReturn(List.of(booking));
        when(bookingService.getOwnerBookings("ALL", owner.getId())).thenReturn(List.of(booking));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getBookingId()), Long.class))
                .andExpect(jsonPath("$[0].start", equalTo(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].end", equalTo(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].status", equalTo(booking.getBookingStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", equalTo(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", equalTo(booking.getItem().getId()), Long.class)
                );

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "CURRENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(0))
                );

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "PAST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(0))
                );
    }

    @Test
    public void testMethodGetBookingsForOwner() throws Exception {
        when(bookingService.getOwnerBookings("ALL", owner.getId())).thenReturn(List.of(booking));
        when(bookingService.getOwnerBookings("ALL", owner.getId())).thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getBookingId()), Long.class))
                .andExpect(jsonPath("$[0].start", equalTo(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].end", equalTo(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].status", equalTo(booking.getBookingStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", equalTo(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", equalTo(booking.getItem().getId()), Long.class)
                );

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", "CURRENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(0))
                );

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", "PAST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(0))
                );
    }


    @Test
    public void testMethodCreate() throws Exception {
        BookingCreateDto bookingCreate = new BookingCreateDto();
        bookingCreate.setStart(booking.getStart());
        bookingCreate.setEnd(booking.getEnd());
        bookingCreate.setItemId(booking.getItem().getId());

        when(bookingService.createBooking(bookingCreate, booker.getId())).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId())
                        .content(mapper.writeValueAsString(bookingCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getBookingId()), Long.class))
                .andExpect(jsonPath("$.start", equalTo(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", equalTo(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(booking.getBookingStatus().toString())))
                .andExpect(jsonPath("$.booker.id", equalTo(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", equalTo(booking.getItem().getId()), Long.class)
                );
    }
}
