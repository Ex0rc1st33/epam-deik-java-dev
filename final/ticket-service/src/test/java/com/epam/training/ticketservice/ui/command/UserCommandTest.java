package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.booking.service.BookingService;
import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserCommandTest {

    private final UserService userService = mock(UserService.class);
    private final BookingService bookingService = mock(BookingService.class);
    private final UserCommand underTest = new UserCommand(userService, bookingService);

    @Test
    public void testDescribeAccountShouldReturnCorrectSuccessMessageAndListExistingBookings() {
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
        when(bookingService.getBookingsByUser(userDto.getUsername())).thenReturn(List.of(bookingDto));
        when(bookingService.calculateExistingBookingTotalPrice(bookingDto)).thenReturn(3000);
        String expected = "Signed in with account 'lajos'\n"
                + "Your previous bookings are\n"
                + "Seats (5,5), (5,6) on Lord of the Rings in room Room1 "
                + "starting at 2021-03-15 12:00 for 3000 HUF";

        //When
        String actual = underTest.describeAccount();

        //Then
        assertEquals(expected, actual);
        verify(userService).getLoggedInUser();
        verify(bookingService).getBookingsByUser(userDto.getUsername());
        verify(bookingService).calculateExistingBookingTotalPrice(bookingDto);
    }

    @Test
    public void testDescribeAccountShouldReturnCorrectSuccessMessageAndNotListAnyBookings() {
        //Given
        UserDto userDto = UserDto.builder()
                .withUsername("lajos")
                .withRole(User.Role.USER)
                .build();
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        when(bookingService.getBookingsByUser(userDto.getUsername())).thenReturn(List.of());
        String expected = "Signed in with account 'lajos'\n"
                + "You have not booked any tickets yet";

        //When
        String actual = underTest.describeAccount();

        //Then
        assertEquals(expected, actual);
        verify(userService).getLoggedInUser();
        verify(bookingService).getBookingsByUser(userDto.getUsername());
    }

    @Test
    public void testDescribeAccountShouldReturnCorrectSuccessMessageForAdminAccounts() {
        //Given
        UserDto userDto = UserDto.builder()
                .withUsername("lajos")
                .withRole(User.Role.ADMIN)
                .build();
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        String expected = "Signed in with privileged account 'lajos'";

        //When
        String actual = underTest.describeAccount();

        //Then
        assertEquals(expected, actual);
        verify(userService).getLoggedInUser();
    }

    @Test
    public void testDescribeAccountShouldReturnCorrectErrorMessageWhenUserNotSignedIn() {
        //Given
        when(userService.getLoggedInUser()).thenReturn(Optional.empty());
        String expected = "You are not signed in";

        //When
        String actual = underTest.describeAccount();

        //Then
        assertEquals(expected, actual);
        verify(userService).getLoggedInUser();
    }

}
