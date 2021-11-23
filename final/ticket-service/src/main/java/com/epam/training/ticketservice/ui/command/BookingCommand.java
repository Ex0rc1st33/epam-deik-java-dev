package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.booking.service.BookingService;
import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class BookingCommand {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingCommand(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "book", value = "Create new booking")
    public String book(String movieTitle, String roomName, String startedAt, String seats) {
        Optional<UserDto> loggedInUser = userService.getLoggedInUser();
        if (loggedInUser.isEmpty()) {
            return "You need to sign in first";
        }
        String user = loggedInUser.get().getUsername();
        BookingDto bookingDto = BookingDto.builder()
                .withMovieTitle(movieTitle)
                .withRoomName(roomName)
                .withStartedAt(startedAt)
                .withSeats(seats)
                .withUser(user)
                .build();
        return bookingService.createBooking(bookingDto);
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() != User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("you are not a non-admin user!");
    }

}
