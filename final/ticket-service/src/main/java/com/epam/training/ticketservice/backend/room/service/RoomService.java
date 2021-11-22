package com.epam.training.ticketservice.backend.room.service;

import com.epam.training.ticketservice.backend.room.model.RoomDto;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    String createRoom(RoomDto room);

    String updateRoom(RoomDto room);

    String deleteRoom(String name);

    List<RoomDto> listRooms();

    Optional<Room> getRoomByName(String roomName);

    void saveRoom(Room room);

}
