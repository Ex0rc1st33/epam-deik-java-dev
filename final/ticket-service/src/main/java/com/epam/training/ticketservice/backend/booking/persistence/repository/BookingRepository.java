package com.epam.training.ticketservice.backend.booking.persistence.repository;

import com.epam.training.ticketservice.backend.booking.persistence.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByMovieTitleAndRoomNameAndStartedAt(String movieTitle, String roomName, String startedAt);

    Optional<Booking> findByMovieTitleAndRoomNameAndStartedAtAndUserAndSeats(String movieTitle,
                                                                             String roomName,
                                                                             String startedAt,
                                                                             String user,
                                                                             String seats);

    List<Booking> findAllByUser(String user);

}
