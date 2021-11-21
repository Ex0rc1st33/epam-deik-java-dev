package com.epam.training.ticketservice.backend.room.persistance.repository;

import com.epam.training.ticketservice.backend.room.persistance.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

}
