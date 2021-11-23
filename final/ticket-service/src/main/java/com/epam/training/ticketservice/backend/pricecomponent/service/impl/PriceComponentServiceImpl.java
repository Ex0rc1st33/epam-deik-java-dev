package com.epam.training.ticketservice.backend.pricecomponent.service.impl;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.backend.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.backend.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.backend.pricecomponent.service.PriceComponentService;
import com.epam.training.ticketservice.backend.room.model.RoomDto;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.backend.screening.service.ScreeningService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PriceComponentServiceImpl implements PriceComponentService {

    private final PriceComponentRepository priceComponentRepository;
    private final MovieService movieService;
    private final RoomService roomService;
    private final ScreeningService screeningService;

    public PriceComponentServiceImpl(PriceComponentRepository priceComponentRepository,
                                     MovieService movieService,
                                     RoomService roomService,
                                     ScreeningService screeningService) {
        this.priceComponentRepository = priceComponentRepository;
        this.movieService = movieService;
        this.roomService = roomService;
        this.screeningService = screeningService;
    }

    @Override
    public String createPriceComponent(PriceComponentDto priceComponentDto) {
        checkValid(priceComponentDto);
        PriceComponent priceComponent = new PriceComponent(priceComponentDto.getName(), priceComponentDto.getValue());
        priceComponentRepository.save(priceComponent);
        return "Created price component: " + priceComponent;
    }

    @Override
    public String attachPriceComponentToRoom(String componentName, String roomName) {
        PriceComponent priceComponent = priceComponentRepository.findById(componentName)
                .orElseThrow(() -> new IllegalStateException("Price component does not exist"));
        Room room = roomService.getRoomByName(roomName)
                .orElseThrow(() -> new IllegalStateException("Room does not exist"));
        room.setPriceComponent(componentName);
        roomService.saveRoom(room);
        return "Attached " + priceComponent + " component to " + room + " room";
    }

    @Override
    public String attachPriceComponentToMovie(String componentName, String movieTitle) {
        PriceComponent priceComponent = priceComponentRepository.findById(componentName)
                .orElseThrow(() -> new IllegalStateException("Price component does not exist"));
        Movie movie = movieService.getMovieByTitle(movieTitle)
                .orElseThrow(() -> new IllegalStateException("Movie does not exist"));
        movie.setPriceComponent(componentName);
        movieService.saveMovie(movie);
        return "Attached " + priceComponent + " component to " + movie + " movie";
    }

    @Override
    public String attachPriceComponentToScreening(String componentName,
                                                  String movieTitle,
                                                  String roomName,
                                                  String startedAt) {
        PriceComponent priceComponent = priceComponentRepository.findById(componentName)
                .orElseThrow(() -> new IllegalStateException("Price component does not exist"));
        Screening screening = screeningService
                .getMovieByTitleAndRoomNameAndStartedAt(movieTitle, roomName, startedAt)
                .orElseThrow(() -> new IllegalStateException("Screening does not exist"));
        screening.setPriceComponent(componentName);
        screeningService.saveScreening(screening);
        return "Attached " + priceComponent + " component to " + screening + " screening";
    }

    @Override
    public Integer showPriceForComponents(BookingDto bookingDto) {
        Room room = roomService.getRoomByName(bookingDto.getRoomName())
                .orElseThrow(() -> new IllegalStateException("Room does not exist"));
        Movie movie = movieService.getMovieByTitle(bookingDto.getMovieTitle())
                .orElseThrow(() -> new IllegalStateException("Movie does not exist"));
        Screening screening = screeningService.getMovieByTitleAndRoomNameAndStartedAt(bookingDto.getMovieTitle(),
                        bookingDto.getRoomName(),
                        bookingDto.getStartedAt())
                .orElseThrow(() -> new IllegalStateException("Screening does not exist"));
        String roomComponent = room.getPriceComponent();
        String movieComponent = movie.getPriceComponent();
        String screeningComponent = screening.getPriceComponent();
        return (roomComponent != null ? priceComponentRepository
                .findById(roomComponent).orElseThrow().getValue() : 0)
                + (movieComponent != null ? priceComponentRepository
                .findById(movieComponent).orElseThrow().getValue() : 0)
                + (screeningComponent != null ? priceComponentRepository
                .findById(screeningComponent).orElseThrow().getValue() : 0);
    }

    private void checkValid(PriceComponentDto priceComponentDto) {
        Objects.requireNonNull(priceComponentDto, "Price component cannot be null");
        Objects.requireNonNull(priceComponentDto.getName(), "Price component name cannot be null");
        Objects.requireNonNull(priceComponentDto.getValue(), "Price component value cannot be null");
    }

}
