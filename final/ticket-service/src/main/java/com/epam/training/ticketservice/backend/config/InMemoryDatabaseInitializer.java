package com.epam.training.ticketservice.backend.config;

import javax.annotation.PostConstruct;

import com.epam.training.ticketservice.backend.movie.persistance.repository.MovieRepository;
import com.epam.training.ticketservice.backend.room.persistance.repository.RoomRepository;
import com.epam.training.ticketservice.backend.screening.persistance.repository.ScreeningRepository;
import com.epam.training.ticketservice.backend.user.persistance.entity.User;
import com.epam.training.ticketservice.backend.user.persistance.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class InMemoryDatabaseInitializer {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

    public InMemoryDatabaseInitializer(UserRepository userRepository, MovieRepository movieRepository, RoomRepository roomRepository,
                                       ScreeningRepository screeningRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.screeningRepository = screeningRepository;
    }

    @PostConstruct
    public void init() {
        User admin = new User("admin", "admin", User.Role.ADMIN);
        //Movie movie1 = new Movie("Sátántangó", "drama", 450);
        //Room room1 = new Room("Pedersoli", 20, 10);
        //Screening screening1 = new Screening("Sátántangó", "Pedersoli", "2021-03-15 10:45");
        userRepository.save(admin);
        //movieRepository.save(movie1);
        //roomRepository.save(room1);
        //screeningRepository.save(screening1);
    }
}
