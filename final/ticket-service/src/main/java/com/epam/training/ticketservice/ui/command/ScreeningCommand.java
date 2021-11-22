package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.screening.model.ScreeningDto;
import com.epam.training.ticketservice.backend.screening.service.ScreeningService;
import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
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
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovieTitle(movieTitle)
                .withRoomName(roomName)
                .withStartedAt(startedAt)
                .build();
        return screeningService.createScreening(screeningDto);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete existing screening")
    public String deleteScreening(String movieTitle, String roomName, String startedAt) {
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovieTitle(movieTitle)
                .withRoomName(roomName)
                .withStartedAt(startedAt)
                .build();
        return screeningService.deleteScreening(screeningDto);
    }

    @ShellMethod(key = "list screenings", value = "List all existing screenings")
    public String listScreenings() {
        List<ScreeningDto> screenings = screeningService.listScreenings();
        if (screenings.size() == 0) {
            return "There are no screenings";
        }
        StringBuilder buffer = new StringBuilder();
        for (ScreeningDto screeningDto : screenings) {
            Optional<Movie> movieOptional = movieService.getMovieByTitle(screeningDto.getMovieTitle());
            if (movieOptional.isPresent()) {
                Movie movie = movieOptional.get();
                buffer.append(screeningDto.getMovieTitle())
                        .append(" (")
                        .append(movie.getGenre())
                        .append(", ")
                        .append(movie.getLength())
                        .append(" " + "minutes), screened in room ")
                        .append(screeningDto.getRoomName())
                        .append(", at ")
                        .append(screeningDto.getStartedAt())
                        .append("\n");
            }
        }
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("you are not an admin!");
    }

}
