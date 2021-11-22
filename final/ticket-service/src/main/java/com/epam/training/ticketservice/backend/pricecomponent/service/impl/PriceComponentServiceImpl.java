package com.epam.training.ticketservice.backend.pricecomponent.service.impl;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.backend.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.backend.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.backend.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.backend.pricecomponent.service.PriceComponentService;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.backend.screening.persistence.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PriceComponentServiceImpl implements PriceComponentService {

    private final PriceComponentRepository priceComponentRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;

    public PriceComponentServiceImpl(PriceComponentRepository priceComponentRepository,
                                     RoomRepository roomRepository,
                                     MovieRepository movieRepository,
                                     ScreeningRepository screeningRepository) {
        this.priceComponentRepository = priceComponentRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
    }

    @Override
    public String createPriceComponent(PriceComponentDto priceComponentDto) {
        Objects.requireNonNull(priceComponentDto, "Price component cannot be null");
        Objects.requireNonNull(priceComponentDto.getName(), "Price component name cannot be null");
        Objects.requireNonNull(priceComponentDto.getValue(), "Price component value cannot be null");
        PriceComponent priceComponent = new PriceComponent(priceComponentDto.getName(), priceComponentDto.getValue());
        priceComponentRepository.save(priceComponent);
        return "Created price component: " + priceComponent;
    }

    @Override
    public String attachPriceComponentToRoom(String componentName, String roomName) {
        Optional<PriceComponent> priceComponentOptional = priceComponentRepository.findById(componentName);
        if (priceComponentOptional.isEmpty()) {
            return "Price component does not exist";
        }
        Optional<Room> roomOptional = roomRepository.findById(roomName);
        if (roomOptional.isEmpty()) {
            return "Room does not exist";
        }
        Room room = roomOptional.get();
        room.setPriceComponent(componentName);
        roomRepository.save(room);
        return "Attached " + priceComponentOptional.get() + " component to " + room + " room";
    }

    @Override
    public String attachPriceComponentToMovie(String componentName, String movieTitle) {
        Optional<PriceComponent> priceComponentOptional = priceComponentRepository.findById(componentName);
        if (priceComponentOptional.isEmpty()) {
            return "Price component does not exist";
        }
        Optional<Movie> movieOptional = movieRepository.findById(movieTitle);
        if (movieOptional.isEmpty()) {
            return "Movie does not exist";
        }
        Movie movie = movieOptional.get();
        movie.setPriceComponent(componentName);
        movieRepository.save(movie);
        return "Attached " + priceComponentOptional.get() + " component to " + movie + " movie";
    }

    @Override
    public String attachPriceComponentToScreening(String componentName,
                                                  String movieTitle,
                                                  String roomName,
                                                  String startedAt) {
        Optional<PriceComponent> priceComponentOptional = priceComponentRepository.findById(componentName);
        if (priceComponentOptional.isEmpty()) {
            return "Price component does not exist";
        }
        Optional<Screening> screeningOptional = screeningRepository
                .findByMovieTitleAndRoomNameAndStartedAt(movieTitle, roomName, startedAt);
        if (screeningOptional.isEmpty()) {
            return "Screening does not exist";
        }
        Screening screening = screeningOptional.get();
        screening.setPriceComponent(componentName);
        screeningRepository.save(screening);
        return "Attached " + priceComponentOptional.get() + " component to " + screening + " screening";
    }

    @Override
    public Integer showPriceFor(BookingDto bookingDto) {
        Room room = roomRepository.findById(bookingDto.getRoomName()).orElseThrow();
        Movie movie = movieRepository.findById(bookingDto.getMovieTitle()).orElseThrow();
        Screening screening = screeningRepository
                .findByMovieTitleAndRoomNameAndStartedAt(bookingDto.getMovieTitle(),
                        bookingDto.getRoomName(),
                        bookingDto.getStartedAt())
                .orElseThrow();
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

}
