package com.epam.training.ticketservice.backend.movie.service.impl;

import com.epam.training.ticketservice.backend.movie.model.MovieDTO;
import com.epam.training.ticketservice.backend.movie.persistance.entity.Movie;
import com.epam.training.ticketservice.backend.movie.persistance.repository.MovieRepository;
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
    public String createMovie(MovieDTO movieDTO) {
        Objects.requireNonNull(movieDTO, "Movie cannot be null");
        Objects.requireNonNull(movieDTO.getTitle(), "Movie title cannot be null");
        Objects.requireNonNull(movieDTO.getGenre(), "Movie genre cannot be null");
        Objects.requireNonNull(movieDTO.getLength(), "Movie length cannot be null");
        Movie movie = new Movie(movieDTO.getTitle(), movieDTO.getGenre(), movieDTO.getLength());
        movieRepository.save(movie);
        return "Created movie: " + movie;
    }

    @Override
    public String updateMovie(MovieDTO movieDTO) {
        Objects.requireNonNull(movieDTO, "Movie cannot be null");
        Objects.requireNonNull(movieDTO.getTitle(), "Movie title cannot be null");
        Objects.requireNonNull(movieDTO.getGenre(), "Movie genre cannot be null");
        Objects.requireNonNull(movieDTO.getLength(), "Movie length cannot be null");
        Optional<Movie> movieOptional = movieRepository.findById(movieDTO.getTitle());
        if (movieOptional.isEmpty()) {
            return "Movie does not exist";
        }
        Movie movie = movieOptional.get();
        movie.setGenre(movieDTO.getGenre());
        movie.setLength(movieDTO.getLength());
        movieRepository.save(movie);
        return "Updated movie: " + movie;
    }

    @Override
    public String deleteMovie(String title) {
        Optional<Movie> movieOptional = movieRepository.findById(title);
        if (movieOptional.isEmpty()) {
            return "Movie does not exist";
        }
        Movie movie = movieOptional.get();
        movieRepository.delete(movie);
        return "Deleted movie: " + movie;
    }

    @Override
    public List<MovieDTO> listMovies() {
        return movieRepository.findAll().stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<Movie> getMovieByTitle(String title) {
        return movieRepository.findById(title);
    }

    private MovieDTO convertEntityToDTO(Movie movie) {
        return MovieDTO.builder()
                .withTitle(movie.getTitle())
                .withGenre(movie.getGenre())
                .withLength(movie.getLength())
                .build();
    }

}
