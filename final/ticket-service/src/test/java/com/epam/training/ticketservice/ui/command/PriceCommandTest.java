package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.booking.service.BookingService;
import com.epam.training.ticketservice.backend.pricecomponent.service.PriceComponentService;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PriceCommandTest {

    private final PriceComponentService priceComponentService = mock(PriceComponentService.class);
    private final BookingService bookingService = mock(BookingService.class);
    private final UserService userService = mock(UserService.class);
    private final PriceCommand underTest = new PriceCommand(priceComponentService, bookingService, userService);

    @Test
    public void testListRoomsShouldReturnCorrectListOfExistingRooms() {
        //Given
        BookingDto bookingDto = BookingDto.builder()
                .withMovieTitle("Lord of the Rings")
                .withRoomName("Room1")
                .withStartedAt("2021-03-15 12:00")
                .withSeats("5,5 6,6")
                .withUser(null)
                .build();
        when(bookingService.calculateNewBookingTotalPrice(bookingDto)).thenReturn(3000);
        String expected = "The price for this booking would be 3000 HUF";

        //When
        String actual = underTest.showPriceFor(bookingDto.getMovieTitle(),
                bookingDto.getRoomName(),
                bookingDto.getStartedAt(),
                bookingDto.getSeats());

        //Then
        assertEquals(expected, actual);
        verify(bookingService).calculateNewBookingTotalPrice(bookingDto);
    }

}
