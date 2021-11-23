package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.movie.model.MovieDto;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class MovieCommandTest {

    private final MovieService movieService = mock(MovieService.class);
    private final UserService userService = mock(UserService.class);
    private final MovieCommand underTest = new MovieCommand(movieService, userService);

    @Test
    public void testListMoviesShouldReturnCorrectListOfExistingMovies() {
        //Given
        List<MovieDto> list = List.of(
                MovieDto.builder()
                        .withTitle("Lord of the Rings")
                        .withGenre("fantasy")
                        .withLength(150)
                        .build());
        when(movieService.listMovies()).thenReturn(list);
        String expected = "Lord of the Rings (fantasy, 150 minutes)";

        //When
        String actual = underTest.listMovies();

        //Then
        assertEquals(expected, actual);
        verify(movieService).listMovies();
    }

    @Test
    public void testListMoviesShouldReturnCorrectErrorMessageWhenTheAreNoMovies() {
        //Given
        when(movieService.listMovies()).thenReturn(List.of());
        String expected = "There are no movies at the moment";

        //When
        String actual = underTest.listMovies();

        //Then
        assertEquals(expected, actual);
        verify(movieService).listMovies();
    }

}
