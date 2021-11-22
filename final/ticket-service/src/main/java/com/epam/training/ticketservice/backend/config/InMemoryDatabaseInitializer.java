package com.epam.training.ticketservice.backend.config;

import javax.annotation.PostConstruct;

import com.epam.training.ticketservice.backend.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.backend.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.backend.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class InMemoryDatabaseInitializer {

    private final UserRepository userRepository;
    /*private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;*/

    public InMemoryDatabaseInitializer(UserRepository userRepository/*,
                                       BookingRepository bookingRepository,
                                       MovieRepository movieRepository,
                                       RoomRepository roomRepository,
                                       ScreeningRepository screeningRepository*/) {
        this.userRepository = userRepository;
        /*this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.screeningRepository = screeningRepository;*/
    }

    @PostConstruct
    public void init() {
        User admin = new User("admin", "admin", User.Role.ADMIN);
        /*Booking booking = new Booking("Sátántangó", "Pedersoli", "2021-03-15 10:45", "5,5 5,6", "joska", 1500);
        Movie movie = new Movie("Sátántangó", "joska", 150);
        Room room = new Room("Pedersoli", 30, 20);
        Screening screening = new Screening("Sátántangó", "Pedersoli", "2021-03-15 10:45");*/
        userRepository.save(admin);
        /*bookingRepository.save(booking);
        movieRepository.save(movie);
        roomRepository.save(room);
        screeningRepository.save(screening);*/
    }
}
