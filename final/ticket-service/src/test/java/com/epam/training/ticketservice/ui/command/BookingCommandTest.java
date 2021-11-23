package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.booking.service.BookingService;
import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class BookingCommandTest {

    private final BookingService bookingService = mock(BookingService.class);
    private final UserService userService = mock(UserService.class);
    private final BookingCommand underTest = new BookingCommand(bookingService, userService);

    @Test
    public void testBookShouldMakeNewBookingWhenUserSignedIn() {
        //Given
        UserDto userDto = UserDto.builder()
                .withUsername("lajos")
                .withRole(User.Role.USER)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 12:00")
                .withSeats("5,5 5,6")
                .withUser("lajos")
                .build();
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));

        //When
        underTest.book(bookingDto.getMovieTitle(), bookingDto.getRoomName(), bookingDto.getStartedAt(), bookingDto.getSeats());

        //Then
        verify(userService).getLoggedInUser();
        verify(bookingService).createBooking(bookingDto);
    }

    @Test
    public void testBookShouldReturnCorrectErrorMessageWhenUserNotSignedIn() {
        //Given
        BookingDto bookingDto = BookingDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 12:00")
                .withSeats("5,5 5,6")
                .withUser("lajos")
                .build();
        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        //When
        underTest.book(bookingDto.getMovieTitle(), bookingDto.getRoomName(), bookingDto.getStartedAt(), bookingDto.getSeats());

        //Then
        verify(userService).getLoggedInUser();
    }

}
