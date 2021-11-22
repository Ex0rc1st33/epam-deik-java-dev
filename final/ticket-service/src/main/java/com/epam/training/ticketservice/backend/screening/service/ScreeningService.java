package com.epam.training.ticketservice.backend.screening.service;

import com.epam.training.ticketservice.backend.screening.model.ScreeningDto;

import java.util.List;

public interface ScreeningService {

    String createScreening(ScreeningDto screening);

    String deleteScreening(ScreeningDto screening);

    List<ScreeningDto> listScreenings();

}
