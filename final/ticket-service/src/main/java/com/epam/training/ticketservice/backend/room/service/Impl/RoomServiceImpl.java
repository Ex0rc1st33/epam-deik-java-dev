package com.epam.training.ticketservice.backend.room.service.Impl;

import com.epam.training.ticketservice.backend.room.model.RoomDTO;
import com.epam.training.ticketservice.backend.room.persistance.entity.Room;
import com.epam.training.ticketservice.backend.room.persistance.repository.RoomRepository;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public String createRoom(RoomDTO roomDTO) {
        Objects.requireNonNull(roomDTO, "Room cannot be null");
        Objects.requireNonNull(roomDTO.getName(), "Room name cannot be null");
        Objects.requireNonNull(roomDTO.getRowCount(), "Room rowCount cannot be null");
        Objects.requireNonNull(roomDTO.getColCount(), "Room colCount cannot be null");
        Room room = new Room(roomDTO.getName(), roomDTO.getRowCount(), roomDTO.getColCount());
        roomRepository.save(room);
        return "Created room: " + room;
    }

    @Override
    public String updateRoom(RoomDTO roomDTO) {
        Objects.requireNonNull(roomDTO, "Room cannot be null");
        Objects.requireNonNull(roomDTO.getName(), "Room name cannot be null");
        Objects.requireNonNull(roomDTO.getRowCount(), "Room rowCount cannot be null");
        Objects.requireNonNull(roomDTO.getColCount(), "Room colCount cannot be null");
        Optional<Room> roomOptional = roomRepository.findById(roomDTO.getName());
        if (roomOptional.isEmpty()) {
            return "Room does not exist";
        }
        Room room = roomOptional.get();
        room.setRowCount(roomDTO.getRowCount());
        room.setColCount(roomDTO.getColCount());
        roomRepository.save(room);
        return "Updated room: " + room;
    }

    @Override
    public String deleteRoom(String name) {
        Optional<Room> roomOptional = roomRepository.findById(name);
        if (roomOptional.isEmpty()) {
            return "Room does not exist";
        }
        Room room = roomOptional.get();
        roomRepository.delete(room);
        return "Deleted room: " + room;
    }

    @Override
    public List<RoomDTO> listRooms() {
        return roomRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private RoomDTO convertEntityToDto(Room room) {
        return RoomDTO.builder()
                .withName(room.getName())
                .withRowCount(room.getRowCount())
                .withColCount(room.getColCount())
                .build();
    }

}
