package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.screening.model.ScreeningDto;
import com.epam.training.ticketservice.backend.screening.service.ScreeningService;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ScreeningCommandTest {

    private final ScreeningService screeningService = mock(ScreeningService.class);
    private final UserService userService = mock(UserService.class);
    private final MovieService movieService = mock(MovieService.class);
    private final ScreeningCommand underTest = new ScreeningCommand(screeningService, userService, movieService);

    @Test
    public void testListScreeningsShouldReturnCorrectListOfExistingScreenings() {
        //Given
        List<ScreeningDto> list = List.of(
                ScreeningDto.builder()
                        .withMovieTitle("Lord of the Rings")
                        .withRoomName("Room1")
                        .withStartedAt("2021-03-15 10:50")
                        .build());
        Movie movie = new Movie("Lord of the Rings", "fantasy", 150, null);
        when(screeningService.listScreenings()).thenReturn(list);
        when(movieService.getMovieByTitle(list.get(0).getMovieTitle())).thenReturn(Optional.of(movie));
        String expected = "Lord of the Rings "
                + "(fantasy, 150 minutes), screened in room Room1, at 2021-03-15 10:50";

        //When
        String actual = underTest.listScreenings();

        //Then
        assertEquals(expected, actual);
        verify(screeningService).listScreenings();
        verify(movieService).getMovieByTitle(list.get(0).getMovieTitle());
    }

    @Test
    public void testListScreeningsShouldReturnCorrectErrorMessageWhenTheAreNoScreenings() {
        //Given
        when(screeningService.listScreenings()).thenReturn(List.of());
        String expected = "There are no screenings";

        //When
        String actual = underTest.listScreenings();

        //Then
        assertEquals(expected, actual);
        verify(screeningService).listScreenings();
    }

}
