package com.epam.training.ticketservice.backend.movie.service;

import com.epam.training.ticketservice.backend.movie.model.MovieDTO;
import com.epam.training.ticketservice.backend.movie.persistance.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    String createMovie(MovieDTO movie);

    String updateMovie(MovieDTO movie);

    String deleteMovie(String title);

    List<MovieDTO> listMovies();

    Optional<Movie> getMovieByTitle(String title);

}