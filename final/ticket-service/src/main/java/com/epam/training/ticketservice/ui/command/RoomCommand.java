package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.room.model.RoomDto;
import com.epam.training.ticketservice.backend.room.service.RoomService;
import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class RoomCommand {

    private final RoomService roomService;
    private final UserService userService;

    public RoomCommand(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Create new room")
    public String createRoom(String name, Integer rowCount, Integer colCount) {
        RoomDto room = RoomDto.builder()
                .withName(name)
                .withRowCount(rowCount)
                .withColCount(colCount)
                .build();
        return roomService.createRoom(room);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Update existing room")
    public String updateRoom(String name, Integer rowCount, Integer colCount) {
        RoomDto room = RoomDto.builder()
                .withName(name)
                .withRowCount(rowCount)
                .withColCount(colCount)
                .build();
        return roomService.updateRoom(room);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Delete existing room")
    public String deleteRoom(String name) {
        return roomService.deleteRoom(name);
    }

    @ShellMethod(key = "list rooms", value = "List all existing rooms")
    public String listRooms() {
        List<RoomDto> rooms = roomService.listRooms();
        if (rooms.size() == 0) {
            return "There are no rooms at the moment";
        }
        StringBuilder buffer = new StringBuilder();
        for (RoomDto room : rooms) {
            buffer.append(room).append("\n");
        }
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("you are not an admin!");
    }

}
