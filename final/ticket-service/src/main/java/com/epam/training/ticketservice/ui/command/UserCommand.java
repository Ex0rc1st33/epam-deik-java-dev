package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.booking.service.BookingService;
import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class UserCommand {

    private final UserService userService;
    private final BookingService bookingService;

    public UserCommand(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @ShellMethod(key = "sign in privileged", value = "Login for admin users")
    public String signInPrivileged(String username, String password) {
        return userService.signInPrivileged(username, password);
    }

    @ShellMethod(key = "sign up", value = "Registration for non-admin users")
    public String signUp(String username, String password) {
        return userService.signUp(username, password);
    }

    @ShellMethod(key = "sign in", value = "Login for non-admin users")
    public String signIn(String username, String password) {
        return userService.signIn(username, password);
    }

    @ShellMethod(key = "sign out", value = "Logout from account")
    public String signOut() {
        return userService.signOut();
    }

    @ShellMethod(key = "describe account", value = "Get user information")
    public String describeAccount() {
        Optional<UserDto> userDto = userService.getLoggedInUser();
        if (userDto.isEmpty()) {
            return "You are not signed in";
        }
        if (userDto.get().getRole() == User.Role.ADMIN) {
            return "Signed in with privileged account '" + userDto.get().getUsername() + "'";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append("Signed in with account '").append(userDto.get().getUsername()).append("'\n");
        List<BookingDto> bookings = bookingService.getBookingsByUser(userDto.get().getUsername());
        if (bookings.isEmpty()) {
            buffer.append("You have not booked any tickets yet");
            return buffer.toString();
        }
        buffer.append("Your previous bookings are\n");
        for (BookingDto bookingDto : bookings) {
            buffer.append("Seats");
            String[] seats = bookingDto.getSeats().split(" ");
            for (String seat : seats) {
                buffer.append(" (").append(seat).append("),");
            }
            buffer.deleteCharAt(buffer.length() - 1);
            buffer.append(" on ")
                    .append(bookingDto.getMovieTitle())
                    .append(" in room ")
                    .append(bookingDto.getRoomName())
                    .append(" starting at ")
                    .append(bookingDto.getStartedAt())
                    .append(" for ")
                    .append(bookingService.getBasePriceOfExistingBooking(bookingDto) * seats.length)
                    .append(" HUF\n");
        }
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

}
