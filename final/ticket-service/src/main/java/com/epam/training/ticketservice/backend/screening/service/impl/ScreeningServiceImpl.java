package com.epam.training.ticketservice.backend.screening.service.impl;

import com.epam.training.ticketservice.backend.movie.persistance.entity.Movie;
import com.epam.training.ticketservice.backend.movie.persistance.repository.MovieRepository;
import com.epam.training.ticketservice.backend.room.persistance.entity.Room;
import com.epam.training.ticketservice.backend.room.persistance.repository.RoomRepository;
import com.epam.training.ticketservice.backend.screening.model.ScreeningDTO;
import com.epam.training.ticketservice.backend.screening.persistance.entity.Screening;
import com.epam.training.ticketservice.backend.screening.persistance.repository.ScreeningRepository;
import com.epam.training.ticketservice.backend.screening.service.ScreeningService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public ScreeningServiceImpl(ScreeningRepository screeningRepository, MovieRepository movieRepository, RoomRepository roomRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public String createScreening(ScreeningDTO screeningDTO) {
        Objects.requireNonNull(screeningDTO, "Screening cannot be null");
        Objects.requireNonNull(screeningDTO.getMovieTitle(), "Screening movieTitle cannot be null");
        Objects.requireNonNull(screeningDTO.getRoomName(), "Screening roomName cannot be null");
        Objects.requireNonNull(screeningDTO.getStartedAt(), "Screening startedAt cannot be null");
        Optional<Movie> movie = movieRepository.findById(screeningDTO.getMovieTitle());
        if (movie.isEmpty()) {
            return "Movie does not exist";
        }
        Optional<Room> room = roomRepository.findById(screeningDTO.getRoomName());
        if (room.isEmpty()) {
            return "Room does not exist";
        }
        if (isOverlapping(screeningDTO)) {
            return "There is an overlapping screening";
        }
        if (isInTheBreakPeriod(screeningDTO)) {
            return "This would start in the break period after another screening in this room";
        }
        Screening screening = new Screening(screeningDTO.getMovieTitle(), screeningDTO.getRoomName(), screeningDTO.getStartedAt());
        screeningRepository.save(screening);
        return "Created screening: " + screening;
    }

    @Override
    public String deleteScreening(ScreeningDTO screeningDTO) {
        Optional<Screening> screeningOptional = screeningRepository.findByMovieTitleAndRoomNameAndStartedAt(screeningDTO.getMovieTitle(),
                screeningDTO.getRoomName(), screeningDTO.getStartedAt());
        if (screeningOptional.isEmpty()) {
            return "Screening does not exist";
        }
        Screening screening = screeningOptional.get();
        screeningRepository.delete(screening);
        return "Deleted screening: " + screening;
    }

    @Override
    public List<ScreeningDTO> listScreenings() {
        return screeningRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private ScreeningDTO convertEntityToDto(Screening screening) {
        return ScreeningDTO.builder()
                .withMovieTitle(screening.getMovieTitle())
                .withRoomName(screening.getRoomName())
                .withStartedAt(screening.getStartedAt())
                .build();
    }

    private boolean isOverlapping(ScreeningDTO screeningDTO) {
        List<Screening> screeningList = screeningRepository.findAllByRoomName(screeningDTO.getRoomName());
        Optional<Movie> newMovie = movieRepository.findById(screeningDTO.getMovieTitle());
        int newLength = 0;
        if (newMovie.isPresent()) {
            newLength = newMovie.get().getLength();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime newStartedAt = LocalDateTime.parse(screeningDTO.getStartedAt(), formatter);
        LocalDateTime newFinishedAt = newStartedAt.plusMinutes(newLength + 1);
        for (Screening screening : screeningList) {
            Optional<Movie> existingMovie = movieRepository.findById(screening.getMovieTitle());
            int existingLength = 0;
            if (existingMovie.isPresent()) {
                existingLength = existingMovie.get().getLength();
            }
            LocalDateTime existingStartedAt = LocalDateTime.parse(screening.getStartedAt(), formatter);
            LocalDateTime existingFinishesAt = existingStartedAt.plusMinutes(existingLength + 1);
            if ((newStartedAt.isAfter(existingStartedAt) && newStartedAt.isBefore(existingFinishesAt)) ||
                (newFinishedAt.isAfter(existingStartedAt) && newFinishedAt.isBefore(existingFinishesAt))) {
                return true;
            }
        }
        return false;
    }

    private boolean isInTheBreakPeriod(ScreeningDTO screeningDTO) {
        List<Screening> screeningList = screeningRepository.findAllByRoomName(screeningDTO.getRoomName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime newStartedAt = LocalDateTime.parse(screeningDTO.getStartedAt(), formatter);
        for (Screening screening : screeningList) {
            Optional<Movie> existingMovie = movieRepository.findById(screening.getMovieTitle());
            int existingLength = 0;
            if (existingMovie.isPresent()) {
                existingLength = existingMovie.get().getLength();
            }
            LocalDateTime startOfBreak = LocalDateTime.parse(screening.getStartedAt(), formatter).plusMinutes(existingLength);
            LocalDateTime endOfBreak = startOfBreak.plusMinutes(10 + 1);
            if (newStartedAt.isAfter(startOfBreak) && newStartedAt.isBefore(endOfBreak)) {
                return true;
            }
        }
        return false;
    }

}
