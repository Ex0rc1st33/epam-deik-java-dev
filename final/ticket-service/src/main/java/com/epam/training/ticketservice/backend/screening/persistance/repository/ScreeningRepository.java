package com.epam.training.ticketservice.backend.screening.persistance.repository;

import com.epam.training.ticketservice.backend.screening.persistance.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Optional<Screening> findByMovieTitleAndRoomNameAndStartedAt(String movieTitle, String roomName, String startedAt);

    List<Screening> findAllByRoomName(String roomName);

}
