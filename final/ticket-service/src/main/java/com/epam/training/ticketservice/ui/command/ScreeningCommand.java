package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.movie.persistance.entity.Movie;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.screening.model.ScreeningDTO;
import com.epam.training.ticketservice.backend.screening.service.ScreeningService;
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
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final UserService userService;
    private final MovieService movieService;

    public ScreeningCommand(ScreeningService screeningService, UserService userService, MovieService movieService) {
        this.screeningService = screeningService;
        this.userService = userService;
        this.movieService = movieService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create new screening")
    public String createScreening(String movieTitle, String roomName, String startedAt) {
        ScreeningDTO screeningDTO = ScreeningDTO.builder()
                .withMovieTitle(movieTitle)
                .withRoomName(roomName)
                .withStartedAt(startedAt)
                .build();
        return screeningService.createScreening(screeningDTO);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete existing screening")
    public String deleteScreening(String movieTitle, String roomName, String startedAt) {
        ScreeningDTO screeningDTO = ScreeningDTO.builder()
                .withMovieTitle(movieTitle)
                .withRoomName(roomName)
                .withStartedAt(startedAt)
                .build();
        return screeningService.deleteScreening(screeningDTO);
    }

    @ShellMethod(key = "list screenings", value = "List all existing screenings")
    public String listScreenings() {
        List<ScreeningDTO> screenings = screeningService.listScreenings();
        if (screenings.size() == 0) {
            return "There are no screenings";
        }
        StringBuilder buffer = new StringBuilder();
        for (ScreeningDTO screeningDTO : screenings) {
            Optional<Movie> movieOptional = movieService.getMovieByTitle(screeningDTO.getMovieTitle());
            if (movieOptional.isPresent()) {
                Movie movie = movieOptional.get();
                buffer.append(screeningDTO.getMovieTitle())
                        .append(" (")
                        .append(movie.getGenre())
                        .append(", ")
                        .append(movie.getLength())
                        .append(" " + "minutes), screened in room ")
                        .append(screeningDTO.getRoomName())
                        .append(", at ")
                        .append(screeningDTO.getStartedAt())
                        .append("\n");
            }
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
