package com.epam.training.ticketservice.backend.movie.service.impl;

import com.epam.training.ticketservice.backend.movie.model.MovieDto;
import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public String createMovie(MovieDto movieDto) {
        checkValid(movieDto);
        Movie movie = new Movie(movieDto.getTitle(), movieDto.getGenre(), movieDto.getLength(), null);
        saveMovie(movie);
        return "Created movie" + movie;
    }

    @Override
    public String updateMovie(MovieDto movieDto) {
        checkValid(movieDto);
        Optional<Movie> movieOptional = movieRepository.findById(movieDto.getTitle());
        if (movieOptional.isEmpty()) {
            return "Movie does not exist";
        }
        Movie movie = movieOptional.get();
        movie.setGenre(movieDto.getGenre());
        movie.setLength(movieDto.getLength());
        saveMovie(movie);
        return "Updated movie" + movie;
    }

    @Override
    public String deleteMovie(String title) {
        Optional<Movie> movieOptional = movieRepository.findById(title);
        if (movieOptional.isEmpty()) {
            return "Movie does not exist";
        }
        Movie movie = movieOptional.get();
        movieRepository.delete(movie);
        return "Deleted movie" + movie;
    }

    @Override
    public List<MovieDto> listMovies() {
        return movieRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<Movie> getMovieByTitle(String title) {
        return movieRepository.findById(title);
    }

    @Override
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
                .withTitle(movie.getTitle())
                .withGenre(movie.getGenre())
                .withLength(movie.getLength())
                .build();
    }

    private void checkValid(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie title cannot be null");
        Objects.requireNonNull(movieDto.getGenre(), "Movie genre cannot be null");
        Objects.requireNonNull(movieDto.getLength(), "Movie length cannot be null");
    }

}
