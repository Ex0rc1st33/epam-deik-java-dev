package com.epam.training.ticketservice.backend.movie.service;

import com.epam.training.ticketservice.backend.movie.model.MovieDto;
import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    String createMovie(MovieDto movie);

    String updateMovie(MovieDto movie);

    String deleteMovie(String title);

    List<MovieDto> listMovies();

    Optional<Movie> getMovieByTitle(String title);

    void saveMovie(Movie movie);

}