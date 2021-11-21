package com.epam.training.ticketservice.backend.room.service;

import com.epam.training.ticketservice.backend.room.model.RoomDTO;

import java.util.List;

public interface RoomService {

    String createRoom(RoomDTO room);

    String updateRoom(RoomDTO room);

    String deleteRoom(String name);

    List<RoomDTO> listRooms();

}
