package com.epam.training.ticketservice.backend.room.service;

import com.epam.training.ticketservice.backend.room.model.RoomDto;

import java.util.List;

public interface RoomService {

    String createRoom(RoomDto room);

    String updateRoom(RoomDto room);

    String deleteRoom(String name);

    List<RoomDto> listRooms();

}
