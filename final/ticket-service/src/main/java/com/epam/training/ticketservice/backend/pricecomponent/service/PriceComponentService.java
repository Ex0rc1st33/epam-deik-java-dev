package com.epam.training.ticketservice.backend.pricecomponent.service;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.pricecomponent.model.PriceComponentDto;

public interface PriceComponentService {

    String createPriceComponent(PriceComponentDto priceComponent);

    String attachPriceComponentToRoom(String componentName, String roomName);

    String attachPriceComponentToMovie(String componentName, String movieTitle);

    String attachPriceComponentToScreening(String componentName, String movieTitle, String roomName, String startedAt);

    Integer showPriceForComponents(BookingDto bookingDto);

}
