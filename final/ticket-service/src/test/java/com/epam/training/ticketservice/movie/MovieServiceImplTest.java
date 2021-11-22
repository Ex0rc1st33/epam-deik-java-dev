package com.epam.training.ticketservice.movie;

import com.epam.training.ticketservice.backend.movie.model.MovieDto;
import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.backend.movie.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieServiceImplTest {

    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final MovieServiceImpl underTest = new MovieServiceImpl(movieRepository);

    @Test
    public void testCreateMovieShouldCallMovieRepositoryAndReturnCorrectMessageWhenGivenMovieValid() {
        //Given
        Movie movie = new Movie("Lord of the Rings", "fantasy", 150, null);
        MovieDto movieDto = MovieDto.builder()
                .withTitle("Lord of the Rings")
                .withGenre("fantasy")
                .withLength(150)
                .build();
        when(movieRepository.save(movie)).thenReturn(movie);
        String expected = "Created movie" + movie;

        //When
        String actual = underTest.createMovie(movieDto);

        //Then
        assertEquals(expected, actual);
        verify(movieRepository).save(movie);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenGivenMovieInvalid() {
        //Given-When-Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(null));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenGivenMovieTitleInvalid() {
        //Given-When-Then
        MovieDto movieDto = MovieDto.builder()
                .withTitle(null)
                .withGenre("fantasy")
                .withLength(150)
                .build();
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movieDto));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenGivenMovieGenreInvalid() {
        //Given-When-Then
        MovieDto movieDto = MovieDto.builder()
                .withTitle("Lord of the Rings")
                .withGenre(null)
                .withLength(150)
                .build();
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movieDto));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenGivenMovieLengthInvalid() {
        //Given-When-Then
        MovieDto movieDto = MovieDto.builder()
                .withTitle("Lord of the Rings")
                .withGenre("fantasy")
                .withLength(null)
                .build();
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movieDto));
    }

    @Test
    public void testUpdateMovieShouldCallMovieRepositoryAndReturnCorrectMessageWhenGivenMovieExists() {
        //Given
        Movie original = new Movie("Lord of the Rings", "fantasy", 150, null);
        MovieDto movieDto = MovieDto.builder()
                .withTitle("Lord of the Rings")
                .withGenre("fantasy")
                .withLength(200)
                .build();
        when(movieRepository.findById(movieDto.getTitle())).thenReturn(Optional.of(original));
        Movie result = new Movie("Lord of the Rings", "fantasy", 200, null);
        when(movieRepository.save(result)).thenReturn(result);
        String expected = "Updated movie" + result;

        //When
        String actual = underTest.updateMovie(movieDto);

        //Then
        assertEquals(expected, actual);
        verify(movieRepository).findById(movieDto.getTitle());
        verify(movieRepository).save(result);
    }

    @Test
    public void testUpdateMovieShouldCallMovieRepositoryAndReturnErrorMessageWhenGivenMovieDoesNotExist() {
        //Given
        MovieDto movieDto = MovieDto.builder()
                .withTitle("Lord of the Rings")
                .withGenre("fantasy")
                .withLength(200)
                .build();
        when(movieRepository.findById(movieDto.getTitle())).thenReturn(Optional.empty());
        String expected = "Movie does not exist";

        //When
        String actual = underTest.updateMovie(movieDto);

        //Then
        assertEquals(expected, actual);
        verify(movieRepository).findById(movieDto.getTitle());
    }

    @Test
    public void testDeleteMovieShouldCallMovieRepositoryAndReturnCorrectMessageWhenGivenMovieExists() {
        //Given
        Movie movie = new Movie("Lord of the Rings", "fantasy", 150, null);
        MovieDto movieDto = MovieDto.builder()
                .withTitle("Lord of the Rings")
                .withGenre("fantasy")
                .withLength(150)
                .build();
        when(movieRepository.findById(movieDto.getTitle())).thenReturn(Optional.of(movie));
        String expected = "Deleted movie" + movie;

        //When
        String actual = underTest.deleteMovie(movieDto.getTitle());

        //Then
        assertEquals(expected, actual);
        verify(movieRepository).findById(movieDto.getTitle());
        verify(movieRepository).delete(movie);
    }

    @Test
    public void testDeleteMovieShouldCallMovieRepositoryAndReturnErrorMessageWhenGivenMovieDoesNotExist() {
        //Given
        MovieDto movieDto = MovieDto.builder()
                .withTitle("Lord of the Rings")
                .withGenre("fantasy")
                .withLength(150)
                .build();
        when(movieRepository.findById(movieDto.getTitle())).thenReturn(Optional.empty());
        String expected = "Movie does not exist";

        //When
        String actual = underTest.deleteMovie(movieDto.getTitle());

        //Then
        assertEquals(expected, actual);
        verify(movieRepository).findById(movieDto.getTitle());
    }

    @Test
    public void testListMoviesShouldReturnCorrectListOfMovies() {
        //Given
        List<Movie> list = List.of(
                new Movie("Lord of the Rings", "fantasy", 150, null),
                new Movie("Lord of the Rings", "fantasy", 200, null),
                new Movie("Lord of the Rings", "fantasy", 250, null));
        when(movieRepository.findAll()).thenReturn(list);
        List<MovieDto> expected = List.of(
                MovieDto.builder()
                        .withTitle("Lord of the Rings")
                        .withGenre("fantasy")
                        .withLength(150)
                        .build(),
                MovieDto.builder()
                        .withTitle("Lord of the Rings")
                        .withGenre("fantasy")
                        .withLength(200)
                        .build(),
                MovieDto.builder()
                        .withTitle("Lord of the Rings")
                        .withGenre("fantasy")
                        .withLength(250)
                        .build()
        );

        //When
        List<MovieDto> actual = underTest.listMovies();

        //Then
        assertEquals(expected, actual);
        verify(movieRepository).findAll();
    }

    @Test
    public void testGetMovieByTitleShouldCallMovieRepositoryAndReturnCorrectOptional() {
        //Given
        Movie movie = new Movie("Lord of the Rings", "fantasy", 150, null);
        when(movieRepository.findById(movie.getTitle())).thenReturn(Optional.of(movie));
        Optional<Movie> expected = Optional.of(movie);

        //When
        Optional<Movie> actual = underTest.getMovieByTitle(movie.getTitle());

        //Then
        assertEquals(expected, actual);
        verify(movieRepository).findById(movie.getTitle());
    }

}
