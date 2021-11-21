package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.movie.model.MovieDTO;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.user.model.UserDTO;
import com.epam.training.ticketservice.backend.user.persistance.entity.User;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class MovieCommand {

    private final MovieService movieService;
    private final UserService userService;

    public MovieCommand(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create new movie")
    public String createMovie(String title, String genre, Integer length) {
        MovieDTO movie = MovieDTO.builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        return movieService.createMovie(movie);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Update existing movie")
    public String updateMovie(String title, String genre, Integer length) {
        MovieDTO movie = MovieDTO.builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        return movieService.updateMovie(movie);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Delete existing movie")
    public String deleteMovie(String title) {
        return movieService.deleteMovie(title);
    }

    @ShellMethod(key = "list movies", value = "List all existing movies")
    public String listMovies() {
        List<MovieDTO> movies = movieService.listMovies();
        if (movies.size() == 0) {
            return "There are no movies at the moment";
        }
        StringBuilder buffer = new StringBuilder();
        for (MovieDTO movie : movies) {
            buffer.append(movie).append("\n");
        }
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

    private Availability isAvailable() {
        Optional<UserDTO> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("you are not an admin!");
    }

}
