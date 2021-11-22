package com.epam.training.ticketservice.backend.booking.service;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;

import java.util.List;

public interface BookingService {

    String createBooking(BookingDto booking);

    List<BookingDto> getBookingsByUser(String user);

    String updateBasePrice(Integer basePrice);

    Integer getBasePriceOfExistingBooking(BookingDto booking);

    Integer getBasePrice();

}
