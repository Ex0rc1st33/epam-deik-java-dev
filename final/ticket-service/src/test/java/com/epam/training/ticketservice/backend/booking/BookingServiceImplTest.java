package com.epam.training.ticketservice.backend.booking;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.backend.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.backend.booking.service.BookingService;
import com.epam.training.ticketservice.backend.booking.service.impl.BookingServiceImpl;
import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.pricecomponent.service.PriceComponentService;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.backend.screening.service.ScreeningService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class BookingServiceImplTest {

    private static final Movie MOVIE_ENTITY = new Movie("Lord of the Rings", "fantasy", 150, null);
    private static final Room ROOM_ENTITY = new Room("Room1", 20, 30, null);
    private static final Screening SCREENING_ENTITY = new Screening("Lord of the Rings",
            "Room1",
            "2021-03-15 12:00",
            null);
    private static final Booking BOOKING_ENTITY = new Booking("Lord of the Rings",
            "Room1",
            "2021-03-15 12:00",
            "5,5",
            "sanyi",
            1500);
    private static final BookingDto BOOKING_DTO = BookingDto.builder()
            .withMovieTitle("Lord of the Rings")
            .withRoomName("Room1")
            .withStartedAt("2021-03-15 12:00")
            .withSeats("5,5")
            .withUser("sanyi")
            .build();

    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final RoomService roomService = mock(RoomService.class);
    private final ScreeningService screeningService = mock(ScreeningService.class);
    private final PriceComponentService priceComponentService = mock(PriceComponentService.class);
    private final BookingService underTest = new BookingServiceImpl(bookingRepository,
            roomService,
            screeningService,
            priceComponentService);

    @Test
    public void testCreateBookingShouldCallBookingRepositoryAndReturnCorrectMessageWhenGivenBookingValid() {
        //Given
        Booking booking = new Booking("Lord of the Rings",
                "Room1",
                "2021-03-15 12:00",
                "6,6",
                "karcsi",
                1500);
        when(screeningService.getMovieByTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        when(bookingRepository.save(BOOKING_ENTITY)).thenReturn(BOOKING_ENTITY);
        when(bookingRepository.findAllByMovieTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt()))
                .thenReturn(List.of(booking));
        when(roomService.getRoomByName(BOOKING_DTO.getRoomName())).thenReturn(Optional.of(ROOM_ENTITY));
        String expected = "Seats booked: (5,5); the price for this booking is 1500 HUF";

        //When
        String actual = underTest.createBooking(BOOKING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(screeningService).getMovieByTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt());
        verify(bookingRepository).save(BOOKING_ENTITY);
        verify(bookingRepository).findAllByMovieTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt());
        verify(roomService).getRoomByName(BOOKING_DTO.getRoomName());
    }

    @Test
    public void testCreateBookingShouldThrowIllegalStateExceptionWhenGivenScreeningDoesNotExist() {
        //Given
        when(screeningService.getMovieByTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt()))
                .thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest.createBooking(BOOKING_DTO));
        verify(screeningService).getMovieByTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt());
    }

    @Test
    public void testCreateBookingShouldReturnCorrectErrorMessageWhenSeatAlreadyTaken() {
        //Given
        Booking booking = new Booking("Lord of the Rings",
                "Room1",
                "2021-03-15 12:00",
                "5,5",
                "karcsi",
                1500);
        when(screeningService.getMovieByTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        when(bookingRepository.findAllByMovieTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt()))
                .thenReturn(List.of(booking));
        String expected = "Seat (5,5) is already taken";

        //When
        String actual = underTest.createBooking(BOOKING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(screeningService).getMovieByTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt());
        verify(bookingRepository).findAllByMovieTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt());
    }

    @Test
    public void testCreateBookingShouldReturnCorrectErrorMessageWhenSeatDoesNotExist() {
        //Given
        BookingDto bookingDto1 = BookingDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 12:00")
                .withSeats("0,1")
                .withUser("sanyi")
                .build();
        BookingDto bookingDto2 = BookingDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 12:00")
                .withSeats("1,0")
                .withUser("sanyi")
                .build();
        BookingDto bookingDto3 = BookingDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 12:00")
                .withSeats("30,10")
                .withUser("sanyi")
                .build();
        BookingDto bookingDto4 = BookingDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 12:00")
                .withSeats("10,40")
                .withUser("sanyi")
                .build();
        when(screeningService.getMovieByTitleAndRoomNameAndStartedAt(bookingDto1.getMovieTitle(),
                bookingDto1.getRoomName(),
                bookingDto1.getStartedAt()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        when(bookingRepository.findAllByMovieTitleAndRoomNameAndStartedAt(bookingDto1.getMovieTitle(),
                bookingDto1.getRoomName(),
                bookingDto1.getStartedAt()))
                .thenReturn(List.of());
        when(roomService.getRoomByName(bookingDto1.getRoomName())).thenReturn(Optional.of(ROOM_ENTITY));
        String expected1 = "Seat (0,1) does not exist in this room";
        String expected2 = "Seat (1,0) does not exist in this room";
        String expected3 = "Seat (30,10) does not exist in this room";
        String expected4 = "Seat (10,40) does not exist in this room";

        //When
        String actual1 = underTest.createBooking(bookingDto1);
        String actual2 = underTest.createBooking(bookingDto2);
        String actual3 = underTest.createBooking(bookingDto3);
        String actual4 = underTest.createBooking(bookingDto4);

        //Then
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
        assertEquals(expected4, actual4);
        verify(screeningService, times(4)).getMovieByTitleAndRoomNameAndStartedAt(bookingDto1.getMovieTitle(),
                bookingDto1.getRoomName(),
                bookingDto1.getStartedAt());
        verify(bookingRepository, times(4)).findAllByMovieTitleAndRoomNameAndStartedAt(bookingDto1.getMovieTitle(),
                bookingDto1.getRoomName(),
                bookingDto1.getStartedAt());
        verify(roomService, times(4)).getRoomByName(bookingDto1.getRoomName());
    }

    @Test
    public void testCalculateNewBookingTotalPriceShouldReturnCorrectInteger() {
        //Given
        when(priceComponentService.showPriceForComponents(BOOKING_DTO)).thenReturn(3000);
        Integer expected = 4500;

        //When
        Integer actual = underTest.calculateNewBookingTotalPrice(BOOKING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(priceComponentService).showPriceForComponents(BOOKING_DTO);
    }

    @Test
    public void testCalculateExistingBookingTotalPriceShouldReturnCorrectInteger() {
        //Given
        Booking booking = new Booking("Lord of the Rings",
                "Room1",
                "2021-03-15 12:00",
                "5,5",
                "sanyi",
                3000);
        when(bookingRepository.findByMovieTitleAndRoomNameAndStartedAtAndUserAndSeats(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt(),
                BOOKING_DTO.getUser(),
                BOOKING_DTO.getSeats()))
                .thenReturn(Optional.of(booking));
        when(priceComponentService.showPriceForComponents(BOOKING_DTO)).thenReturn(3000);
        Integer expected = 6000;

        //When
        Integer actual = underTest.calculateExistingBookingTotalPrice(BOOKING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(bookingRepository).findByMovieTitleAndRoomNameAndStartedAtAndUserAndSeats(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt(),
                BOOKING_DTO.getUser(),
                BOOKING_DTO.getSeats());
        verify(priceComponentService).showPriceForComponents(BOOKING_DTO);
    }

    @Test
    public void testGetBookingsByUserShouldReturnListOfAllBookingsMadeByGivenUser() {
        //Given
        List<Booking> list = List.of(
                new Booking("Lord of the Rings",
                        "Room1",
                        "2021-03-15 12:00",
                        "5,5",
                        "sanyi",
                        1500),
                new Booking("Lord of the Rings",
                        "Room1",
                        "2021-03-15 12:00",
                        "5,6",
                        "sanyi",
                        1500),
                new Booking("Lord of the Rings",
                        "Room1",
                        "2021-03-15 12:00",
                        "5,7",
                        "sanyi",
                        1500));
        when(bookingRepository.findAllByUser(BOOKING_DTO.getUser())).thenReturn(list);
        List<BookingDto> expected = List.of(BookingDto.builder()
                        .withMovieTitle("Lord of the Rings")
                        .withRoomName("Room1")
                        .withStartedAt("2021-03-15 12:00")
                        .withSeats("5,5")
                        .withUser("sanyi")
                        .build(),
                BookingDto.builder()
                        .withMovieTitle("Lord of the Rings")
                        .withRoomName("Room1")
                        .withStartedAt("2021-03-15 12:00")
                        .withSeats("5,6")
                        .withUser("sanyi")
                        .build(),
                BookingDto.builder()
                        .withMovieTitle("Lord of the Rings")
                        .withRoomName("Room1")
                        .withStartedAt("2021-03-15 12:00")
                        .withSeats("5,7")
                        .withUser("sanyi")
                        .build());

        //When
        List<BookingDto> actual = underTest.getBookingsByUser(BOOKING_DTO.getUser());

        //Then
        assertEquals(expected, actual);
        verify(bookingRepository).findAllByUser(BOOKING_DTO.getUser());
    }

    @Test
    public void testUpdateBasePriceShouldModifyBasePriceOfNewBookings() {
        //Given
        String expected = "Base price changed to " + 5000;

        //When
        String actual = underTest.updateBasePrice(5000);

        //Then
        assertEquals(actual, expected);
    }

}
