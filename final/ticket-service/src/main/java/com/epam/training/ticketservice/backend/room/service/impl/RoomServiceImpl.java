package com.epam.training.ticketservice.backend.room.service.impl;

import com.epam.training.ticketservice.backend.room.model.RoomDto;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.persistence.repository.RoomRepository;
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
    public String createRoom(RoomDto roomDto) {
        checkValid(roomDto);
        Room room = new Room(roomDto.getName(), roomDto.getRowCount(), roomDto.getColCount(), null);
        saveRoom(room);
        return "Created room: " + room;
    }

    @Override
    public String updateRoom(RoomDto roomDto) {
        checkValid(roomDto);
        Room room = roomRepository.findById(roomDto.getName())
                .orElseThrow(() -> new IllegalStateException("Room does not exist"));
        room.setRowCount(roomDto.getRowCount());
        room.setColCount(roomDto.getColCount());
        saveRoom(room);
        return "Updated room: " + room;
    }

    @Override
    public String deleteRoom(String name) {
        Room room = roomRepository.findById(name)
                .orElseThrow(() -> new IllegalStateException("Room does not exist"));
        roomRepository.delete(room);
        return "Deleted room: " + room;
    }

    @Override
    public List<RoomDto> listRooms() {
        return roomRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<Room> getRoomByName(String roomName) {
        return roomRepository.findById(roomName);
    }

    @Override
    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    private RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
                .withName(room.getName())
                .withRowCount(room.getRowCount())
                .withColCount(room.getColCount())
                .build();
    }

    private void checkValid(RoomDto roomDto) {
        Objects.requireNonNull(roomDto, "Room cannot be null");
        Objects.requireNonNull(roomDto.getName(), "Room name cannot be null");
        Objects.requireNonNull(roomDto.getRowCount(), "Room rowCount cannot be null");
        Objects.requireNonNull(roomDto.getColCount(), "Room colCount cannot be null");
    }

}
