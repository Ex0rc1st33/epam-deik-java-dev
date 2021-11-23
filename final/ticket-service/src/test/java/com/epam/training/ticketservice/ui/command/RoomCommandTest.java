package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.room.model.RoomDto;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoomCommandTest {

    private final RoomService roomService = mock(RoomService.class);
    private final UserService userService = mock(UserService.class);
    private final RoomCommand underTest = new RoomCommand(roomService, userService);

    @Test
    public void testListRoomsShouldReturnCorrectListOfExistingRooms() {
        //Given
        List<RoomDto> list = List.of(
                RoomDto.builder()
                        .withName("Room1")
                        .withRowCount(20)
                        .withColCount(30)
                        .build());
        when(roomService.listRooms()).thenReturn(list);
        String expected = "Room Room1 with 600 seats, 20 rows and 30 columns";

        //When
        String actual = underTest.listRooms();

        //Then
        assertEquals(expected, actual);
        verify(roomService).listRooms();
    }

    @Test
    public void testListRoomsShouldReturnCorrectErrorMessageWhenTheAreNoRooms() {
        //Given
        when(roomService.listRooms()).thenReturn(List.of());
        String expected = "There are no rooms at the moment";

        //When
        String actual = underTest.listRooms();

        //Then
        assertEquals(expected, actual);
        verify(roomService).listRooms();
    }

}
