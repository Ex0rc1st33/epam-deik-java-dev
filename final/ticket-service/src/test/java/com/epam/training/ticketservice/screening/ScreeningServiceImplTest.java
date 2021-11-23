package com.epam.training.ticketservice.screening;

import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.room.model.RoomDto;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import com.epam.training.ticketservice.backend.screening.model.ScreeningDto;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.backend.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.backend.screening.service.ScreeningService;
import com.epam.training.ticketservice.backend.screening.service.impl.ScreeningServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ScreeningServiceImplTest {

    private static final Screening SCREENING_ENTITY = new Screening("Lord of the Rings",
            "Room1",
            "2021-03-15 10:50",
            null);
    private static final ScreeningDto SCREENING_DTO = ScreeningDto.builder()
            .withMovieTitle("Lord of the Rings")
            .withRoomName("Room1")
            .withStartedAt("2021-03-15 10:50")
            .build();
    private static final Movie MOVIE_ENTITY = new Movie("Lord of the Rings", "fantasy", 150, null);
    private static final Room ROOM_ENTITY = new Room("Room1", 20, 30, null);

    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final MovieService movieService = mock(MovieService.class);
    private final RoomService roomService = mock(RoomService.class);
    private final ScreeningService underTest = new ScreeningServiceImpl(screeningRepository,
            movieService,
            roomService);

    @Test
    public void testCreateScreeningShouldCallScreeningRepositoryAndReturnCorrectMessageWhenGivenScreeningValid() {
        //Given
        when(movieService.getMovieByTitle(SCREENING_DTO.getMovieTitle())).thenReturn(Optional.of(MOVIE_ENTITY));
        when(roomService.getRoomByName(ROOM_ENTITY.getName())).thenReturn(Optional.of(ROOM_ENTITY));
        when(screeningRepository.save(SCREENING_ENTITY)).thenReturn(SCREENING_ENTITY);
        Screening screening = new Screening("Harry Potter",
                "Room1",
                "2021-03-16 10:50",
                null);
        Movie movie = new Movie("Harry Potter", "fantasy", 200, null);
        when(screeningRepository.findAllByRoomName(SCREENING_DTO.getRoomName())).thenReturn(List.of(screening));
        when(movieService.getMovieByTitle(screening.getMovieTitle())).thenReturn(Optional.of(movie));
        String expected = "Created screening: " + SCREENING_ENTITY;

        //When
        String actual = underTest.createScreening(SCREENING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(movieService, times(2)).getMovieByTitle(SCREENING_DTO.getMovieTitle());
        verify(roomService).getRoomByName(ROOM_ENTITY.getName());
        verify(screeningRepository).save(SCREENING_ENTITY);
        verify(screeningRepository, times(2)).findAllByRoomName(SCREENING_DTO.getRoomName());
        verify(movieService, times(2)).getMovieByTitle(screening.getMovieTitle());
    }

    @Test
    public void testCreateScreeningShouldThrowIllegalStateExceptionWhenGivenMovieInvalid() {
        //Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 10:50")
                .build();
        when(movieService.getMovieByTitle(screeningDto.getMovieTitle())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest.createScreening(screeningDto));
        verify(movieService).getMovieByTitle(screeningDto.getMovieTitle());
    }

    @Test
    public void testCreateScreeningShouldThrowIllegalStateExceptionWhenGiveRoomInvalid() {
        //Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 10:50")
                .build();
        when(movieService.getMovieByTitle(screeningDto.getMovieTitle())).thenReturn(Optional.of(MOVIE_ENTITY));
        when(roomService.getRoomByName(screeningDto.getRoomName())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest.createScreening(screeningDto));
        verify(movieService).getMovieByTitle(screeningDto.getMovieTitle());
        verify(roomService).getRoomByName(screeningDto.getRoomName());
    }

    @Test
    public void testCreateScreeningShouldCallScreeningRepositoryAndReturnErrorMessageWhenGivenScreeningOverlapping() {
        //Given
        when(movieService.getMovieByTitle(SCREENING_DTO.getMovieTitle())).thenReturn(Optional.of(MOVIE_ENTITY));
        when(roomService.getRoomByName(ROOM_ENTITY.getName())).thenReturn(Optional.of(ROOM_ENTITY));
        Screening screening = new Screening("Harry Potter",
                "Room1",
                "2021-03-15 11:00",
                null);
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 11:10")
                .build();
        Movie movie = new Movie("Harry Potter", "fantasy", 200, null);
        when(screeningRepository.findAllByRoomName(SCREENING_DTO.getRoomName())).thenReturn(List.of(screening));
        when(movieService.getMovieByTitle(screening.getMovieTitle())).thenReturn(Optional.of(movie));
        String expected = "There is an overlapping screening";

        //When
        String actual1 = underTest.createScreening(SCREENING_DTO);
        String actual2 = underTest.createScreening(screeningDto);

        //Then
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
        verify(movieService, times(4)).getMovieByTitle(SCREENING_DTO.getMovieTitle());
        verify(roomService, times(2)).getRoomByName(ROOM_ENTITY.getName());
        verify(screeningRepository, times(2)).findAllByRoomName(SCREENING_DTO.getRoomName());
        verify(movieService, times(2)).getMovieByTitle(screening.getMovieTitle());
    }

    @Test
    public void testCreateScreeningShouldCallScreeningRepositoryAndReturnErrorMessageWhenGivenScreeningInBreakPeriod() {
        //Given
        when(movieService.getMovieByTitle(SCREENING_DTO.getMovieTitle())).thenReturn(Optional.of(MOVIE_ENTITY));
        when(roomService.getRoomByName(ROOM_ENTITY.getName())).thenReturn(Optional.of(ROOM_ENTITY));
        Screening screening = new Screening("Harry Potter",
                "Room1",
                "2021-03-15 07:25",
                null);
        Movie movie = new Movie("Harry Potter", "fantasy", 200, null);
        when(screeningRepository.findAllByRoomName(SCREENING_DTO.getRoomName())).thenReturn(List.of(screening));
        when(movieService.getMovieByTitle(screening.getMovieTitle())).thenReturn(Optional.of(movie));
        String expected = "This would start in the break period after another screening in this room";

        //When
        String actual1 = underTest.createScreening(SCREENING_DTO);

        //Then
        assertEquals(expected, actual1);
        verify(movieService, times(2)).getMovieByTitle(SCREENING_DTO.getMovieTitle());
        verify(roomService, times(1)).getRoomByName(ROOM_ENTITY.getName());
        verify(screeningRepository, times(2)).findAllByRoomName(SCREENING_DTO.getRoomName());
        verify(movieService, times(2)).getMovieByTitle(screening.getMovieTitle());
    }

    @Test
    public void testDeleteScreeningShouldCallScreeningRepositoryAndReturnCorrectMessageWhenGivenScreeningExists() {
        //Given
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartedAt(SCREENING_DTO.getMovieTitle(),
                SCREENING_DTO.getRoomName(),
                SCREENING_DTO.getStartedAt())).
                thenReturn(Optional.of(SCREENING_ENTITY));
        String expected = "Deleted screening: " + SCREENING_ENTITY;

        //When
        String actual = underTest.deleteScreening(SCREENING_DTO);

        //Then
        assertEquals(expected, actual);
        verify(screeningRepository).findByMovieTitleAndRoomNameAndStartedAt(SCREENING_DTO.getMovieTitle(),
                SCREENING_DTO.getRoomName(),
                SCREENING_DTO.getStartedAt());
        verify(screeningRepository).delete(SCREENING_ENTITY);
    }

    @Test
    public void testDeleteScreeningShouldThrowIllegalStateExceptionWhenGivenScreeningDoesNotExist() {
        //Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 10:50")
                .build();
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartedAt(screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartedAt()))
                .thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest.deleteScreening(screeningDto));
        verify(screeningRepository).findByMovieTitleAndRoomNameAndStartedAt(screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartedAt());
    }

    @Test
    public void testListScreeningsShouldReturnCorrectListOfScreenings() {
        //Given
        List<Screening> list = List.of(
                new Screening("Harry Potter", "Room1", "2021-03-15 10:50", null),
                new Screening("Dune", "Room2", "2021-03-15 10:50", null),
                new Screening("Star Wars", "Room3", "2021-03-15 10:50", null));
        when(screeningRepository.findAll()).thenReturn(list);
        List<ScreeningDto> expected = List.of(
                ScreeningDto.builder()
                        .withMovieTitle("Harry Potter")
                        .withRoomName("Room1")
                        .withStartedAt("2021-03-15 10:50")
                        .build(),
                ScreeningDto.builder()
                        .withMovieTitle("Dune")
                        .withRoomName("Room2")
                        .withStartedAt("2021-03-15 10:50")
                        .build(),
                ScreeningDto.builder()
                        .withMovieTitle("Star Wars")
                        .withRoomName("Room3")
                        .withStartedAt("2021-03-15 10:50")
                        .build()
        );

        //When
        List<ScreeningDto> actual = underTest.listScreenings();

        //Then
        assertEquals(expected, actual);
        verify(screeningRepository).findAll();
    }

    @Test
    public void testGetMovieByTitleAndRoomNameAndStartedAtShouldCallMovieRepositoryAndReturnCorrectOptional() {
        //Given
        Screening screening = new Screening("Lord of the Rings",
                "Room1",
                "2021-03-15 10:50",
                null);
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartedAt(SCREENING_DTO.getMovieTitle(),
                SCREENING_DTO.getRoomName(),
                SCREENING_DTO.getStartedAt()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        Optional<Screening> expected = Optional.of(SCREENING_ENTITY);

        //When
        Optional<Screening> actual = underTest.getMovieByTitleAndRoomNameAndStartedAt(SCREENING_DTO.getMovieTitle(),
                SCREENING_DTO.getRoomName(),
                SCREENING_DTO.getStartedAt());

        //Then
        assertEquals(expected, actual);
        verify(screeningRepository).findByMovieTitleAndRoomNameAndStartedAt(SCREENING_DTO.getMovieTitle(),
                SCREENING_DTO.getRoomName(),
                SCREENING_DTO.getStartedAt());
    }

}
