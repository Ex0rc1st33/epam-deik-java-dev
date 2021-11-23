package com.epam.training.ticketservice.backend.pricecomponent;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.backend.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.backend.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.backend.pricecomponent.service.PriceComponentService;
import com.epam.training.ticketservice.backend.pricecomponent.service.impl.PriceComponentServiceImpl;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.backend.screening.service.ScreeningService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class PriceComponentServiceImplTest {

    private static final PriceComponent PRICE_COMPONENT_ENTITY = new PriceComponent("VAT",
            500);
    private static final PriceComponentDto PRICE_COMPONENT_DTO = PriceComponentDto.builder()
            .withName("VAT")
            .withValue(500)
            .build();
    private static final Movie MOVIE_ENTITY = new Movie("Lord of the Rings", "fantasy", 150, null);
    private static final Room ROOM_ENTITY = new Room("Room1", 20, 30, null);
    private static final Screening SCREENING_ENTITY = new Screening("Lord of the Rings",
            "Room1",
            "2021-03-15 12:00",
            null);
    private static final BookingDto BOOKING_DTO = BookingDto.builder()
            .withMovieTitle("Lord of the Rings")
            .withRoomName("Room1")
            .withStartedAt("2021-03-15 12:00")
            .withUser("sanyi")
            .withSeats("5,5")
            .build();

    private final PriceComponentRepository priceComponentRepository = mock(PriceComponentRepository.class);
    private final MovieService movieService = mock(MovieService.class);
    private final RoomService roomService = mock(RoomService.class);
    private final ScreeningService screeningService = mock(ScreeningService.class);
    private final PriceComponentService underTest = new PriceComponentServiceImpl(priceComponentRepository,
            movieService,
            roomService,
            screeningService);

    @Test
    public void testCreatePriceComponentShouldCallPriceComponentRepositoryAndReturnCorrectMessageWhenGivenPriceComponentValid() {
        //Given
        when(priceComponentRepository.save(PRICE_COMPONENT_ENTITY)).thenReturn(PRICE_COMPONENT_ENTITY);
        String expected = "Created price component: " + PRICE_COMPONENT_ENTITY;

        //When
        String actual = underTest.createPriceComponent(PRICE_COMPONENT_DTO);

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).save(PRICE_COMPONENT_ENTITY);
    }

    @Test
    public void testAttachPriceComponentToRoomShouldModifyTheRoomCorrectlyAndReturnCorrectMessageWhenGivenPriceComponentNameAndRoomNameValid() {
        //Given
        Room result = new Room("Room1", 20, 30, "VAT");
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName())).thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(roomService.getRoomByName(ROOM_ENTITY.getName())).thenReturn(Optional.of(ROOM_ENTITY));
        String expected = "Attached " + PRICE_COMPONENT_ENTITY + " component to " + result + " room";

        //When
        String actual = underTest.attachPriceComponentToRoom(PRICE_COMPONENT_DTO.getName(), ROOM_ENTITY.getName());

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
        verify(roomService).getRoomByName(ROOM_ENTITY.getName());
        verify(roomService).saveRoom(result);
    }

    @Test
    public void testAttachPriceComponentToRoomShouldThrowIllegalStateExceptionWhenGivenPriceComponentDoesNotExist() {
        //Given
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .attachPriceComponentToRoom(PRICE_COMPONENT_DTO.getName(), ROOM_ENTITY.getName()));
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
    }

    @Test
    public void testAttachPriceComponentToRoomShouldThrowIllegalStateExceptionWhenGivenRoomDoesNotExist() {
        //Given
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName()))
                .thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(roomService.getRoomByName(ROOM_ENTITY.getName())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .attachPriceComponentToRoom(PRICE_COMPONENT_DTO.getName(), ROOM_ENTITY.getName()));
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
        verify(roomService).getRoomByName(ROOM_ENTITY.getName());
    }

    @Test
    public void testAttachPriceComponentToMovieShouldModifyTheMovieCorrectlyAndReturnCorrectMessageWhenGivenPriceComponentNameAndMovieTitleValid() {
        //Given
        Movie result = new Movie("Lord of the Rings", "fantasy", 150, "VAT");
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName())).thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(movieService.getMovieByTitle(MOVIE_ENTITY.getTitle())).thenReturn(Optional.of(MOVIE_ENTITY));
        String expected = "Attached " + PRICE_COMPONENT_ENTITY + " component to " + result + " movie";

        //When
        String actual = underTest.attachPriceComponentToMovie(PRICE_COMPONENT_DTO.getName(), MOVIE_ENTITY.getTitle());

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
        verify(movieService).getMovieByTitle(MOVIE_ENTITY.getTitle());
        verify(movieService).saveMovie(result);
    }

    @Test
    public void testAttachPriceComponentToMovieShouldThrowIllegalStateExceptionWhenGivenPriceComponentDoesNotExist() {
        //Given
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .attachPriceComponentToMovie(PRICE_COMPONENT_DTO.getName(), MOVIE_ENTITY.getTitle()));
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
    }

    @Test
    public void testAttachPriceComponentToMovieShouldThrowIllegalStateExceptionWhenGivenMovieDoesNotExist() {
        //Given
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName()))
                .thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(movieService.getMovieByTitle(MOVIE_ENTITY.getTitle())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .attachPriceComponentToMovie(PRICE_COMPONENT_DTO.getName(), MOVIE_ENTITY.getTitle()));
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
        verify(movieService).getMovieByTitle(MOVIE_ENTITY.getTitle());
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldModifyTheScreeningCorrectlyAndReturnCorrectMessageWhenGivenPriceComponentNameAndScreeningValid() {
        //Given
        Screening result = new Screening("Lord of the Rings",
                "Room1",
                "2021-03-15 12:00",
                "VAT");
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName())).thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(screeningService.getMovieByTitleAndRoomNameAndStartedAt(SCREENING_ENTITY.getMovieTitle(),
                SCREENING_ENTITY.getRoomName(),
                SCREENING_ENTITY.getStartedAt()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        String expected = "Attached " + PRICE_COMPONENT_ENTITY + " component to " + result + " screening";

        //When
        String actual = underTest.attachPriceComponentToScreening(PRICE_COMPONENT_DTO.getName(),
                SCREENING_ENTITY.getMovieTitle(),
                SCREENING_ENTITY.getRoomName(),
                SCREENING_ENTITY.getStartedAt());

        //Then
        assertEquals(expected, actual);
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
        verify(screeningService).getMovieByTitleAndRoomNameAndStartedAt(SCREENING_ENTITY.getMovieTitle(),
                SCREENING_ENTITY.getRoomName(),
                SCREENING_ENTITY.getStartedAt());
        verify(screeningService).saveScreening(result);
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowIllegalStateExceptionWhenGivenPriceComponentDoesNotExist() {
        //Given
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .attachPriceComponentToScreening(PRICE_COMPONENT_DTO.getName(),
                        SCREENING_ENTITY.getMovieTitle(),
                        SCREENING_ENTITY.getRoomName(),
                        SCREENING_ENTITY.getStartedAt()));
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowIllegalStateExceptionWhenGivenScreeningDoesNotExist() {
        //Given
        when(priceComponentRepository.findById(PRICE_COMPONENT_DTO.getName()))
                .thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(screeningService.getMovieByTitleAndRoomNameAndStartedAt(SCREENING_ENTITY.getMovieTitle(),
                SCREENING_ENTITY.getRoomName(),
                SCREENING_ENTITY.getStartedAt()))
                .thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .attachPriceComponentToScreening(PRICE_COMPONENT_DTO.getName(),
                        SCREENING_ENTITY.getMovieTitle(),
                        SCREENING_ENTITY.getRoomName(),
                        SCREENING_ENTITY.getStartedAt()));
        verify(priceComponentRepository).findById(PRICE_COMPONENT_DTO.getName());
        verify(screeningService).getMovieByTitleAndRoomNameAndStartedAt(SCREENING_ENTITY.getMovieTitle(),
                SCREENING_ENTITY.getRoomName(),
                SCREENING_ENTITY.getStartedAt());
    }

    @Test
    public void testShowPriceForComponentsShouldReturnCorrectIntegerWhenMovieAndRoomAndScreeningValid() {
        //Given
        Movie movie = new Movie("Lord of the Rings", "fantasy", 150, "VAT");
        Room room = new Room("Room1", 20, 30, "VAT");
        Screening screening = new Screening("Lord of the Rings",
                "Room1",
                "2021-03-15 12:00",
                "VAT");
        BookingDto bookingDto = BookingDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 12:00")
                .withUser("sanyi")
                .withSeats("5,5")
                .build();
        when(roomService.getRoomByName(room.getName())).thenReturn(Optional.of(room));
        when(movieService.getMovieByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(screeningService.getMovieByTitleAndRoomNameAndStartedAt(screening.getMovieTitle(),
                screening.getRoomName(),
                screening.getStartedAt()))
                .thenReturn(Optional.of(screening));
        when(priceComponentRepository.findById(room.getPriceComponent())).thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(priceComponentRepository.findById(movie.getPriceComponent())).thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(priceComponentRepository.findById(screening.getPriceComponent())).thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        Integer expected1 = 1500;
        Integer expected2 = 0;

        //When
        Integer actual1 = underTest.showPriceForComponents(bookingDto);
        movie.setPriceComponent(null);
        room.setPriceComponent(null);
        screening.setPriceComponent(null);
        Integer actual2 = underTest.showPriceForComponents(bookingDto);

        //Then
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        verify(roomService, times(2)).getRoomByName(room.getName());
        verify(movieService, times(2)).getMovieByTitle(movie.getTitle());
        verify(screeningService, times(2)).getMovieByTitleAndRoomNameAndStartedAt(screening.getMovieTitle(),
                screening.getRoomName(),
                screening.getStartedAt());
    }

    @Test
    public void testShowPriceForComponentsShouldThrowIllegalStateExceptionWhenGivenRoomDoesNotExist() {
        //Given
        when(roomService.getRoomByName(BOOKING_DTO.getRoomName())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .showPriceForComponents(BOOKING_DTO));
        verify(roomService).getRoomByName(BOOKING_DTO.getRoomName());
    }

    @Test
    public void testShowPriceForComponentsShouldThrowIllegalStateExceptionWhenGivenMovieDoesNotExist() {
        //Given
        when(roomService.getRoomByName(BOOKING_DTO.getRoomName())).thenReturn(Optional.of(ROOM_ENTITY));
        when(movieService.getMovieByTitle(BOOKING_DTO.getMovieTitle())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .showPriceForComponents(BOOKING_DTO));
        verify(roomService).getRoomByName(BOOKING_DTO.getRoomName());
        verify(movieService).getMovieByTitle(BOOKING_DTO.getMovieTitle());
    }

    @Test
    public void testShowPriceForComponentsShouldThrowIllegalStateExceptionWhenGivenScreeningDoesNotExist() {
        //Given
        when(roomService.getRoomByName(BOOKING_DTO.getRoomName())).thenReturn(Optional.of(ROOM_ENTITY));
        when(movieService.getMovieByTitle(BOOKING_DTO.getMovieTitle())).thenReturn(Optional.of(MOVIE_ENTITY));
        when(screeningService.getMovieByTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt()))
                .thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest
                .showPriceForComponents(BOOKING_DTO));
        verify(roomService).getRoomByName(BOOKING_DTO.getRoomName());
        verify(movieService).getMovieByTitle(BOOKING_DTO.getMovieTitle());
        verify(screeningService).getMovieByTitleAndRoomNameAndStartedAt(BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartedAt());
    }

}
