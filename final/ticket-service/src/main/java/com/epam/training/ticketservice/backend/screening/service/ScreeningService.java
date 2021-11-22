package com.epam.training.ticketservice.backend.screening.service;

import com.epam.training.ticketservice.backend.screening.model.ScreeningDto;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;

import java.util.List;
import java.util.Optional;

public interface ScreeningService {

    String createScreening(ScreeningDto screening);

    String deleteScreening(ScreeningDto screening);

    List<ScreeningDto> listScreenings();

    Optional<Screening> getMovieByTitleAndRoomNameAndStartedAt(String movieTitle, String roomName, String startedAt);

    void saveScreening(Screening screening);

}
