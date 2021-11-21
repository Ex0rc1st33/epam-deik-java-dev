package com.epam.training.ticketservice.backend.screening.service;

import com.epam.training.ticketservice.backend.screening.model.ScreeningDTO;

import java.util.List;

public interface ScreeningService {

    String createScreening(ScreeningDTO screening);

    String deleteScreening(ScreeningDTO screening);

    List<ScreeningDTO> listScreenings();

}
