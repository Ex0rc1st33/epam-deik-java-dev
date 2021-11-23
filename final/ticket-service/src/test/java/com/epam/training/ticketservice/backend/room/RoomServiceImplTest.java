package com.epam.training.ticketservice.backend.room;

import com.epam.training.ticketservice.backend.room.model.RoomDto;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import com.epam.training.ticketservice.backend.room.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class RoomServiceImplTest {

    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final RoomService underTest = new RoomServiceImpl(roomRepository);

    @Test
    public void testCreateRoomShouldCallRoomRepositoryAndReturnCorrectMessageWhenGivenRoomValid() {
        //Given
        Room room = new Room("Room1", 20, 30, null);
        RoomDto roomDto = RoomDto.builder()
                .withName("Room1")
                .withRowCount(20)
                .withColCount(30)
                .build();
        when(roomRepository.save(room)).thenReturn(room);
        String expected = "Created room: " + room;

        //When
        String actual = underTest.createRoom(roomDto);

        //Then
        assertEquals(expected, actual);
        verify(roomRepository).save(room);
    }

    @Test
    public void testUpdateRoomShouldCallRoomRepositoryAndReturnCorrectMessageWhenGivenRoomExists() {
        //Given
        Room original = new Room("Room1", 20, 30, null);
        RoomDto roomDto = RoomDto.builder()
                .withName("Room1")
                .withRowCount(20)
                .withColCount(35)
                .build();
        when(roomRepository.findById(roomDto.getName())).thenReturn(Optional.of(original));
        Room result = new Room("Room1", 20, 35, null);
        when(roomRepository.save(result)).thenReturn(result);
        String expected = "Updated room: " + result;

        //When
        String actual = underTest.updateRoom(roomDto);

        //Then
        assertEquals(expected, actual);
        verify(roomRepository).findById(roomDto.getName());
        verify(roomRepository).save(result);
    }

    @Test
    public void testUpdateRoomShouldThrowIllegalStateExceptionWhenGivenRoomDoesNotExist() {
        //Given
        RoomDto roomDto = RoomDto.builder()
                .withName("Room1")
                .withRowCount(20)
                .withColCount(30)
                .build();
        when(roomRepository.findById(roomDto.getName())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest.updateRoom(roomDto));
        verify(roomRepository).findById(roomDto.getName());
    }

    @Test
    public void testDeleteRoomShouldCallRoomRepositoryAndReturnCorrectMessageWhenGivenRoomExists() {
        //Given
        Room room = new Room("Room1", 20, 30, null);
        RoomDto roomDto = RoomDto.builder()
                .withName("Room1")
                .withRowCount(20)
                .withColCount(30)
                .build();
        when(roomRepository.findById(roomDto.getName())).thenReturn(Optional.of(room));
        String expected = "Deleted room: " + room;

        //When
        String actual = underTest.deleteRoom(roomDto.getName());

        //Then
        assertEquals(expected, actual);
        verify(roomRepository).findById(roomDto.getName());
        verify(roomRepository).delete(room);
    }

    @Test
    public void testDeleteMovieShouldThrowIllegalStateExceptionWhenGivenMovieDoesNotExist() {
        //Given
        RoomDto roomDto = RoomDto.builder()
                .withName("Room1")
                .withRowCount(20)
                .withColCount(30)
                .build();
        when(roomRepository.findById(roomDto.getName())).thenReturn(Optional.empty());

        //When-Then
        assertThrows(IllegalStateException.class, () -> underTest.deleteRoom(roomDto.getName()));
        verify(roomRepository).findById(roomDto.getName());
    }

    @Test
    public void testListMoviesShouldReturnCorrectListOfMovies() {
        //Given
        List<Room> list = List.of(
                new Room("Room1", 20, 30, null),
                new Room("Room2", 20, 30, null),
                new Room("Room3", 20, 30, null));
        when(roomRepository.findAll()).thenReturn(list);
        List<RoomDto> expected = List.of(
                RoomDto.builder()
                        .withName("Room1")
                        .withRowCount(20)
                        .withColCount(30)
                        .build(),
                RoomDto.builder()
                        .withName("Room2")
                        .withRowCount(20)
                        .withColCount(30)
                        .build(),
                RoomDto.builder()
                        .withName("Room3")
                        .withRowCount(20)
                        .withColCount(30)
                        .build()
        );

        //When
        List<RoomDto> actual = underTest.listRooms();

        //Then
        assertEquals(expected, actual);
        verify(roomRepository).findAll();
    }

    @Test
    public void testGetMovieByTitleShouldCallMovieRepositoryAndReturnCorrectOptional() {
        //Given
        Room room = new Room("Room1", 20, 30, null);
        when(roomRepository.findById(room.getName())).thenReturn(Optional.of(room));
        Optional<Room> expected = Optional.of(room);

        //When
        Optional<Room> actual = underTest.getRoomByName(room.getName());

        //Then
        assertEquals(expected, actual);
        verify(roomRepository).findById(room.getName());
    }

}
