package com.epam.training.ticketservice.backend.screening.service.impl;

import com.epam.training.ticketservice.backend.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.backend.movie.service.MovieService;
import com.epam.training.ticketservice.backend.room.model.RoomDto;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import com.epam.training.ticketservice.backend.screening.model.ScreeningDto;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.backend.screening.persistence.repository.ScreeningRepository;
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
    private final MovieService movieService;
    private final RoomService roomService;

    public ScreeningServiceImpl(ScreeningRepository screeningRepository,
                                MovieService movieService,
                                RoomService roomService) {
        this.screeningRepository = screeningRepository;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    @Override
    public String createScreening(ScreeningDto screeningDto) {
        checkValid(screeningDto);
        movieService.getMovieByTitle(screeningDto.getMovieTitle()).orElseThrow();
        roomService.getRoomByName(screeningDto.getRoomName()).orElseThrow();
        if (isOverlapping(screeningDto)) {
            return "There is an overlapping screening";
        }
        if (isInTheBreakPeriod(screeningDto)) {
            return "This would start in the break period after another screening in this room";
        }
        Screening screening = new Screening(screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartedAt(),
                null);
        saveScreening(screening);
        return "Created screening: " + screening;
    }

    @Override
    public String deleteScreening(ScreeningDto screeningDto) {
        Optional<Screening> screeningOptional = screeningRepository
                .findByMovieTitleAndRoomNameAndStartedAt(screeningDto.getMovieTitle(),
                        screeningDto.getRoomName(), screeningDto.getStartedAt());
        if (screeningOptional.isEmpty()) {
            return "Screening does not exist";
        }
        Screening screening = screeningOptional.get();
        screeningRepository.delete(screening);
        return "Deleted screening: " + screening;
    }

    @Override
    public List<ScreeningDto> listScreenings() {
        return screeningRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<Screening> getMovieByTitleAndRoomNameAndStartedAt(String movieTitle,
                                                                      String roomName,
                                                                      String startedAt) {
        return screeningRepository.findByMovieTitleAndRoomNameAndStartedAt(movieTitle, roomName, startedAt);
    }

    @Override
    public void saveScreening(Screening screening) {
        screeningRepository.save(screening);
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .withMovieTitle(screening.getMovieTitle())
                .withRoomName(screening.getRoomName())
                .withStartedAt(screening.getStartedAt())
                .build();
    }

    private boolean isOverlapping(ScreeningDto screeningDto) {
        List<Screening> screeningList = screeningRepository.findAllByRoomName(screeningDto.getRoomName());
        Optional<Movie> newMovie = movieService.getMovieByTitle(screeningDto.getMovieTitle());
        int newLength = newMovie.orElseThrow().getLength();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime newStartedAt = LocalDateTime.parse(screeningDto.getStartedAt(), formatter);
        LocalDateTime newFinishedAt = newStartedAt.plusMinutes(newLength + 1);
        for (Screening screening : screeningList) {
            Optional<Movie> existingMovie = movieService.getMovieByTitle(screening.getMovieTitle());
            int existingLength = existingMovie.orElseThrow().getLength();
            LocalDateTime existingStartedAt = LocalDateTime.parse(screening.getStartedAt(), formatter);
            LocalDateTime existingFinishesAt = existingStartedAt.plusMinutes(existingLength + 1);
            if ((newStartedAt.isAfter(existingStartedAt) && newStartedAt.isBefore(existingFinishesAt))
                    || (newFinishedAt.isAfter(existingStartedAt) && newFinishedAt.isBefore(existingFinishesAt))) {
                return true;
            }
        }
        return false;
    }

    private boolean isInTheBreakPeriod(ScreeningDto screeningDto) {
        List<Screening> screeningList = screeningRepository.findAllByRoomName(screeningDto.getRoomName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime newStartedAt = LocalDateTime.parse(screeningDto.getStartedAt(), formatter);
        for (Screening screening : screeningList) {
            Optional<Movie> existingMovie = movieService.getMovieByTitle(screening.getMovieTitle());
            int existingLength = existingMovie.orElseThrow().getLength();
            LocalDateTime startOfBreak = LocalDateTime.parse(screening.getStartedAt(), formatter)
                    .plusMinutes(existingLength);
            LocalDateTime endOfBreak = startOfBreak.plusMinutes(10 + 1);
            if (newStartedAt.isAfter(startOfBreak) && newStartedAt.isBefore(endOfBreak)) {
                return true;
            }
        }
        return false;
    }

    private void checkValid(ScreeningDto screeningDto) {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovieTitle(), "Screening movieTitle cannot be null");
        Objects.requireNonNull(screeningDto.getRoomName(), "Screening roomName cannot be null");
        Objects.requireNonNull(screeningDto.getStartedAt(), "Screening startedAt cannot be null");
    }

}
