package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.booking.service.BookingService;
import com.epam.training.ticketservice.backend.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.backend.pricecomponent.service.PriceComponentService;
import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class PriceCommand {

    private final PriceComponentService priceComponentService;
    private final BookingService bookingService;
    private final UserService userService;

    public PriceCommand(PriceComponentService priceComponentService,
                        BookingService bookingService,
                        UserService userService) {
        this.priceComponentService = priceComponentService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update base price", value = "Update base price of bookings")
    public String updateBasePrice(Integer basePrice) {
        return bookingService.updateBasePrice(basePrice);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create price component", value = "Create new price component")
    public String createPriceComponent(String name, Integer value) {
        PriceComponentDto priceComponentDto = PriceComponentDto.builder()
                .withName(name)
                .withValue(value)
                .build();
        return priceComponentService.createPriceComponent(priceComponentDto);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to room",
            value = "Attach an existing price component to an existing room")
    public String attachPriceComponentToRoom(String componentName, String roomName) {
        return priceComponentService.attachPriceComponentToRoom(componentName, roomName);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to movie",
            value = "Attach an existing price component to an existing movie")
    public String attachPriceComponentToMovie(String componentName, String movieTitle) {
        return priceComponentService.attachPriceComponentToMovie(componentName, movieTitle);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to screening",
            value = "Attach an existing price component to an existing screening")
    public String attachPriceComponentToScreening(String componentName,
                                                  String movieTitle,
                                                  String roomName,
                                                  String startedAt) {
        return priceComponentService.attachPriceComponentToScreening(componentName, movieTitle, roomName, startedAt);
    }

    @ShellMethod(key = "show price for", value = "Show price for a booking without actually making the booking")
    public String showPriceFor(String movieTitle, String roomName, String startedAt, String seats) {
        BookingDto bookingDto = BookingDto.builder()
                .withMovieTitle(movieTitle)
                .withRoomName(roomName)
                .withStartedAt(startedAt)
                .withSeats(seats)
                .build();
        return "The price for this booking would be "
                + (bookingService.calculateNewBookingTotalPrice(bookingDto))
                + " HUF";
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("you are not an admin!");
    }

}
